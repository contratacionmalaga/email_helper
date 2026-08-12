# Auditoria viva del proyecto email-helper

Fecha de creacion: 2026-08-12  
Ultima revision: 2026-08-12  
Proyecto: `email-helper`  
Version declarada en `pom.xml`: `6.1.0`  
Tipo: libreria Java/Maven para envio de correos SMTP con Jakarta Mail  
Estado del documento: vivo, debe actualizarse al cerrar cada hito

## Como mantener viva esta auditoria

Cada vez que se aplique una mejora:

1. Marcar el hito correspondiente en la seccion "Hitos vivos".
2. Mover o actualizar los hallazgos afectados en "Hallazgos priorizados".
3. Anadir una entrada en "Bitacora de actualizaciones".
4. Reejecutar las verificaciones minimas y registrar el resultado.

Verificaciones minimas recomendadas:

```powershell
.\mvnw.cmd test
.\mvnw.cmd -Pquality checkstyle:check spotbugs:check
.\mvnw.cmd versions:display-dependency-updates versions:display-plugin-updates versions:display-property-updates
```

## Resumen ejecutivo

El proyecto ha mejorado de forma clara respecto a la auditoria historica de `doc/auditoria/2026_05_09_auditoria_proyecto.md`. Actualmente tiene Maven Wrapper, tests unitarios reales, perfil de calidad con Checkstyle y SpotBugs, workflows de GitHub Actions, Dependabot y una API bastante acotada para envio SMTP.

Estado verificado el 2026-08-12:

- `.\mvnw.cmd test`: correcto, 19 tests, 0 fallos.
- `.\mvnw.cmd -Pquality checkstyle:check spotbugs:check`: correcto, 0 violaciones Checkstyle y 0 hallazgos SpotBugs.
- Java efectivo: Oracle JDK `21.0.9`.
- Maven Wrapper efectivo: Apache Maven `3.9.16`.
- No se han encontrado secretos reales en el codigo actual mediante busqueda textual basica.

Los principales puntos pendientes ya no son de bloqueo inmediato. Tras las mejoras del 2026-08-12 queda cerrada la estrategia Java: mantener baseline Java 21, validar CI con JDK 21 y JDK 25, y reservar la migracion real a Java 25 para una version mayor cuando los consumidores esten preparados. Siguen abiertos la evaluacion de actualizaciones mayores o inestables y la trazabilidad del secreto historico mencionado en la auditoria antigua.

## Fuentes y comprobaciones usadas

- Codigo local del repositorio en `C:\java\desarrollo\email-helper`.
- `mvn test`, `mvn -Pquality checkstyle:check spotbugs:check`, `mvn dependency:tree`.
- `versions-maven-plugin` para dependencias, plugins y propiedades.
- Apache Maven Release History: https://maven.apache.org/docs/history.html
- Oracle Java Downloads: https://www.oracle.com/java/technologies/downloads/
- Oracle Java SE Support Roadmap: https://www.oracle.com/java/technologies/java-se-support-roadmap.html

## Estado del repositorio

No habia cambios pendientes en Git antes de crear esta auditoria.

Archivos/directorios relevantes versionados:

- `pom.xml`
- `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties`
- `.github/workflows/maven-ci.yml`
- `.github/workflows/maven-release.yml`
- `.github/workflows/maven-publish-package.yml`
- `.github/dependabot.yml`
- `src/main/java/local/jarios/email/**`
- `src/test/java/local/jarios/email/**`
- `src/checkstyle/checkstyle.xml`

Artefactos locales ignorados correctamente por `.gitignore`:

- `target/`
- `.idea/`
- `logs/`
- `config/`

## Actualizaciones disponibles

Resultado local de `versions-maven-plugin` el 2026-08-12:

