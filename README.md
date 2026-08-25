<div align="center">

# SDeathInventory

### Mira exactamente qué llevaba un jugador al morir.

Herramienta esencial para el staff: revisa inventarios de muerte con un clic, resuelve reportes al instante.

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

Requiere **[SiriusCore](https://github.com/JhonTova)**.

## Instalación

`SiriusCore.jar` + `SDeathInventory.jar` en `plugins/`. Spigot / Paper / Purpur 1.21.11+.
