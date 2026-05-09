# Email Helper

Librería Java para enviar correos electrónicos mediante SMTP usando Jakarta Mail.

Está pensada para aplicaciones Java que necesitan centralizar el envío de correos, validar datos de entrada, construir cuerpos HTML sencillos y delegar el logging en la aplicación consumidora mediante SLF4J.

> Autor: Juan Antonio Ríos  
> Inicio del proyecto: 04/06/2024  
> Versión actual: 5.3.0

## Características

- Envío SMTP con autenticación.
- Validación de remitente, destinatarios y propiedades SMTP obligatorias.
- Soporte de múltiples destinatarios separados por coma.
- Soporte de contenido HTML con escape de valores dinámicos.
- Excepción de dominio: `EmailException`.
- API sencilla basada en `EmailService`.
- Separación entre servicio de envío (`EmailService`) y transporte SMTP (`EmailSender`).
- Logging mediante `slf4j-api`, sin forzar Logback, Log4j2 u otra implementación en producción.
- Notificación de errores configurable mediante `ErrorNotificationService`.

## Requisitos

- Java 21 o superior.
- Maven Wrapper incluido en el proyecto.
- Acceso a un servidor SMTP.

En Windows:

```powershell
.\mvnw.cmd test
```

En Linux/macOS:

```bash
./mvnw test
```

## Estructura

```text
src/
├── main/
│   └── java/
│       └── local/jarios/email/
│           ├── api/
│           │   ├── EmailSender.java
│           │   ├── EmailSenderImpl.java
│           │   ├── EmailService.java
│           │   ├── EmailServiceImpl.java
│           │   └── ErrorNotificationService.java
│           ├── common/util/
│           │   ├── Constantes.java
│           │   └── Mensajes.java
│           ├── enums/
│           │   └── TipoFinalEjecucion.java
│           ├── exception/
│           │   └── EmailException.java
│           ├── helper/
│           │   ├── ComunHelper.java
│           │   ├── EmailHelper.java
│           │   ├── ErrorEmailBuilder.java
│           │   ├── ExceptionUtils.java
│           │   ├── FinalDelProgramaHelper.java
│           │   └── TextHelper.java
│           ├── model/
│           │   └── EmailData.java
│           └── validator/
│               └── EmailRequestValidator.java
└── test/
    ├── java/
    │   └── local/jarios/email/
    │       ├── EmailDemo.java
    │       ├── api/
    │       ├── helper/
    │       └── validator/
    └── resources/
        └── logback-test.xml
```

## Instalación

El proyecto publica artefactos Maven en GitHub Packages:

```xml
<dependency>
    <groupId>local.jarios</groupId>
    <artifactId>email-helper</artifactId>
    <version>5.3.0</version>
</dependency>
```

Si el paquete se consume desde GitHub Packages, la aplicación consumidora debe tener configurado el repositorio y credenciales correspondientes en Maven.

## Uso básico

```java
import local.jarios.email.api.EmailSender;
import local.jarios.email.api.EmailSenderImpl;
import local.jarios.email.api.EmailService;
import local.jarios.email.api.EmailServiceImpl;
import local.jarios.email.model.EmailData;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.SMTP_AUTH;
import static local.jarios.email.common.util.Constantes.SMTP_HOST;
import static local.jarios.email.common.util.Constantes.SMTP_PASSWORD;
import static local.jarios.email.common.util.Constantes.SMTP_PORT;
import static local.jarios.email.common.util.Constantes.SMTP_STARTTLS;
import static local.jarios.email.common.util.Constantes.SMTP_USER;

public class Example {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(SMTP_HOST, "smtp.example.com");
        props.setProperty(SMTP_PORT, "587");
        props.setProperty(SMTP_AUTH, "true");
        props.setProperty(SMTP_STARTTLS, "true");
        props.setProperty(SMTP_USER, System.getenv("SMTP_USER"));
        props.setProperty(SMTP_PASSWORD, System.getenv("SMTP_PASSWORD"));

        EmailData emailData = new EmailData(
            "from@example.com",
            "to@example.com",
            "Asunto del correo",
            "<p>Contenido del correo</p>"
        );

        EmailSender emailSender = new EmailSenderImpl();
        EmailService emailService = new EmailServiceImpl(emailSender);

        emailService.sendEmail(props, emailData);
    }
}
```

