package me.sirius.sdeathinventory.listeners;

import me.sirius.core.text.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryListener implements Listener {
    
    private static final String GUI_IDENTIFIER = "Inventory Death of";
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView() == null || event.getView().getTitle() == null) {
            return;
        }
        
        String title = Text.strip(event.getView().getTitle());
        
        if (title.contains(GUI_IDENTIFIER)) {
            event.setCancelled(true);
            event.setResult(org.bukkit.event.Event.Result.DENY);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInventoryDrag(InventoryDragEvent event) {

        if (event.getView() == null || event.getView().getTitle() == null) {
            return;
        }
        
        String title = Text.strip(event.getView().getTitle());
        
        if (title.contains(GUI_IDENTIFIER)) {
            event.setCancelled(true);
            event.setResult(org.bukkit.event.Event.Result.DENY);
        }
    }
}