| Componente | Version actual | Version detectada | Recomendacion |
| --- | ---: | ---: | --- |
| `org.projectlombok:lombok` | `1.18.46` | `1.18.46` | Cerrado el 2026-08-12. |
| `ch.qos.logback:logback-classic` | `1.5.32` | `1.6.2` | Actualizar en rama separada y validar tests/logging. Es solo test. |
| `org.junit.jupiter:junit-jupiter` | `5.10.5` | `6.1.3` | Evaluar con cuidado. Es salto mayor y puede requerir ajustes del ecosistema JUnit Platform/Surefire. |
| `org.assertj:assertj-core` | `3.26.3` | `4.0.0-M1` | No actualizar todavia salvo necesidad expresa: es milestone, no estable final. |
| `org.slf4j:slf4j-api` | `2.0.17` | `2.1.0-alpha1` | No actualizar todavia: es alpha y afecta API publica transitiva. |
| `com.sun.mail:jakarta.mail` | `2.0.2` | `2.0.2` | Sin actualizacion detectada por Maven. |
| `versions-maven-plugin` | `2.21.0` | `2.21.0` | Sin actualizacion detectada. |
| `maven-enforcer-plugin` | `3.6.3` | `3.6.3` | Cerrado el 2026-08-12. |
| `spotbugs-maven-plugin` | `4.10.3.0` | `4.10.3.0` | Cerrado el 2026-08-12. |
| `maven-surefire-plugin` | `3.5.5` | `3.6.0-M1` | No actualizar todavia salvo necesidad: milestone. |
| `maven-compiler-plugin` | `3.15.0` | `4.0.0-beta-4` | No actualizar mientras el proyecto siga en Maven 3.x. Requiere Maven 4 rc/beta segun el informe. |

Java y Maven:

| Componente | Proyecto/local | Estado actual consultado | Recomendacion |
| --- | ---: | ---: | --- |
| Java de compilacion | `21` | Oracle indica JDK 26 como ultima version feature y JDK 25 como LTS mas reciente; JDK 21 queda como LTS anterior. | Cerrado el 2026-08-12: mantener `maven.compiler.release=21` y validar CI con JDK 21 y JDK 25. |
| JDK local | `21.0.9` | Oracle publica `21.0.12` para la linea JDK 21. | Actualizar el JDK local/CI a un patch reciente de Java 21 o migrar de forma planificada a 25 LTS. |
| Maven Wrapper | `3.9.16` | Apache lista Maven `3.9.16` como estable y `3.10.0-rc-1` como release candidate. | Cerrado el 2026-08-12; evitar `3.10.0-rc-1` para produccion. |
| Maven minimo en POM | `3.6.3` | Los plugins actuales no exigen mas que `3.6.3`. | Mantener si se busca compatibilidad; subir a `3.9.x` si se quiere alinear con el wrapper. |

## Hallazgos priorizados

### Cerrado 2026-08-12: cobertura de `EmailServiceImpl`

Referencia: `src/main/java/local/jarios/email/api/EmailServiceImpl.java:50`

`EmailServiceImpl` ya tiene tests directos con un `EmailSender` capturador. La cobertura valida construccion del `MimeMessage`, content-type HTML, destinatarios, propagacion de `EmailException`, wrapping de `RuntimeException` y props nulas.

Riesgo:

- Cambios en cabeceras, recipients, subject, content-type o wrapping de excepciones podrian romper consumidores sin deteccion.
- El contrato real de `sendEmail(Properties, EmailData)` queda parcialmente protegido por tests indirectos.

Accion recomendada:

- Anadir `EmailServiceImplTest` con un `EmailSender` fake/capturador.
- Verificar `from`, `to`, `subject`, `text/html; charset=utf-8` y propagacion de `EmailException`.
- Cubrir errores inesperados del sender.

### Cerrado 2026-08-12: estrategia Java 21 con validacion JDK 25

Referencia: `pom.xml:35`

El proyecto mantiene `maven.compiler.release=21` para no romper consumidores actuales. A fecha 2026-08-12, Java 25 es la LTS mas reciente y Java 26 es la ultima version feature segun Oracle; por eso el CI valida tambien JDK 25 como preparacion para una migracion futura.

