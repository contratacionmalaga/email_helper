# Importar Ficheros GC (configuración de PLACSP) desde EXCEL a una base de datos

## Información General del Aplicativo
Este aplicativo se encarga de importar la infomación asociada al Directorio Común de Unidades Orgánicas y Oficinas (DIR3) para las entidades locales (EELL) de forma que posteriormente podamos explotar dicha información. Las tecnologías utilizadas para este aplicativo son:

- __Lenguaje de Programación__: Java en su versión `v21.0.5`.
- __Base de datos__: PostgreSQL en su versión `17.2` haciendo uso de esquemas.
- __Versión del framework Hibernate__: HikariCP en su versión `6.6.9` para core y `6.6.9` para HikariCP
- __Framework acceso a la base de datos__: HikariCP en su versión `6.2.1`.
- __Libreria PostgreSQL__: PostgreSQL Connector for Java en su versión `42.7.5`.
- __Biblioteca Lombok__: Biblioteca que facilita la programación mediante la inyección de código mediante etiquetas. Utilizamos la versión `1.18.34`.
- __Gestión de Logs__: Utilizamos `slf4j` como fachada para los logs de los componentes y como elemento generador de logs utilizamos `log4j`. Las versiones de los productos utilizadas son las siguientes:
    - __Fachada__: `slf4j` versión `2.0.16` junto con la implementación `log4j-slf4j2-impl` para `log4j` en su versión `2.24.3`.
    - __Gestor de Logs__: `log4j` en su versión `2.24.3`.
- Parseo de los ficheros GC con
    - __jaxb-core__: `2.3.0.1`
    - __jaxb-imp__: `2.3.0.1`
    - __jaxb-api__: `2.4.0-b180830.0359`
    - __javax.activation-api__: `1.2.0`
- __Envío de email__: Jakarta Mail con los siguientes paquetes:
    - __jakarta.mail-api__: `2.1.3`
    - __jakarta.activation-api__: `2.1.3`
    - __angus-mail__: `2.0.3`

## Actuaciones realizadas

* __TODO__
    
    * Crear los mensajes en un fichero de `mensajes.properties` para que sea mas sencilla la modificación de los mensajes.
    * Carga del EXCEL desde __URL__
    * La Gestión de las Excepciones tal y como se realiza en los códigos de ejemplo
    
* __27/12/2025__ 
    * Actualización a la versión última de PostgreSQL, Hibernate, HikariCP y Jakarta Mail
    * Eliminación de los LOGGER por @Slf4j
    * Actualización del fichero log4j2.xml para la desagregación de los logs en nuevos ficheros
    * 

***

<p>
Juan Antonio Ríos Peláez
</p>

`jarios@malaga.es`
