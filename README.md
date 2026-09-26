# Biblioteca de Envío de Correo Electrónico

Biblioteca Java para enviar correos HTML mediante SMTP, validar los datos del mensaje y construir notificaciones de error. La aplicación consumidora proporciona la configuración SMTP y la implementación de logging.

**Artefacto Maven:** `local.jarios:email-helper:6.2.0`

**Repositorio:** [contratacionmalaga/email_helper](https://github.com/contratacionmalaga/email_helper)

## Información de la versión

| Característica | Configuración |
|---|---|
| Versión preparada para publicación | **6.2.0** |
| Tipo de artefacto | Biblioteca `jar` |
| Parent Maven | `local.jarios:jarios-parent:1.0.15` |
| Java de referencia y compilación | **21** |
| Maven mínimo y distribuido mediante Wrapper | **3.9.16** |
| Maven Wrapper | **3.3.4** |
| Contenido de los mensajes | HTML, UTF-8 |
| Publicación | GitHub Packages |
| Revisión de dependencias y herramientas | 26 de septiembre de 2026 |

El POM ya declara **6.2.0**. Los ejemplos corresponden a esta versión; su disponibilidad en GitHub Packages depende de la publicación del tag `v6.2.0`.

Esta versión adopta el parent 1.0.15 y actualiza herramientas de compilación, pruebas y calidad. Mantiene la API pública y el baseline Java 21. Las versiones de dependencias y herramientas se heredan del parent, sin sobrescrituras locales.

## Alcance y funcionamiento

`EmailServiceImpl` valida la solicitud, crea una sesión Jakarta Mail con autenticación, construye un mensaje HTML y delega su envío en `EmailSender`. `EmailSenderImpl` utiliza el transporte SMTP de Jakarta Mail.

- Un remitente y uno o varios destinatarios separados por comas.
- Asunto y cuerpo obligatorios.
- Helpers para generar HTML con escape de los valores dinámicos.
- Notificaciones de error con contexto y traza de la excepción.
- Errores de validación y envío comunicados mediante `EmailException`.

Es una biblioteca para integrar en aplicaciones Java. La API `EmailData` no incorpora adjuntos, CC ni CCO. El envío es síncrono y no incluye colas ni reintentos automáticos.

## Requisitos e integración Maven

Se requiere Java 21 o superior, acceso a un servidor SMTP y sus credenciales. Para compilar se necesita un JDK y Maven 3.9.16 o superior; el Wrapper incluido proporciona Maven.

Declare la dependencia en el POM de la aplicación consumidora:

```xml
<dependency>
  <groupId>local.jarios</groupId>
  <artifactId>email-helper</artifactId>
  <version>6.2.0</version>
</dependency>
```

Puede omitir la versión si su parent ya gestiona la versión deseada. En particular, `jarios-parent:1.0.15` todavía gestiona `email-helper:6.1.4`: usar ese parent no selecciona automáticamente la 6.2.0.

### Acceso a GitHub Packages

Incorpore estos repositorios al POM consumidor o a un perfil activo de Maven:

```xml
<repositories>
  <repository>
    <id>github-email-helper</id>
    <url>https://maven.pkg.github.com/contratacionmalaga/email_helper</url>
  </repository>
  <repository>
    <id>github-jarios-parent</id>
    <url>https://maven.pkg.github.com/contratacionmalaga/jarios-parent</url>
  </repository>
</repositories>
```

Añada los servidores a su `settings.xml`, conservando la configuración existente:

```xml
<servers>
  <server>
    <id>github-email-helper</id>
    <username>${env.GITHUB_ACTOR}</username>
    <password>${env.PACKAGES_TOKEN}</password>
  </server>
  <server>
    <id>github-jarios-parent</id>
    <username>${env.GITHUB_ACTOR}</username>
    <password>${env.PACKAGES_TOKEN}</password>
  </server>
</servers>
```

Defina `GITHUB_ACTOR` y `PACKAGES_TOKEN` con un usuario y una credencial con acceso de lectura a los paquetes. Los identificadores de servidor deben coincidir con los de los repositorios.

## Uso básico

Este ejemplo obtiene las credenciales de variables de entorno. La biblioteca no las lee automáticamente.

```java
import java.util.Properties;
import local.jarios.email.api.EmailSenderImpl;
import local.jarios.email.api.EmailService;
import local.jarios.email.api.EmailServiceImpl;
import local.jarios.email.model.EmailData;

public final class EjemploCorreo {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.setProperty("mail.smtp.host", "smtp.example.com");
    props.setProperty("mail.smtp.port", "587");
    props.setProperty("mail.smtp.auth", "true");
    props.setProperty("mail.smtp.starttls.enable", "true");
    props.setProperty("mail.smtp.starttls.required", "true");
    props.setProperty("mail.smtp.user", variableObligatoria("SMTP_USER"));
    props.setProperty("mail.smtp.password", variableObligatoria("SMTP_PASSWORD"));
    props.setProperty("mail.smtp.connectiontimeout", "10000");
    props.setProperty("mail.smtp.timeout", "10000");
    props.setProperty("mail.smtp.writetimeout", "10000");

    EmailData mensaje = new EmailData(
        "from@example.com",
        "one@example.com,two@example.com",
        "Resultado del proceso",
        "<p>El proceso ha terminado correctamente.</p>");

    EmailService servicio = new EmailServiceImpl(new EmailSenderImpl());
    servicio.sendEmail(props, mensaje);
  }

  private static String variableObligatoria(String nombre) {
    String valor = System.getenv(nombre);
    if (valor == null || valor.isBlank()) {
      throw new IllegalStateException("Falta configurar " + nombre);
    }
    return valor;
  }
}
```

Adapte el host, puerto y remitente a su servidor. Los tiempos de espera del ejemplo están expresados en milisegundos; la biblioteca no establece valores propios para ellos.

## Configuración SMTP y validación

Las constantes siguientes pertenecen a `local.jarios.email.common.util.Constantes`:

| Constante | Clave | Ejemplo |
|---|---|---|
| `SMTP_HOST` | `mail.smtp.host` | `smtp.example.com` |
| `SMTP_PORT` | `mail.smtp.port` | `587` |
| `SMTP_AUTH` | `mail.smtp.auth` | `true` |
| `SMTP_STARTTLS` | `mail.smtp.starttls.enable` | `true` |
| `SMTP_USER` | `mail.smtp.user` | Usuario SMTP |
| `SMTP_PASSWORD` | `mail.smtp.password` | Contraseña SMTP |

El validador exige que las seis propiedades existan y no estén en blanco, incluso si `mail.smtp.auth` vale `false`. Comprueba presencia, pero no valida el rango del puerto ni los valores booleanos. Las propiedades adicionales se entregan a Jakarta Mail.

También rechaza propiedades o datos del mensaje nulos, campos vacíos y direcciones que no superen la validación de Jakarta Mail. Esta comprobación no garantiza que el buzón exista ni que el servidor acepte el mensaje.

## API pública

| Tipo o método | Finalidad |
|---|---|
| `EmailData(String from, String to, String subject, String body)` | Record con los datos del correo. |
| `new EmailServiceImpl(EmailSender sender)` | Servicio con transporte inyectable. |
| `EmailService.sendEmail(Properties props, EmailData data)` | Valida, construye y envía el mensaje. |
| `EmailSender.send(Session session, Message message)` | Contrato de transporte, sustituible en pruebas. |
| `new EmailSenderImpl()` | Transporte basado en `Transport.send(message)`. |
| `ErrorNotificationService.notifyError(Throwable ex, String contexto, Properties props)` | Intenta notificar un error y registra el fallo de notificación. |
| `ErrorNotificationService.notifyErrorAndReturnResult(...)` | Mismo envío; devuelve `true` si se completa y `false` si falla. |

`EmailException` extiende `RuntimeException`. El servicio conserva las excepciones de dominio y envuelve los fallos inesperados. La aplicación decide cómo informar al usuario o reintentar.

### Notificación de errores

Dentro del tratamiento de una excepción, con el servicio y las propiedades SMTP ya configurados:

```java
import local.jarios.email.api.ErrorNotificationService;

ErrorNotificationService notificador = new ErrorNotificationService(
    servicio, "errors@example.com", "ops@example.com");

boolean enviado = notificador.notifyErrorAndReturnResult(
    excepcion, "Proceso de importación", props);
```

El notificador captura las excepciones del intento de notificación. Si el envío falla, registra la excepción y devuelve `false`; no sustituye el manejo del error original de la aplicación.

`ErrorEmailBuilder.build(ex, contexto, from, to)` incluye fecha, clase y mensaje de la excepción y una traza limitada por defecto a 6.000 caracteres, más una marca si se trunca. La sobrecarga `build(ex, contexto, from, to, subject, maxStackTraceLength)` permite personalizar asunto y límite.

### Construcción de HTML

| Método de `EmailHelper` | Uso |
|---|---|
| `getCuerpoEstadistica(String[][] estadistica)` | Cuerpo HTML con estadísticas. |
| `getCuerpoExcepcion(String[] excepcion)` | Cuerpo HTML con información de una excepción. |
| `getAsunto(String appName, String appVersion, String equipo, boolean success)` | Asunto con aplicación, versión, equipo, resultado y fecha. |

Los helpers escapan los valores dinámicos que insertan. El cuerpo entregado directamente a `EmailData` se trata como HTML y no se sanea automáticamente.

## Logging y tratamiento de datos

La biblioteca utiliza SLF4J y no impone un backend de logging en producción. La aplicación consumidora debe aportar uno compatible; Logback solo se usa en las pruebas.

Los errores de envío registran host, puerto y usuario SMTP. Los correos de error contienen mensajes de excepción y trazas, y el fallo de una notificación también registra su excepción. No existe un filtro general de secretos: revise qué información incluyen las excepciones y a quién se remiten antes de utilizar estas notificaciones con datos sensibles.

Mantenga las credenciales en la configuración protegida de la aplicación, fuera del código y del repositorio.

## Dependencias

Versiones gestionadas por `jarios-parent:1.0.15`:

| Dependencia | Versión | Ámbito | Finalidad |
|---|---|---|---|
| `org.slf4j:slf4j-api` | 2.0.20 | `compile` | API de logging. |
| `com.sun.mail:jakarta.mail` | 2.0.2 | `compile` | API y transporte de correo. |
| `org.projectlombok:lombok` | 1.18.48 | `provided` | Generación de código durante la compilación. |
| `ch.qos.logback:logback-classic` | 1.6.4 | `test` | Logging de las pruebas. |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | `test` | Pruebas unitarias. |
| `org.assertj:assertj-core` | 3.27.7 | `test` | Aserciones. |

La revisión no detectó versiones estables posteriores de estas coordenadas. Se excluyen las versiones preliminares de SLF4J y AssertJ. La evolución de Jakarta Mail continúa en [Eclipse Angus](https://eclipse-ee4j.github.io/angus-mail/); cambiar de implementación requiere una migración específica y no forma parte de esta actualización.

## Construcción y verificación

Desde la raíz del repositorio y con `JAVA_HOME` apuntando al JDK:

| Operación | Windows | Linux y macOS |
|---|---|---|
| Comprobar herramientas | `.\mvnw.cmd -version` | `./mvnw -version` |
| Ejecutar pruebas | `.\mvnw.cmd test` | `./mvnw test` |
| Construir y verificar | `.\mvnw.cmd clean verify` | `./mvnw clean verify` |
| Comprobar calidad | `.\mvnw.cmd -Pquality verify` | `./mvnw -Pquality verify` |
| Instalar localmente | `.\mvnw.cmd install` | `./mvnw install` |

En Linux y macOS puede ser necesario ejecutar `chmod +x mvnw`. El primer uso puede descargar Maven y las dependencias.

El POM referencia `../jarios-parent/pom.xml`. Si el proyecto vecino declara otra versión, Maven resuelve **1.0.15** desde el repositorio local o remoto; no adopta automáticamente el parent vecino.

La construcción genera `target/email-helper-6.2.0.jar`. El POM actual no vincula la generación de JAR de fuentes ni de Javadoc al ciclo de construcción.

Las 19 pruebas actuales cubren validación, construcción del mensaje, delegación del envío, propagación de errores, notificaciones y helpers HTML. Utilizan transportes simulados: no verifican la conectividad ni la entrega real en un servidor SMTP.

### Herramientas de construcción y calidad

| Herramienta | Versión | Procedencia |
|---|---|---|
| Maven Enforcer Plugin | 3.6.3 | Parent |
| Maven Compiler Plugin | 3.16.0 | Parent |
| Maven Surefire Plugin | 3.6.0 | Parent |
| Maven Clean Plugin | 3.5.0 | Parent |
| Maven JAR Plugin | 3.5.1 | Parent |
| Versions Maven Plugin | 2.22.0 | Parent |
| Spotless Maven Plugin | 3.10.3 | Parent |
| Google Java Format | 1.36.1 | Parent |
| Maven Checkstyle Plugin | 3.6.0 | Parent |
| Checkstyle | 14.1.0 | Parent |
| SpotBugs Maven Plugin | 4.10.4.1 | Parent |
| OWASP Dependency-Check | 13.0.0 | Parent |

El perfil `quality` ejecuta Spotless, Checkstyle y SpotBugs en `verify`. Checkstyle utiliza las reglas locales de [`src/checkstyle/checkstyle.xml`](src/checkstyle/checkstyle.xml). OWASP se ejecuta por separado, mediante `org.owasp:dependency-check-maven:check`, y utiliza `NVD_API_KEY` si está configurada.

## Automatización y publicación

| Workflow | Activación | Función |
|---|---|---|
| [`CI`](.github/workflows/ci.yml) | Push y PR sobre `main`; manual | `clean verify` con JDK 21. |
| [`Quality`](.github/workflows/quality.yml) | Push y PR sobre `main`; manual | `-Pquality verify` con JDK 21. |
| [`Dependency Check`](.github/workflows/dependency-check.yml) | Lunes a las 04:10 UTC; manual | Análisis OWASP y conservación del informe. |
| [`Release Package`](.github/workflows/release-package.yml) | Tags `v*`; manual con `release_tag` | Verificación, publicación Maven y GitHub Release. |

Para publicar la 6.2.0, el tag **`v6.2.0` debe apuntar al commit cuyo POM declara `6.2.0`**. El workflow verifica esa coincidencia; no cambia la versión del POM. También puede ejecutarse manualmente indicando un tag existente en `release_tag`.

La publicación ejecuta `clean verify`, después `deploy`, crea la GitHub Release y adjunta los JAR de `target/`. Requiere permisos de escritura sobre contenido y paquetes. La lectura de paquetes usa `PACKAGES_TOKEN` o el `GITHUB_TOKEN` configurado como alternativa, que debe tener acceso a los paquetes necesarios.

Dependabot revisa Maven semanalmente mediante [su configuración](.github/dependabot.yml). No hay actualización automática de GitHub Actions ni fusión automática configuradas. El acceso de Dependabot a repositorios privados debe configurarse por separado de las credenciales de los workflows.

## Resolución de problemas

| Síntoma | Comprobación |
|---|---|
| No se resuelve el parent o la biblioteca | Versión publicada, repositorios y credenciales Maven. |
| Error 401 o 403 al descargar paquetes | Permisos del token y coincidencia entre identificadores de repositorio y servidor. |
| Maven rechaza Java o su propia versión | `JAVA_HOME` y salida de `mvnw -version`. |
| Falta una propiedad SMTP | Las seis propiedades obligatorias deben tener un valor no vacío. |
| Falla autenticación, conexión o TLS | Host, puerto, credenciales y requisitos del servidor; causa de `EmailException`. |
| El envío permanece bloqueado | Configurar tiempos de espera SMTP en las propiedades. |
| No aparecen logs | Backend SLF4J y niveles de logging de la aplicación. |
| La notificación devuelve `false` | Consultar el log y conservar el diagnóstico del error original. |
| Falla la publicación | Coincidencia tag/POM, acceso a paquetes y permisos del workflow. |

## Documentación y mantenimiento

- [Revisión de actualizaciones y validación de 6.2.0](docs/releases/6.2.0.md).
- [Auditoría técnica de agosto de 2026](docs/auditorias/2026-08-12-auditoria-viva-proyecto.md).
- [Auditoría histórica de mayo de 2026](doc/auditoria/2026_05_09_auditoria_proyecto.md).

Mantenga este README sincronizado con el POM, la API y los workflows. Los documentos históricos describen el estado de su fecha y no sustituyen el proceso de publicación actual.

## Licencia

El repositorio no incluye un archivo de licencia propio ni una declaración de licencia en el POM o el parent revisado. Las dependencias conservan sus respectivas licencias.