Riesgo:

- Cerrado: existe decision explicita de mantener baseline Java 21.
- Riesgo residual: la migracion a baseline Java 25 debe coordinarse con los consumidores y publicarse como version mayor.

Accion recomendada:

- Cerrado: CI con matriz JDK 21 y JDK 25.
- Mantener `maven.compiler.release=21` en la serie `6.x`.
- Migrar a `maven.compiler.release=25` en una futura version mayor, por ejemplo `7.0.0`, cuando los consumidores esten listos.

### Parcialmente cerrado 2026-08-12: actualizaciones disponibles con mezcla de estables, milestones y alpha

Referencia: `pom.xml:39`, `pom.xml:40`, `pom.xml:44`, `pom.xml:45`, `pom.xml:48`, `pom.xml:54`

Las actualizaciones estables de bajo riesgo ya se aplicaron: Lombok, Enforcer, SpotBugs y Maven Wrapper. Siguen apareciendo actualizaciones que no son candidatas sanas para aplicar directamente. JUnit 6 debe evaluarse como migracion mayor. AssertJ `4.0.0-M1`, Surefire `3.6.0-M1` y SLF4J `2.1.0-alpha1` deberian evitarse salvo necesidad.

Riesgo:

- Adoptar versiones no finales puede introducir inestabilidad en una libreria pequena.
- Saltos mayores pueden exigir cambios de tests o del runtime de consumidores.

Accion recomendada:

- Crear una rama de mantenimiento de dependencias.
- Aplicar primero solo versiones estables y de bajo riesgo.
- Reejecutar `test` y `-Pquality`.

### Cerrado 2026-08-12: `ErrorNotificationService` expone resultado observable

Referencia: `src/main/java/local/jarios/email/api/ErrorNotificationService.java:27`, `src/main/java/local/jarios/email/api/ErrorNotificationService.java:44`

El servicio mantiene `notifyError(...)` como API compatible y anade `notifyErrorAndReturnResult(...)`, que devuelve `true` si la notificacion se envia y `false` si falla sin relanzar.

Riesgo:

- Operacion no observable en entornos productivos.
- Falsa sensacion de que la alerta fue entregada.

Accion recomendada:

- Mantener la no propagacion si es parte del contrato.
- Cambiar el metodo para devolver `boolean` o un resultado tipado.
- Alternativamente, documentar de forma explicita que el unico canal de observabilidad es el log.

### Cerrado 2026-08-12: stacktrace de error limitado y configurable

Referencia: `src/main/java/local/jarios/email/helper/ErrorEmailBuilder.java:39`, `src/main/java/local/jarios/email/helper/ErrorEmailBuilder.java:45`

El cuerpo de error limita el stacktrace por defecto y permite configurar asunto y longitud maxima desde `ErrorEmailBuilder.build(...)`.

Riesgo:

- Exposicion de informacion interna en buzones.
- Ruido excesivo en correos de error largos.

Accion recomendada:

- Hacer configurable el nivel de detalle.
- Limitar longitud del stacktrace.
- Considerar enviar identificador/correlacion y dejar detalle completo en logs.

### Cerrado 2026-08-12: README actualizado a la version actual

Referencia: `README.md:9`, `README.md:91`

El README ya declara `6.1.0` y enlaza la auditoria viva actual en `docs/auditorias/2026-08-12-auditoria-viva-proyecto.md`.

Riesgo:

- Confusion al consumir la libreria.
- Publicacion o soporte sobre version equivocada.

Accion recomendada:

- Cerrado: README actualizado a `6.1.0`.
- Considerar no mantener una version manual en README o derivarla del release.

### Cerrado 2026-08-12: el workflow de CI ejecuta explicitamente el perfil `quality`

Referencia: `.github/workflows/maven-ci.yml:27`, `.github/workflows/maven-ci.yml:30`

El CI ejecuta `./mvnw -B clean install`, el perfil `-Pquality` con Checkstyle/SpotBugs y OWASP Dependency Check.

