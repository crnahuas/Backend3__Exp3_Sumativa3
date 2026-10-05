# Plan de pruebas y evidencias — Semana 8

Este documento permite repetir las pruebas de la pauta en el ambiente académico. Antes de comenzar, copie `.env.example` a `.env`, reemplace los valores marcados y ejecute `./scripts/preparar-wallet.sh`.

## Matriz de cobertura

| Criterio | Implementación | Prueba esperada |
|---|---|---|
| OAuth2 funcional | `auth-server`, Resource Servers y scopes | Token `client_credentials`; 401 sin token; 403 sin scope; acceso con scope |
| Imágenes Docker | `Dockerfile` multi-stage con `SERVICE` | `docker compose build` termina sin errores y aparecen ocho imágenes `banco/*:semana8` |
| Orquestación | `docker-compose.yaml` | `docker compose ps` muestra infraestructura y microservicios en ejecución |
| Resilience4j | Circuit breaker `backendCore` en cada BFF | Detener `backend-core`, repetir llamadas y observar fallback/estado abierto |
| JMS | Productor en `backend-core`, ActiveMQ y consumidor transaccional | Retiro produce evento; consumidor registra correlación; reintentos y DLQ ante error |
| Documentación | README, ADR, SAGA, contrato JSON Schema | Revisión del repositorio y capturas reales de ejecución |

## 1. Construcción y estado de los contenedores

```bash
docker compose up --build -d
docker compose ps
docker images --format '{{.Repository}}:{{.Tag}}' | grep '^banco/'
```

## 2. Flujo OAuth2

```bash
set -a
. ./.env
set +a

READ_TOKEN=$(curl -fsS -u "$OAUTH2_CLIENT_ID:$OAUTH2_CLIENT_SECRET" \
  -X POST http://localhost:9000/oauth2/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data 'grant_type=client_credentials&scope=cuentas.read' \
  | jq -r '.access_token')

FULL_TOKEN=$(curl -fsS -u "$OAUTH2_CLIENT_ID:$OAUTH2_CLIENT_SECRET" \
  -X POST http://localhost:9000/oauth2/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data 'grant_type=client_credentials&scope=cuentas.read retiros.write' \
  | jq -r '.access_token')
```

Pruebas de acceso:

```bash
# Esperado: 401
curl -i http://localhost:8084/api/cajero/cuentas/101

# Esperado: 403 porque falta retiros.write
curl -i -X POST http://localhost:8084/api/cajero/cuentas/101/retiro \
  -H "Authorization: Bearer $READ_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"monto":1000}'

# Esperado: 200 si Oracle contiene la cuenta y tiene saldo
curl -i -X POST http://localhost:8084/api/cajero/cuentas/101/retiro \
  -H "Authorization: Bearer $FULL_TOKEN" \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-Id: semana8-evidencia-001' \
  -d '{"monto":1000}'
```

## 3. Resilience4j

```bash
docker compose stop backend-core
```

Repita cinco veces una consulta autenticada al BFF. Las primeras fallas deben devolver un error controlado y, alcanzado el umbral, el circuito debe abrirse. Consulte métricas o logs:

```bash
docker compose logs --since=5m bffcajero
docker compose start backend-core
```

Transcurridos 10 segundos, una solicitud válida vuelve a probar la dependencia y permite demostrar recuperación.

## 4. JMS, reintentos y DLQ

Después del retiro exitoso:

```bash
docker compose logs --since=5m backend-core ms-mensajeria
```

Corrobore el mismo `correlationId` en `event=jms_event_published` y `event=jms_event_consumed`. Para demostrar persistencia asíncrona, detenga `ms-mensajeria`, realice otro retiro, observe el mensaje pendiente en `http://localhost:8161` y vuelva a iniciar el consumidor.

Un mensaje inválido debe mostrar `deliveryCount` creciente y, al agotar tres reintentos, aparecer en `ActiveMQ.DLQ`. No reenvíe mensajes de la DLQ sin corregir antes la causa.

## 5. Pruebas automatizadas

```bash
for module in auth-server backend-core bffweb bffmobile bffcajero ms-mensajeria config-server discovery-server; do
  (cd "$module" && mvn -B test)
done
```
