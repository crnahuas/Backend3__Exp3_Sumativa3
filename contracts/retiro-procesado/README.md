# Contrato RetiroProcesadoEvent

La versión vigente es `1.0.0` y su contrato ejecutable está en [`v1/schema.json`](v1/schema.json). Los ejemplos aprobados se mantienen junto al esquema para que productor y consumidores puedan usarlos en pruebas de contrato.

## Política de evolución

- El número sigue SemVer: `MAJOR.MINOR.PATCH`.
- Un cambio aditivo y opcional incrementa `MINOR`; una corrección documental incrementa `PATCH`.
- Eliminar, renombrar, cambiar el tipo o volver obligatorio un campo existente requiere una nueva versión `MAJOR` y una cola o estrategia de migración explícita.
- Los productores no cambian el significado de un campo publicado.
- Los consumidores ignoran propiedades desconocidas. `ms-mensajeria` acepta temporalmente eventos heredados sin metadatos como versión `0.9.0`, según [`legacy-v0-example.json`](legacy-v0-example.json).
- Antes de retirar la compatibilidad `0.x`, se debe comprobar que no queden productores antiguos ni mensajes pendientes en la cola o DLQ.

## Encabezados JMS

El JSON se replica con metadatos de transporte para trazabilidad: `JMSCorrelationID`, `eventId`, `eventType` y `eventVersion`. El contrato de negocio sigue siendo el JSON; los encabezados facilitan búsqueda y diagnóstico sin reemplazarlo.