Riesgo:

- La calidad local puede pasar, pero no quedar bloqueada en PR si nadie ejecuta el perfil manualmente.
- SpotBugs/Checkstyle pueden degradarse sin alerta en CI.

Accion recomendada:

- Cerrado con el paso `Run quality checks` en `.github/workflows/maven-ci.yml`.
- Dependency Check se mantiene como paso separado.

### Cerrado 2026-08-12: constantes SMTP duplicadas y JavaDoc impreciso

Referencia: `src/main/java/local/jarios/email/common/util/Constantes.java:38`, `src/main/java/local/jarios/email/common/util/Constantes.java:75`

Se elimino el duplicado `SMTP_STARTTLS_ENABLE`, la demo usa `SMTP_STARTTLS` y se corrigio el JavaDoc de propiedades SMTP que no representan puertos.

Riesgo:

- Ruido de mantenimiento.
- Uso inconsistente entre demo y validador.

Accion recomendada:

- Conservar una sola constante para STARTTLS.
- Corregir JavaDoc de `SMTP_CHECKSERVERIDENTITY`, `SMTP_PROTOCOLS`, `SMTP_TRUST` y similares.

### Cerrado 2026-08-12: `ErrorEmailBuilder` permite asunto configurable

Referencia: `src/main/java/local/jarios/email/helper/ErrorEmailBuilder.java:31`

El asunto por defecto ahora es ASCII y `ErrorEmailBuilder` permite pasar un asunto personalizado mediante el overload configurable.

Riesgo:

- Menor capacidad de personalizacion por consumidor.
- Potenciales problemas en integraciones antiguas.

Accion recomendada:

- Permitir configurar prefijo/asunto.
- Mantener un asunto ASCII por defecto si se prioriza maxima compatibilidad.

### Bajo: documentacion historica desfasada convive con auditoria nueva

Referencia: `doc/auditoria/2026_05_09_auditoria_proyecto.md`

La auditoria historica contiene hallazgos ya corregidos o parcialmente corregidos. Es util como trazabilidad, pero puede inducir a error si se lee como estado actual.

Riesgo:

- Duplicidad de fuentes de verdad.

Accion recomendada:

- Mantenerla como historica.
- Enlazar desde README a esta auditoria viva como estado actual.
- Opcionalmente anadir una nota al inicio de la auditoria antigua.

## Fortalezas actuales

- Separacion razonable entre `EmailService`, `EmailSender`, validacion, modelo y helpers.
- `EmailData` como `record`, simple e inmutable.
- `EmailSender` permite dobles de test sin tocar SMTP real.
- La libreria solo usa `slf4j-api` en runtime y no impone backend de logging.
- `EmailRequestValidator` maneja `null` y campos obligatorios con `EmailException`.
- El HTML dinamico principal se escapa mediante `EmailHelper.escapeHtml`.
- Maven Wrapper incluido y operativo.
- CI/CD y Dependabot presentes.
- `.gitignore` cubre artefactos habituales: `target`, `.idea`, `logs`, `config`.

## Hitos vivos

