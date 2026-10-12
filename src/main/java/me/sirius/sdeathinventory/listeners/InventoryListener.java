package me.sirius.sdeathinventory.listeners;

import me.sirius.sdeathinventory.managers.InventoryManager;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

// Antes se leia y se parseaba el titulo de cada inventario en cada clic del
// servidor para reconocer el GUI. Ahora basta con saber quien lo tiene abierto.
public class InventoryListener implements Listener {

    private final InventoryManager inventories;

    public InventoryListener(InventoryManager inventories) {
        this.inventories = inventories;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (inventories.isViewing(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (inventories.isViewing(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    // Tambien salta al desconectarse con el GUI abierto, asi que cubre el quit.
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        inventories.closed(event.getPlayer().getUniqueId());
    }
}
