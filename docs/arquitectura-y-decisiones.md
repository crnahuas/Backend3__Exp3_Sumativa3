# Arquitectura y decisiones

## Límites de responsabilidad

| Componente | Tipo | Responsabilidad | No debe hacer |
|---|---|---|---|
| `auth-server` | Infraestructura de seguridad | Autenticar al cliente OAuth2 y emitir JWT firmados con scopes | Aplicar reglas bancarias o adaptar respuestas de canal |
| `bffweb`, `bffmobile`, `bffcajero` | BFF | Validar JWT/scopes, validar la forma del request, adaptar DTOs, propagar el Bearer token y aplicar circuit breaker | Decidir saldo, ejecutar reglas bancarias o publicar eventos de dominio |
| `backend-core` | Microservicio de negocio | Validar JWT/scopes, consultar la cuenta, evaluar el retiro y publicar `RetiroProcesadoEvent` | Adaptar respuestas a una interfaz web o móvil |
| `ms-mensajeria` | Microservicio de negocio | Consumir eventos y preparar notificaciones | Consultar sincrónicamente al BFF o decidir la aprobación del retiro |
| ActiveMQ | Infraestructura | Persistir, distribuir, reintentar y aislar mensajes fallidos | Contener lógica de negocio |
| Config Server / Eureka | Infraestructura | Centralizar propiedades y descubrir instancias | Exponer capacidades de negocio |

Esta delimitación corrige el acoplamiento anterior, donde `bffcajero` calculaba el retiro y publicaba el evento. Ahora el BFF sólo adapta el canal; la decisión y el hecho de dominio nacen en `backend-core`.

## Decisiones

### ADR-001: Queue JMS con consumidores competidores

Se usa `retiros.procesados` porque cada notificación debe procesarse una sola vez por el grupo `ms-mensajeria`. Varias instancias compiten por mensajes y permiten escalamiento horizontal. Si en el futuro varios dominios independientes necesitan el mismo evento, se migrará a un tópico con una suscripción durable por dominio.

### ADR-002: Contrato versionado y compatible

El evento incluye identidad, tipo, versión, instante y correlación. El esquema permite campos adicionales para evolución aditiva. El consumidor ignora campos desconocidos y conserva una ventana de compatibilidad con la forma heredada `0.9.0`.

### ADR-003: Reintentos transaccionales y DLQ

El listener usa una sesión JMS transaccional. Una excepción revierte la recepción y ActiveMQ reentrega con backoff. Tras tres reintentos, el broker aplica su estrategia de mensajes venenosos y mueve el mensaje a `ActiveMQ.DLQ`. No se captura y descarta silenciosamente ningún error.

### ADR-004: Entrega al menos una vez

JMS ofrece entrega *at least once*: un consumidor puede recibir un duplicado si falla después de efectuar un efecto y antes del commit. Todo efecto real de notificación debe usar `eventId` como clave idempotente. La persistencia de esa clave será obligatoria al conectar un proveedor real de mensajes.

### ADR-005: OAuth2 con JWT y scopes

Se usa el grant `client_credentials` para la demostración máquina-a-máquina. `auth-server` firma JWT con una llave RSA efímera y publica su JWK. Los BFF y `backend-core` actúan como Resource Servers y validan firma, expiración y emisor. `cuentas.read` autoriza consultas y `retiros.write` autoriza retiros. El BFF propaga el token original al core para evitar saltarse el control de acceso interno.

Para producción, el cliente y las llaves deben almacenarse de forma persistente o delegarse a un proveedor de identidad. La configuración en memoria es deliberada para el entorno académico reproducible.

### ADR-006: Contenedores sin secretos incorporados

Un Dockerfile multi-stage genera una imagen por módulo y ejecuta cada aplicación como usuario sin privilegios. Compose inyecta secretos mediante `.env`, monta el wallet Oracle sólo en `backend-core` y conecta los componentes mediante una red privada. Ninguna contraseña del entorno ni archivo del wallet se incorpora a las imágenes. Los keystores PKCS12 incluidos en los BFF son certificados autofirmados exclusivamente demostrativos.

## Flujo vigente

```text
Cliente -> auth-server -> JWT
   |
   +-> bffcajero [valida JWT/scope]
          |
          +-> backend-core [propaga y valida JWT]
                    |
                    +-> Oracle
                    +-> retiros.procesados
                              |
                              v
                       ms-mensajeria
                              |
            error -> reintentos -> ActiveMQ.DLQ
```
