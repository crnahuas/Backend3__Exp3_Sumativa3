# SAGA del retiro

Se documenta una SAGA coreografiada: cada microservicio reacciona a hechos publicados y no existe un coordinador central. Para la implementación actual, `backend-core` evalúa el retiro y `ms-mensajeria` realiza el paso posterior de notificación.

## Secuencia normal

1. `bffcajero` valida el payload y envía el comando REST con `X-Correlation-Id`.
2. `backend-core` aplica las reglas de negocio y genera `RetiroProcesadoEvent` con el mismo `correlationId`.
3. ActiveMQ conserva el evento en `retiros.procesados`.
4. Una instancia de `ms-mensajeria` consume el evento y prepara la notificación.
5. La sesión JMS confirma el mensaje sólo cuando finaliza el procesamiento.

## Fallos y compensaciones

| Falla | Respuesta | Compensación o recuperación |
|---|---|---|
| `backend-core` no disponible | El circuit breaker del BFF responde de forma controlada | No existe operación ni evento que compensar; el cliente puede reintentar |
| ActiveMQ no disponible al publicar | La operación responde con error y queda registrada | Para producción se debe incorporar Transactional Outbox antes de ejecutar un débito irreversible |
| `ms-mensajeria` falla temporalmente | Rollback JMS y reintento con backoff | Reentrega automática, sin intervención del productor |
| Evento inválido o fallo permanente | Reintentos agotados y mensaje en `ActiveMQ.DLQ` | Operación manual: inspeccionar correlación, corregir causa y reenviar de forma controlada |
| Notificación duplicada | Entrega al menos una vez | Deduplicar por `eventId` antes de invocar al proveedor externo |

## Evolución necesaria para retiros reales

La implementación todavía calcula una respuesta sobre el saldo consultado; no registra un débito contable. Antes de convertirla en retiro real deben agregarse: transacción de débito en `backend-core`, tabla Outbox en la misma transacción, publicador de Outbox e idempotencia persistente en consumidores. Si un paso posterior exigiera reversar dinero, se incorporaría `RetiroCompensadoEvent` y una operación idempotente de reversa en `backend-core`.
