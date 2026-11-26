<div align="center">

# SDeathInventory

### Mira exactamente qué llevaba un jugador al morir.

Herramienta esencial para el staff: revisa inventarios de muerte con un clic, resuelve reportes al instante.

[![build](https://github.com/JhonTova/SDeathInventory/actions/workflows/build.yml/badge.svg)](https://github.com/JhonTova/SDeathInventory/actions/workflows/build.yml) [![release](https://img.shields.io/github/v/release/JhonTova/SDeathInventory?label=release)](https://github.com/JhonTova/SDeathInventory/releases/latest) ![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11%2B-62B47A) ![Java](https://img.shields.io/badge/Java-21-E76F00)

</div>

---

## Características

- **Captura el inventario completo** en el momento de la muerte: ítems, armadura, todo.
- **Mensaje pulsable** — al morir, el jugador (o el staff) recibe un `[AQUÍ]` clicable que
  abre el inventario guardado.
- **GUI de solo lectura** para revisar el estado exacto sin tocar nada.
- **Expiración configurable** — los registros viejos se limpian solos.
- Guarda quién mató a quién.

## Comandos

| Comando | Acción |
|---|---|
| `/sdi view <id>` | Abre un inventario de muerte guardado. |
| `/sdi reload` | Recarga la configuración. |

## Requisitos

Requiere **[SiriusCore](https://github.com/JhonTova/SiriusCore)**.

## Instalación

`SiriusCore.jar` + `SDeathInventory.jar` en `plugins/`. Spigot / Paper / Purpur 1.21.11+.

## Descarga

El `.jar` de cada versión está en [Releases](https://github.com/JhonTova/SDeathInventory/releases/latest).

## Compilar desde el código

Requiere Java 21 y Maven. SiriusCore no está en un repositorio Maven público, así que
primero se instala en local la versión que declara el `pom.xml`:

```bash
git clone --branch v1.1.1 https://github.com/JhonTova/SiriusCore.git
mvn -f SiriusCore/pom.xml install -DskipTests
mvn package        # en este repositorio; el .jar queda en target/
```

Cada push y pull request se compila en GitHub Actions. Al subir una tag `v*`, el `.jar`
se adjunta solo a la release.
