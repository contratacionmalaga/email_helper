# 📧 Helper para el envío de Emails utilizando jakarta.mail-api<

Proyecto Java para **cargar configuración desde archivos `.properties`**, gestionar valores sensibles cifrados y **enviar correos electrónicos** mediante una configuración centralizada y segura.

> **Autor:** Juan Antonio  
> **Fecha de inicio:** 04/06/2024

---

## 🔧 Funcionalidades principales

- ✅ Carga de configuración desde ficheros `.properties`
- ✅ Lectura segura de valores sensibles (`password`, `token`, etc.)
- ✅ Cifrado y descifrado de propiedades con `Jasypt`
- ✅ Envío de correos electrónicos con `JavaMail`
- ✅ Validación y trazabilidad vía `SLF4J` / `Logback`
- ✅ Estructura modular y extensible
- ✅ Uso de jakarta.mail.api

---

## 📁 Estructura del proyecto

```
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── local.jarios/
│ │ │ ├── PruebaEmail.java # Clase principal
│ │ │ ├── properties/ # Módulo de configuración
│ │ │ └── email/ # Módulo de envío de correo
│ │ └── resources/
│ │ └── config/
│ │ └── email.properties # Fichero de configuración
└── README.md
```

---

## ⚙️ Requisitos

- Java 11 o superior
- Maven / Gradle
- Acceso a servidor SMTP
- Variable de entorno con **clave maestra de cifrado**

---

## 🔐 Cifrado seguro con `Jasypt`

Este proyecto utiliza **Jasypt (`BasicTextEncryptor`)** para cifrar claves sensibles como contraseñas. Los valores cifrados se almacenan así:

```properties
mail.password=ENC(xxxxxxx)
```

## 🛡 Cómo cifrar una clave

```
BasicTextEncryptor encryptor = new BasicTextEncryptor();
encryptor.setPassword("MI_CLAVE_MAESTRA");
String valorCifrado = "ENC(" + encryptor.encrypt("mi_clave") + ")";
```

## 📝 Formato de email.properties

```
mail.smtp.host=smtp.servidor.com
mail.smtp.port=587
mail.user=usuario@ejemplo.com
mail.password=ENC(VALOR_CIFRADO)
mail.to=destinatario@ejemplo.com
mail.from=remitente@ejemplo.com
mail.smtp.auth=true
mail.smtp.starttls.enable=true
```
