# Auditoria del proyecto email-helper

Fecha: 2026_05_09  
Proyecto: `email-helper`  
Version declarada: `5.3.0`  
Tipo: libreria Java/Maven para envio de correos SMTP con Jakarta Mail

## 1. Resumen ejecutivo

El proyecto es una libreria pequena y acotada para construir y enviar correos mediante SMTP. La separacion principal entre API, modelo, validacion, helpers y excepciones es razonable, y el diseno evita incluir una implementacion de logging en produccion, lo cual es correcto para una libreria reutilizable.

El estado actual, sin embargo, no esta listo para publicacion ni consumo seguro sin correcciones previas. Los riesgos mas importantes son:

- Hay una credencial SMTP real o aparentemente real versionada en `src/test/java/local/jarios/email/EmailDemo.java`.
- La configuracion exige Java 21, pero el entorno local detectado usa Java 8 y no tiene Maven disponible en `PATH`.
- No existen tests automatizados reales; solo hay una demo con `main`.
- El README esta desactualizado y referencia clases/rutas que ya no coinciden con el codigo.
- Hay APIs de libreria con efectos de proceso global, como `System.exit`, que pueden romper aplicaciones consumidoras.
- La generacion de HTML concatena datos sin escape, exponiendo riesgo de inyeccion HTML en correos.

Prioridad recomendada: retirar/rotar credenciales, estabilizar entorno de build, anadir tests unitarios de validacion/envio, limpiar API publica y actualizar documentacion.

## 2. Alcance revisado

Se revisaron:

- `pom.xml`
- `README.md`
- Codigo bajo `src/main/java/local/jarios/email`
- Demo bajo `src/test/java/local/jarios/email/EmailDemo.java`
- Configuracion de test `src/test/resources/logback-test.xml`
- Estado Git actual del workspace

Actualizacion posterior: se configuraron para la sesion las rutas:

- `JAVA_HOME=C:\java\software\jdk-21.0.9`
- `MAVEN_HOME=C:\java\software\apache-maven-3.9.15`

Con estas rutas, Maven y Java quedan operativos.

## 3. Estado del repositorio

El workspace tenia cambios sin confirmar antes de generar esta auditoria:

- `README.md` modificado
- `pom.xml` modificado
- `src/main/java/local/jarios/email/EmailDemo.java` eliminado
- `src/main/java/local/jarios/email/api/EmailServiceImpl.java` modificado
- `src/main/java/local/jarios/email/api/ErrorNotificationService.java` anadido/modificado
- `src/main/java/local/jarios/email/helper/ErrorEmailBuilder.java` anadido/modificado
- `src/main/java/local/jarios/email/helper/ExceptionUtils.java` anadido/modificado
- `src/main/java/local/jarios/email/helper/FinalDelProgramaHelper.java` modificado
- Recursos `logback.xml`, `spotbugs-security-exclude.xml` y `spotbugs-security-include.xml` eliminados
- `src/test/java/local/jarios/email/EmailDemo.java` anadido
- `src/test/resources/` sin seguimiento previo

La auditoria refleja este estado actual del workspace.

## 4. Verificaciones ejecutadas

### 4.1 Entorno

- Inicialmente `java -version` devolvia Java 8: `1.8.0_491`.
- Tras configurar `JAVA_HOME`, `java -version` devuelve Java 21: `21.0.9`.
- `mvn -version` devuelve Apache Maven `3.9.15`.
- `pom.xml` exige Java 21 mediante `maven.compiler.release` y `maven-enforcer-plugin`.
- No hay Maven Wrapper (`mvnw`) en el proyecto.

Impacto: el proyecto puede validarse si se establecen explicitamente esas rutas, pero sigue sin ser completamente reproducible en maquinas nuevas porque no incluye Maven Wrapper.

### 4.2 Comandos ejecutados

- `mvn test`: correcto, `BUILD SUCCESS`.
- `mvn -Pquality checkstyle:check spotbugs:check`: incorrecto, falla en Checkstyle antes de llegar a SpotBugs.