No se deben versionar usuarios, contraseñas ni tokens SMTP. Usa variables de entorno, ficheros locales ignorados por Git o un gestor de secretos.

## Propiedades SMTP obligatorias

El validador exige estas propiedades:

| Constante | Clave Jakarta Mail |
| --- | --- |
| `SMTP_USER` | `mail.smtp.user` |
| `SMTP_PASSWORD` | `mail.smtp.password` |
| `SMTP_AUTH` | `mail.smtp.auth` |
| `SMTP_STARTTLS` | `mail.smtp.starttls.enable` |
| `SMTP_HOST` | `mail.smtp.host` |
| `SMTP_PORT` | `mail.smtp.port` |

Si falta alguna propiedad o algún dato del correo es inválido, la librería lanza `EmailException`.

## Modelo de datos

```java
public record EmailData(
    String from,
    String to,
    String subject,
    String body
) {}
```

`to` permite una o varias direcciones separadas por coma:

```java
new EmailData(
    "from@example.com",
    "one@example.com,two@example.com",
    "Asunto",
    "<p>Cuerpo</p>"
);
```

## Notificaciones de error

`ErrorNotificationService` permite construir y enviar un correo de error sin relanzar excepciones si la propia notificación falla.

```java
EmailService emailService = new EmailServiceImpl(new EmailSenderImpl());

ErrorNotificationService notifier = new ErrorNotificationService(
    emailService,
    "errors@example.com",
    "ops@example.com"
);

notifier.notifyError(
    exception,
    "Proceso de importación",
    smtpProperties
);
```

El remitente y destinatario se configuran por constructor. No están fijados en la librería.

## Helpers HTML

`EmailHelper` ofrece utilidades para construir cuerpos HTML sencillos:

- `getCuerpoEstadistica(String[][] estadistica)`
- `getCuerpoExcepcion(String[] excepcion)`
- `getAsunto(String appName, String appVersion, String equipo, boolean success)`

Los valores dinámicos insertados en HTML se escapan para evitar marcado inesperado en el correo.

## Logging

La librería solo depende de `slf4j-api`.

La aplicación consumidora debe aportar su implementación de logging si quiere ver logs:

- Logback
- Log4j2
- cualquier implementación compatible con SLF4J

En tests se usa `logback-classic` con `src/test/resources/logback-test.xml`.

## Build y tests

Compilar y ejecutar tests:

```powershell
.\mvnw.cmd test
```

Instalar localmente:

```powershell
.\mvnw.cmd clean install
```

Ejecutar perfil de calidad:

```powershell
.\mvnw.cmd -Pquality checkstyle:check spotbugs:check
```

Actualmente el perfil de calidad usa Checkstyle y SpotBugs. Si Checkstyle se ejecuta con `sun_checks.xml`, puede requerir ajustes de estilo adicionales antes de pasar completamente.

## CI/CD

El repositorio incluye workflows de GitHub Actions:

- `.github/workflows/maven-ci.yml`: compila y ejecuta el build Maven en pushes y pull requests.
- `.github/workflows/maven-release.yml`: genera una nueva release de forma manual.

## Crear una release

Desde GitHub:

1. Ir a `Actions`.
2. Seleccionar `Maven Release`.
3. Pulsar `Run workflow`.
4. Informar `release_version`, por ejemplo `5.3.1`.

El pipeline:

1. Configura JDK 21.
2. Cambia la versión del `pom.xml`.
3. Ejecuta tests.
4. Publica el paquete en GitHub Packages.
5. Crea el tag `vX.Y.Z`.
6. Crea una GitHub Release con el JAR generado.

Para publicar en GitHub Packages, el repositorio debe tener permisos de escritura para Actions:

```text
Settings > Actions > General > Workflow permissions > Read and write permissions
```

## Auditoría

La auditoría técnica del proyecto se encuentra en:

```text
doc/auditoria/2026_05_09_auditoria_proyecto.md
```

## Notas de seguridad

- No versionar credenciales SMTP.
- No incluir contraseñas reales en demos, tests ni documentación.
- Rotar cualquier secreto que haya estado en el repositorio.
- Preferir variables de entorno o gestor de secretos.
- Revisar el histórico Git si se sospecha exposición previa.
