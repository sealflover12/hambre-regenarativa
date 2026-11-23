# Hambre Regenerativa — Fabric 26.3

Mod de servidor/cliente para Minecraft Java 26.3 con Fabric. Regenera **un corazón completo**
(2 puntos de salud) a intervalos determinados por el nivel de hambre.

## Tabla de regeneración

| Barra de hambre | Tiempo por corazón |
|---|---:|
| 9–10 muslitos | 2 segundos |
| 7–8 muslitos | 5 segundos |
| 5–6 muslitos | 10 segundos |
| 3–4 muslitos | 20 segundos |
| 1–2 muslitos | 40 segundos |
| 0 muslitos | 60 segundos |

La barra de hambre y la saturación vanilla no se reemplazan. El mod solo añade curación
pasiva. Se aplica en supervivencia y aventura; no interviene en creativo/espectador.

## Importante: hambre en cero

Minecraft sigue aplicando el daño vanilla por inanición cuando la barra llega a cero.
Para que la regeneración de 60 segundos sea posible, el mod cancela el daño de inanición
(`STARVE`) en jugadores. No cancela ningún otro tipo de daño.

## Requisitos para compilar

- JDK 25
- Gradle 9.6
- Minecraft 26.3
- Fabric Loader y Fabric API

El proyecto usa Fabric Loom no-remapping, porque Minecraft 26.1+ ya no está ofuscado.

## Compilar el .jar

Desde esta carpeta, ejecuta:

```bash
gradle build
```

El mod compilado se genera en:

```text
build/libs/hambre-regenerativa-1.0.0.jar
```

Copia ese `.jar` a la carpeta `mods` de tu instalación Fabric 26.3.
Fabric API 0.166.2+26.3 también debe estar instalada.

## Nota de compatibilidad

Este paquete contiene el código fuente y la configuración del proyecto. No incluye un `.jar`
precompilado: la compilación requiere JDK 25, Gradle 9.6 y descargar las dependencias oficiales
de Fabric. La configuración se alinea con el proyecto oficial de ejemplo de Fabric 26.3.

Para compilar:
1. Instala JDK 25 y Gradle 9.6.
2. Abre una terminal dentro de esta carpeta.
3. Ejecuta `gradle build`.
4. Copia `build/libs/hambre-regenerativa-1.0.0.jar` a `.minecraft/mods`.


## Compilar automáticamente en GitHub (sin instalar Java/Gradle)

1. Crea un repositorio vacío en GitHub.
2. Sube el contenido de este proyecto al repositorio (incluida la carpeta `.github`).
3. Abre la pestaña **Actions** y espera a que termine **Build Fabric mod**.
4. En la ejecución completada, descarga el artefacto `hambre-regenerativa-fabric-26.3`.
5. Dentro del ZIP de artefactos estará el `.jar` para `mods`.

El workflow usa Java 25 y Gradle 9.6 en un runner de GitHub Actions.