Resultado de calidad:

- Checkstyle reporta 142 violaciones usando `sun_checks.xml`.
- Las incidencias son principalmente JavaDoc/package-info ausente, lineas de mas de 80 caracteres, imports no usados, imports wildcard, parametros no `final`, clases no `final` y formato de concatenaciones.

## 5. Arquitectura y diseno

### Fortalezas

- `EmailService` define un contrato claro de envio.
- `EmailSender` permite separar la orquestacion del transporte SMTP.
- `EmailData` como `record` simplifica el modelo.
- `EmailException` centraliza errores de dominio.
- La libreria depende de `slf4j-api` y deja la implementacion de logging al consumidor.
- `EmailRequestValidator` concentra validacion de entrada.

### Debilidades

- `FinalDelProgramaHelper` esta dentro de `src/main` y llama a `System.exit`. Una libreria no deberia terminar el proceso de la aplicacion consumidora.
- `ErrorNotificationService` tiene remitente y destinatario hardcodeados.
- La deteccion de correo de error depende del prefijo textual del asunto.
- Hay helpers HTML con concatenacion directa de datos sin escape.
- El codigo mezcla responsabilidades de libreria y ejecucion/demo.

## 6. Hallazgos priorizados

### Critico: credencial SMTP versionada

Archivo: `src/test/java/local/jarios/email/EmailDemo.java`

Lineas relevantes:

- Linea 120: credencial comentada.
- Linea 134: `SMTP_PASSWORD` con valor en claro.

Riesgo:

- Compromiso de cuenta SMTP si la clave es real.
- Uso indebido del servidor de correo.
- Incumplimiento de buenas practicas de seguridad y trazabilidad.

Accion recomendada:

- Revocar o rotar inmediatamente la credencial.
- Eliminarla del repositorio.
- Sustituirla por variables de entorno o properties locales no versionadas.
- Revisar historico Git si la credencial ya fue commiteada.

### Alto: entorno incompatible con la configuracion del proyecto

Archivo: `pom.xml`

Lineas relevantes:

- Linea 35: `maven.compiler.release` configurado a `21`.
- Lineas 116-118: `requireJavaVersion` exige Java 21.

Riesgo:

- Builds fallidos en estaciones o CI con Java inferior.
- Falsa sensacion de calidad si no existe CI validando Java 21.

Evidencia local:

- Java local detectado: `1.8.0_491`.
- Maven no disponible en `PATH`.

Accion recomendada:

- Instalar/configurar JDK 21 y Maven.
- Anadir Maven Wrapper (`mvnw`) para reproducibilidad.
- Documentar setup exacto.
- Validar en CI con JDK 21.

### Alto: ausencia de tests automatizados reales

Archivos:

- `src/test/java/local/jarios/email/EmailDemo.java`
- `pom.xml`

Observaciones:

- Hay propiedades para `junit.version` y `assertj.version` en `pom.xml`, lineas 44-45.
- No hay dependencias JUnit ni AssertJ declaradas.
- El unico Java bajo `src/test` es una demo ejecutable con `main`, no tests.

Riesgo:

- Cambios en validacion, composicion MIME o manejo de errores pueden romperse sin deteccion automatica.
- Dificulta evolucionar la libreria con seguridad.

Accion recomendada:

- Anadir `junit-jupiter` y, si aplica, AssertJ como dependencias de test.
- Crear tests unitarios para:
  - validacion de `EmailData`
  - validacion de propiedades SMTP
  - direcciones multiples
  - errores de `EmailSender`
  - construccion de `MimeMessage`
  - no reintento/bucle en notificaciones de error

### Alto: posible NullPointerException al validar `body`

Archivos:

- `src/main/java/local/jarios/email/validator/EmailRequestValidator.java`
- `src/main/java/local/jarios/email/helper/TextHelper.java`

Lineas relevantes:

- `EmailRequestValidator.java:76` llama a `TextHelper.recortar(data.body(), ...)`.
- `TextHelper.java:32` usa `cadena.length()` sin comprobar `null`.

