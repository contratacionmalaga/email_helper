# Publicacion de releases y packages

Este documento describe el procedimiento para publicar nuevas versiones de `email-helper` y generar automaticamente el paquete Maven asociado en GitHub Packages.

## Objetivo

Cada release publicada en GitHub debe tener un paquete Maven asociado.

El flujo configurado es:

```text
Crear/publicar release
        |
        v
Workflow Publish Maven Package
        |
        v
./mvnw -B deploy
        |
        v
Paquete publicado en GitHub Packages
```

## Workflows disponibles

### Maven Release

Archivo:

```text
.github/workflows/maven-release.yml
```

Uso:

- Se ejecuta manualmente desde GitHub Actions.
- Solicita una version, por ejemplo `5.3.1`.
- Actualiza la version del `pom.xml`.
- Ejecuta el build.
- Crea commit de release si cambia el `pom.xml`.
- Crea el tag `vX.Y.Z`.
- Crea la GitHub Release.

Este workflow no publica directamente el paquete Maven. Al crear la release, dispara el workflow automatico de publicacion.

### Publish Maven Package

Archivo:

```text
.github/workflows/maven-publish-package.yml
```

Uso:

- Se ejecuta automaticamente cuando una release queda publicada.
- Hace checkout del tag de la release.
- Configura JDK 21.
- Obtiene la version desde el tag `vX.Y.Z`.
- Ajusta la version Maven del paquete a `X.Y.Z` antes de publicar.
- Ejecuta `./mvnw -B deploy`.
- Publica el artefacto en GitHub Packages.

## Requisitos previos

### Permisos del repositorio

En GitHub:

```text
Settings > Actions > General > Workflow permissions
```

Debe estar activado:

```text
Read and write permissions
```

Esto permite que GitHub Actions pueda:

- Crear commits.
- Crear tags.
- Crear releases.
- Publicar packages.

### `pom.xml`

El `pom.xml` debe tener configurado `distributionManagement` con el server id usado por el workflow:

```xml
<distributionManagement>
    <repository>
        <id>github-releases</id>
        <url>https://maven.pkg.github.com/contratacionmalaga/email-helper</url>
    </repository>
    <snapshotRepository>
        <id>github-snapshots</id>
        <url>https://maven.pkg.github.com/contratacionmalaga/email-helper</url>
    </snapshotRepository>
</distributionManagement>
```

El workflow configura ese server con `actions/setup-java`:

```yaml
server-id: github-releases
server-username: GITHUB_ACTOR
server-password: GITHUB_TOKEN
```

## Publicar una nueva version

### 1. Verificar el proyecto en local

En Windows:

```powershell
.\mvnw.cmd test
```

Opcionalmente:

```powershell
.\mvnw.cmd clean install
```

### 2. Comprobar que no hay secretos

Antes de publicar, comprobar que no se versionan contrasenas, tokens o claves SMTP:

```powershell
rg -n "password|PASSWORD|token|TOKEN|secret|SECRET|SMTP_PASSWORD" README.md src .github pom.xml
```

La documentacion puede contener nombres de variables como `SMTP_PASSWORD`, pero no valores reales.

### 3. Ejecutar el workflow de release

En GitHub:

```text
Actions > Maven Release > Run workflow
```

Informar:

```text
release_version = 5.3.1
```

Usar versionado semantico:

- `MAJOR`: cambios incompatibles, por ejemplo `6.0.0`.
- `MINOR`: nuevas funcionalidades compatibles, por ejemplo `5.4.0`.
- `PATCH`: correcciones compatibles, por ejemplo `5.3.1`.

### 4. Verificar la GitHub Release

Tras finalizar `Maven Release`, revisar:

```text
Releases > vX.Y.Z
```

Debe existir:

- Tag `vX.Y.Z`.
- Release publicada.
- JAR adjunto.

### 5. Verificar el package

Al publicarse la release, GitHub dispara automaticamente:

```text
Publish Maven Package
```

Revisar:

```text
Actions > Publish Maven Package
```

Debe finalizar correctamente.

Despues comprobar el paquete en:

```text
GitHub repository > Packages
```

## Crear una release manualmente

Si una release se crea directamente desde la pantalla de GitHub Releases, tambien se publicara el paquete Maven porque el workflow `Publish Maven Package` escucha el evento:

```yaml
on:
  release:
    types:
      - published
```

Condicion importante:

- El tag debe usar el formato `vX.Y.Z`.

Si se crea manualmente una release `v5.3.1`, el workflow ajustara temporalmente el `pom.xml` del runner a `5.3.1` antes de ejecutar `deploy`. Asi el package publicado queda identificado con la misma version de la release.

Por ese motivo, la via recomendada es usar siempre:

```text
Actions > Maven Release
```

## Consumir el paquete

Dependencia Maven:

```xml
<dependency>
    <groupId>local.jarios</groupId>
    <artifactId>email-helper</artifactId>
    <version>5.3.1</version>
</dependency>
```

El proyecto consumidor debe tener acceso a GitHub Packages y configurar el repositorio:

```xml
<repositories>
    <repository>
        <id>github-releases</id>
        <url>https://maven.pkg.github.com/contratacionmalaga/email-helper</url>
    </repository>
</repositories>
```

## Problemas comunes

### Error 403 al publicar package

Revisar:

- `Workflow permissions` en modo `Read and write`.
- Que el workflow tenga:

```yaml
permissions:
  contents: read
  packages: write
```

### El package se publica con version incorrecta

Causa probable:

- El tag de la release no usa el formato `vX.Y.Z`.
- El workflow antiguo pudo ejecutarse antes de que se incorporase el ajuste automatico de version desde el tag.

Solucion:

- Usar `Maven Release`.
- Usar tags con formato `vX.Y.Z`.
- Comprobar en `Publish Maven Package` el paso `Resolve package version from release tag`.

## Borrado de releases y packages antiguos

Borrar releases y packages antiguos es una operacion destructiva. Antes de hacerlo hay que decidir una politica clara.

Politica recomendada:

- Conservar solo la ultima version estable.
- Conservar, si aplica, la ultima version anterior usada por aplicaciones en produccion.
- Eliminar releases y packages de prueba.
- No reutilizar numeros de version eliminados salvo que se tenga claro que ningun consumidor los esta usando.

Antes de borrar:

```powershell
gh release list
```

Para borrar una release y su tag:

```powershell
gh release delete vX.Y.Z --cleanup-tag
```

Para listar packages Maven:

```powershell
gh api /orgs/contratacionmalaga/packages?package_type=maven
```

El borrado de versiones de packages debe hacerse con la API de GitHub Packages identificando primero el package y la version concreta. No se recomienda automatizar este borrado dentro del pipeline de release normal, porque un error de versionado podria eliminar artefactos consumidos por aplicaciones.

### El workflow de package no se dispara

Revisar:

- Que la release este publicada, no en draft.
- Que el workflow exista en la rama por defecto.
- Que el evento sea `release.published`.

### El deploy falla porque el paquete ya existe

GitHub Packages no permite sobrescribir una version release ya publicada.

Solucion:

- Publicar una version nueva.
- No reutilizar tags ni versiones Maven.

## Checklist final

Antes de publicar:

- [ ] `.\mvnw.cmd test` pasa en local.
- [ ] No hay secretos reales versionados.
- [ ] La version elegida no existe previamente como tag.
- [ ] La version elegida no existe previamente como package.
- [ ] GitHub Actions tiene permisos de escritura.

Despues de publicar:

- [ ] Existe tag `vX.Y.Z`.
- [ ] Existe GitHub Release `vX.Y.Z`.
- [ ] El workflow `Publish Maven Package` ha terminado correctamente.
- [ ] El package aparece en GitHub Packages.
