# 📧 Helper para el envío de Emails utilizando jakarta.mail-api<

Herramienta Java para el envío de correos electrónicos mediante SMTP de forma sencilla, robusta y extensible.  
Ideal para proyectos que requieren auditoría, monitoreo o notificaciones automatizadas.

> **Autor:** Juan Antonio  
> **Fecha de inicio:** 04/06/2024

---

## 🧩 Características

- Envío de correos vía SMTP autenticado con TLS
- Validación de direcciones de correo (`RFC 5322`)
- Cuerpo del email soporta HTML con estructuras dinámicas (tablas, secciones, etc.)
- Separación clara de responsabilidades: modelo, servicio, helper
- Manejo centralizado de errores personalizados

---

## 📁 Estructura del proyecto

```
src/
└── main/
    └── java/
        └── local/
            └── jarios/
                ├── email/                          # Punto de entrada y clases de ejemplo/demo
                │   ├── EmailDemo.java              # Clase con el método main para ejecutar una prueba de envío
                │
                ├── api/                            # API pública del servicio de correo
                │   ├── EmailService.java           # Interfaz del servicio de envío de correo
                │   └── EmailServiceImpl.java       # Implementación SMTP del servicio de envío
                │
                ├── model/                          # Modelos de datos relacionados con el correo
                │   └── EmailData.java              # Record que encapsula from, to, subject y body
                │
                ├── helper/                         # Clases utilitarias para construir correos en HTML
                │   └── EmailHelper.java            # Métodos estáticos para componer el cuerpo del correo
                │
                ├── exception/                      # Manejo de errores personalizados
                │   └── EmailServiceException.java  # Excepción específica para errores de envío
                │
                └── common/                         # Utilidades y constantes compartidas
                    └── util/
                        ├── Constantes.java         # Constantes como claves de properties SMTP
                        └── EmailValidator.java     # Validaciones RFC para direcciones de email
test/
└── main/
    └── java/
        └── local/
            └── jarios/
                ├── email/
                    PENDIENTE                
└── README.md
└── pom.xml
```

---

## ⚙️ Requisitos

- Java 11 o superior
- Maven / Gradle
- Acceso a servidor SMTP
- Variable de entorno con **clave maestra de cifrado**

---

## 🔐 Clases principales

✅ EmailService (interface)

```
void enviarEmail(Properties props, EmailData emailData) throws EmailServiceException;
```

✅ EmailServiceImpl

Implementación concreta que utiliza JavaMail (jakarta.mail) para enviar emails mediante SMTP.

- Valida campos obligatorios
- Realiza autenticación
- Permite log extendido en debug
- Soporta HTML

✅ EmailData (record)

Contenedor inmutable que representa los datos del email:

```
public record EmailData(String from, String to, String subject, String body) {}
```

✅ EmailHelper

Utilidades para generar partes del HTML del correo: cabecera, tablas, pie, asunto dinámico, etc.

✅ EmailServiceException
Excepción personalizada que encapsula los errores del servicio de correo.

✅ EmailDemo
Clase de ejemplo que muestra el flujo completo:

1. Carga propiedades SMTP
2. Construye EmailData
3. Envía el correo con EmailServiceImpl

⚙️ Configuración SMTP (ejemplo)
```properties
mail.smtp.host=
mail.smtp.auth=
mail.smtp.port=
mail.smtp.user=
mail.smtp.password=
mail.smtp.starttls.enable=
mail.smtp.trust=
mail.smtp.protocols=
```

🚀 Ejemplo de uso
```
Properties smtpProps = new Properties();
// ... set propiedades SMTP como en ejemplo

EmailData data = new EmailData(
    "origen@dominio.com",
    "destino@dominio.com",
    "Asunto de prueba",
    EmailHelper.getCabeceraHtml() +
    EmailHelper.getHead() +
    EmailHelper.getCabeceraBody("Mensaje de prueba") +
    EmailHelper.getInicioTable() +
    EmailHelper.getFila("Campo", "Valor") +
    EmailHelper.getPieTable() +
    EmailHelper.getPieBody() +
    EmailHelper.getPieHtml()
);

EmailService emailService = new EmailServiceImpl();
emailService.enviarEmail(smtpProps, data);
```

🛠️ Requisitos

- Java 21 o superior
- Jakarta Mail (jakarta.mail:jakarta.mail-api)
- Lombok (opcional)

📝 Notas

- Si quieres desacoplar la configuración SMTP, puedes externalizarla en un .properties o .yaml.
- En producción, nunca incluyas contraseñas directamente en código. Usa vaults, variables de entorno o cifrado.
- El código está preparado para ser migrado a un servicio más complejo, incluyendo colas o APIs REST.

✨ Futuras mejoras

- Plantillas de HTML parametrizadas
- Reintentos automáticos en caso de error
- Soporte para múltiples destinatarios (CC, BCC)
- Adjuntos
- Internacionalización (asuntos, cuerpos)

👤 Autor

Juan Antonio Ríos — jarios@malaga.es