Riesgo:

- Si `data.body()` es `null`, la validacion no lanza `EmailException` clara sino `NullPointerException`.

Accion recomendada:

- Validar `body` con `isBlank(data.body())` antes de recortar.
- Hacer `TextHelper.recortar` null-safe o documentar que no acepta null.

### Alto: HTML sin escape

Archivo: `src/main/java/local/jarios/email/helper/EmailHelper.java`

Lineas relevantes:

- Linea 58 concatena `titulo` dentro de HTML.
- Linea 77 concatena `key` y `value` dentro de HTML.
- Lineas 121-123 agregan filas sin sanitizacion.

Archivo adicional:

- `src/main/java/local/jarios/email/helper/ErrorEmailBuilder.java`
- Lineas 33-46 insertan contexto, excepcion y stacktrace en HTML.

Riesgo:

- Inyeccion HTML en correos si algun valor procede de entrada externa.
- Correos malformados o con contenido no esperado.

Accion recomendada:

- Escapar contenido dinamico antes de insertarlo en HTML.
- Mantener separado HTML de datos.
- Considerar una utilidad central `escapeHtml`.

### Alto: `System.exit` en codigo de libreria

Archivo: `src/main/java/local/jarios/email/helper/FinalDelProgramaHelper.java`

Linea relevante:

- Linea 62: `System.exit(exitCode)`.

Riesgo:

- Una aplicacion consumidora puede terminar abruptamente si invoca este helper.
- Dificulta tests automatizados.
- Rompe el principio de minima sorpresa en una libreria.

Accion recomendada:

- Mover este helper a demo/app, no a la libreria principal.
- O devolver un codigo/resultado en lugar de llamar a `System.exit`.

### Medio: `ErrorNotificationService` hardcodea direcciones

Archivo: `src/main/java/local/jarios/email/api/ErrorNotificationService.java`

Lineas relevantes:

- Lineas 28-29: remitente y destinatario fijos.

Riesgo:

- La libreria queda acoplada a un entorno concreto.
- Reutilizacion limitada.
- Posible filtrado de errores a destinatarios no deseados.

Accion recomendada:

- Recibir `from` y `to` por constructor, configuracion o parametro.
- Evitar valores corporativos en codigo de libreria.

### Medio: deteccion fragil de correo de error

Archivo: `src/main/java/local/jarios/email/api/EmailServiceImpl.java`

Lineas relevantes:

- Lineas 53-56 y 69-72 silencian errores si `isErrorNotification(data)` devuelve true.
- Lineas 88-90 detectan por `subject().startsWith("ERROR")` con simbolo visual en el codigo.

Riesgo:

- Si cambia el asunto, se pierde la proteccion antibucle.
- Si un correo normal empieza igual, se ocultara un fallo real.
- El uso de simbolo Unicode en logica de control puede dar problemas de encoding.

Accion recomendada:

- Modelar explicitamente el tipo de correo o introducir una opcion de envio sin notificacion recursiva.
- No basar control de flujo en texto visible al usuario.

### Medio: errores de envio de notificacion quedan ocultos para el llamador

Archivo: `src/main/java/local/jarios/email/api/ErrorNotificationService.java`

Lineas relevantes:

- Lineas 36-39 capturan cualquier excepcion y solo loguean.

Riesgo:

- Puede ser intencionado para evitar bucles, pero impide a la aplicacion saber si fallo la notificacion.

Accion recomendada:

- Devolver `boolean`, resultado tipado o publicar metrica/evento.
- Mantener la no propagacion si es requisito, pero hacerlo observable.

### Medio: README desactualizado

Archivo: `README.md`

Lineas relevantes:

- Lineas 36-38 indican `EmailDemo.java` en `src/main`, pero ahora esta en `src/test`.
- Linea 50 referencia `EmailServiceException.java`, clase que no existe.
- Linea 55 referencia `EmailValidator.java`, clase que no existe con ese nombre.
- Lineas 97-101 muestran snippet incompleto y sin cierre.
- Linea 62 deja `PENDIENTE`.