| ID | Estado | Prioridad | Hito | Criterio de cierre |
| --- | --- | --- | --- | --- |
| H01 | Completado | Alta | Anadir tests directos para `EmailServiceImpl` | `EmailServiceImplTest` valida mensaje construido, content-type, recipients, excepciones de dominio, runtime inesperado y props nulas; `test` pasa. |
| H02 | Completado | Alta | Actualizar versiones estables de bajo riesgo | Lombok `1.18.46`, Enforcer `3.6.3`, SpotBugs `4.10.3.0` y Maven Wrapper `3.9.16`; `test` y `-Pquality` pasan. |
| H03 | Completado | Alta | Decidir estrategia Java 21 vs Java 25 LTS | Se mantiene `maven.compiler.release=21`; CI valida JDK 21 y JDK 25; migracion real a Java 25 queda reservada para version mayor futura. |
| H04 | Completado | Media | Incorporar `-Pquality` al CI | `.github/workflows/maven-ci.yml` ejecuta Checkstyle y SpotBugs de forma bloqueante. |
| H05 | Completado | Media | Hacer observable `ErrorNotificationService` | Nuevo `notifyErrorAndReturnResult(...)`; `notifyError(...)` mantiene compatibilidad. |
| H06 | Completado | Media | Limitar o configurar stacktrace en emails de error | `ErrorEmailBuilder` limita por defecto y permite configurar longitud; test agregado. |
| H07 | Completado | Media | Corregir README desactualizado | README declara `6.1.0`, documenta compatibilidad Java y enlaza la auditoria viva actual. |
| H08 | Completado | Baja | Limpiar constantes SMTP duplicadas y JavaDoc | Eliminado `SMTP_STARTTLS_ENABLE`; demo migrada a `SMTP_STARTTLS`; JavaDoc corregido. |
| H09 | Completado | Baja | Revisar asunto configurable en `ErrorEmailBuilder` | Asunto configurable y asunto por defecto ASCII. |
| H10 | Pendiente | Baja | Resolver trazabilidad de secreto historico | Se confirma rotacion/revocacion si el secreto antiguo fue real/publicado; se documenta resultado. |

## Backlog recomendado por fases

### Fase 1: calidad inmediata

- H01: tests de `EmailServiceImpl`.
- H04: `-Pquality` en CI.
- H07: README coherente con `6.1.0`.

### Fase 2: mantenimiento de dependencias

- H02: actualizar versiones estables.
- Revisar JUnit 6 en una rama separada.
- Evitar milestones/alpha en libreria salvo justificacion concreta.

### Fase 3: seguridad y operacion

- H05: resultado observable para notificaciones.
- H06: stacktrace configurable/limitado.
- H10: cierre formal del secreto historico.

### Fase 4: limpieza de API/documentacion

- H03: estrategia Java LTS cerrada; mantener baseline Java 21 y validar JDK 25 en CI.
- H08: constantes y JavaDoc.
- H09: asunto configurable.

## Registro de verificaciones

| Fecha | Comando | Resultado |
| --- | --- | --- |
| 2026-08-12 | `.\mvnw.cmd test` | Correcto. 19 tests, 0 fallos, 0 errores. |
| 2026-08-12 | `.\mvnw.cmd -Pquality checkstyle:check spotbugs:check` | Correcto. 0 violaciones Checkstyle, 0 hallazgos SpotBugs. |
| 2026-08-12 | `.\mvnw.cmd versions:display-dependency-updates versions:display-plugin-updates versions:display-property-updates` | Correcto tras mejoras. Lombok, Enforcer y SpotBugs ya figuran al dia; quedan actualizaciones aplazadas o inestables. |
| 2026-08-12 | `.\mvnw.cmd dependency:tree` | Correcto. Arbol de dependencias revisado. |
| 2026-08-12 | Busqueda textual de secretos | Sin secretos reales detectados en codigo actual; solo nombres de variables y valores ficticios. |
| 2026-08-12 | `.\mvnw.cmd package` | Correcto. Artefacto generado: `target/email_helper-6.1.0.jar`. |

## Bitacora de actualizaciones

| Fecha | Cambio | Resultado |
| --- | --- | --- |
| 2026-08-12 | Creacion de auditoria viva inicial | Documento generado con estado actual, actualizaciones disponibles, hallazgos e hitos. |
| 2026-08-12 | Cierre de H01, H02, H04, H05, H06, H07, H08 y H09 | Tests ampliados a 19, quality integrado en CI, versiones estables actualizadas, README corregido, notificaciones observables, stacktrace limitado/configurable, constantes SMTP limpiadas y asunto de error configurable. |
| 2026-08-12 | Cierre de H03 y generacion de version 6.1.0 | Se mantiene baseline Java 21, se valida CI con JDK 21 y JDK 25, README documenta compatibilidad Java y `pom.xml` sube a `6.1.0`. |