Riesgo:

- Onboarding incorrecto.
- Integraciones consumidoras pueden usar nombres/metodos obsoletos.

Accion recomendada:

- Actualizar README con estructura real.
- Documentar API actual: `sendEmail(Properties props, EmailData data)`.
- Anadir ejemplo seguro con variables de entorno.

### Medio: perfil de calidad incompleto

Archivo: `pom.xml`

Lineas relevantes:

- Lineas 148-166 definen perfil `quality` con SpotBugs y Checkstyle.

Observaciones:

- No hay configuracion Checkstyle propia.
- Se eliminaron recursos previos de SpotBugs en `src/main/resources`.
- No se pudo ejecutar por ausencia de Maven.

Riesgo:

- Calidad no reproducible o dependiente de defaults.
- Falsos positivos o reglas insuficientes.

Accion recomendada:

- Definir reglas Checkstyle del proyecto.
- Recuperar o reubicar filtros SpotBugs si eran necesarios.
- Ejecutar calidad en CI.

### Bajo: dependencias de test declaradas solo como propiedades

Archivo: `pom.xml`

Lineas relevantes:

- Lineas 44-45 declaran versiones de JUnit y AssertJ.
- No existen dependencias correspondientes en `dependencies`.

Riesgo:

- Configuracion incompleta.
- Confusion para mantenimiento.

Accion recomendada:

- Anadir dependencias o eliminar propiedades no usadas.

### Bajo: constantes duplicadas o documentacion imprecisa

Archivo: `src/main/java/local/jarios/email/common/util/Constantes.java`

Observaciones:

- `SMTP_STARTTLS` y `SMTP_STARTTLS_ENABLE` contienen la misma clave.
- Varias constantes SMTP tienen comentario repetido sobre "puerto" aunque no representan puertos.

Riesgo:

- Mantenimiento confuso.

Accion recomendada:

- Eliminar duplicados.
- Corregir JavaDoc.

### Bajo: imports y clases con uso dudoso

Ejemplos:

- `FinalDelProgramaHelper.java` importa `Mensajes` pero no lo usa.
- `TipoFinalEjecucion` tiene `@Slf4j` aunque no registra logs.
- `TextHelper` importa `EmailException` solo para JavaDoc.

Riesgo:

- Ruido y menor limpieza del codigo.

Accion recomendada:

- Activar Checkstyle/Spotless/PMD o reglas de IDE para imports no usados.

## 7. Seguridad

### Riesgos principales

1. Credencial SMTP en claro.
2. Direcciones corporativas hardcodeadas.
3. HTML construido con concatenacion sin escape.
4. Stacktrace completo enviado por email.
5. Logs con usuario SMTP y host/puerto.

### Recomendaciones

- Rotar secretos expuestos.
- Usar variables de entorno o gestor de secretos.
- No versionar demos con credenciales reales.
- Escapar HTML.
- Limitar stacktrace en notificaciones o hacerlo configurable.
- Evitar datos personales/corporativos hardcodeados en libreria.

## 8. Build, dependencias y compatibilidad

### Estado Maven

`pom.xml` esta orientado a Java 21 y Maven >= 3.6.3. Esto es coherente con el uso de:

- `record`
- text blocks
- pattern matching en `switch`

Pero el entorno actual no cumple:

- Java local: 8
- Maven: no disponible

### Dependencias principales

- `org.slf4j:slf4j-api`
- `org.projectlombok:lombok` como `provided`
- `com.sun.mail:jakarta.mail`
- `ch.qos.logback:logback-classic` solo test

### Observacion

La libreria usa anotaciones Lombok `@Slf4j`. Al consumidor no se le exige Lombok en runtime, pero el build si lo necesita en compilacion. Esto esta correctamente marcado como `provided`.

## 9. API publica

API actual:

- `EmailService.sendEmail(Properties props, EmailData data)`
- `EmailSender.send(Session session, Message message)`
- `EmailData(String from, String to, String subject, String body)`

Riesgos de API:

- Exponer `Properties` de Jakarta Mail directamente es simple, pero deja toda validacion semantica al llamador.
- `EmailData.to` como `String` separado por comas es flexible, pero menos seguro que una lista tipada.
- `EmailException` extiende `RuntimeException`; la firma declara `throws`, pero no obliga a captura real.

Recomendaciones:

- Mantener API simple si el objetivo es una libreria interna.
- Considerar `EmailConfig` tipado para SMTP si se busca robustez.
- Considerar `List<String>` para destinatarios.
- Documentar claramente que `EmailException` es no comprobada.

## 10. Pruebas recomendadas

Prioridad alta:

- `EmailRequestValidatorTest`
  - props nulas
  - data nula
  - from/to/subject/body vacios
  - body null
  - email from invalido
  - multiples destinatarios con uno invalido
  - falta de propiedad SMTP obligatoria

- `EmailServiceImplTest`
  - crea `MimeMessage` con from/to/subject/body esperados
  - traduce errores de `MessagingException` a `EmailException`
  - no loguea password
  - no relanza cuando se trata de notificacion de error, si se mantiene ese comportamiento

- `EmailHelperTest`
  - construccion de HTML completo
  - escape de valores dinamicos cuando se implemente
  - cuerpo de excepcion con multiples lineas

- `ErrorNotificationServiceTest`
  - usa remitente/destinatario configurables
  - no propaga excepcion si ese es el contrato
  - devuelve resultado observable si se cambia el contrato

## 11. CI/CD recomendado

Crear workflow de GitHub Actions con:

- JDK 21
- cache Maven
- `mvn -B test`
- `mvn -B -Pquality checkstyle:check spotbugs:check`
- escaneo basico de secretos

Anadir Maven Wrapper:

- `mvn -N wrapper:wrapper`

Una vez creado, los comandos recomendados serian:

- `./mvnw test`
- `./mvnw -Pquality checkstyle:check spotbugs:check`
- `./mvnw versions:display-dependency-updates`

## 12. Plan de accion propuesto

### Fase 1: contencion inmediata

1. Revocar/rotar la credencial SMTP expuesta.
2. Eliminar credenciales y direcciones personales/corporativas de demos.
3. Anadir `.env.example` o documentacion de variables sin secretos reales.
4. Revisar historico Git si la credencial fue publicada.

### Fase 2: build reproducible

1. Instalar JDK 21 y Maven o anadir Maven Wrapper.
2. Ejecutar `mvn test`.
3. Ejecutar perfil `quality`.
4. Corregir fallos de compilacion/calidad.

### Fase 3: tests y contratos

1. Anadir JUnit Jupiter.
2. Convertir la demo en test o moverla a documentacion/manual.
3. Cubrir validadores y servicio SMTP con dobles de test.
4. Evitar pruebas que envien correos reales por defecto.

### Fase 4: limpieza de libreria

1. Retirar `System.exit` de `src/main`.
2. Hacer configurable `ErrorNotificationService`.
3. Escapar HTML dinamico.
4. Hacer null-safe `TextHelper.recortar`.
5. Actualizar README.

## 13. Checklist de aceptacion para publicar

- [ ] No hay secretos en codigo, tests, README ni historico publicado.
- [ ] JDK 21 y Maven Wrapper disponibles.
- [ ] `mvn test` pasa.
- [ ] `mvn -Pquality checkstyle:check spotbugs:check` pasa.
- [ ] README refleja clases y ejemplos actuales.
- [ ] La demo no envia correos reales sin configuracion explicita.
- [ ] No hay `System.exit` en API de libreria.
- [ ] HTML dinamico se escapa.
- [ ] Destinatarios/remitentes de notificaciones son configurables.

## 14. Conclusion

La base del proyecto es aprovechable y el dominio esta bien acotado. Antes de evolucionarlo conviene cerrar los riesgos de seguridad y reproducibilidad: secretos, entorno Java/Maven, tests inexistentes y documentacion desactualizada. Con esas correcciones, el proyecto puede quedar como una libreria interna simple y mantenible para envio SMTP.
