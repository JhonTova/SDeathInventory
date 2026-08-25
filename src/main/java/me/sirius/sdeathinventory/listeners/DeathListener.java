package me.sirius.sdeathinventory.listeners;

import me.sirius.core.text.Text;
import me.sirius.sdeathinventory.SDeathInventory;
import me.sirius.sdeathinventory.models.DeathInventoryData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class DeathListener implements Listener {
    
    private final SDeathInventory plugin;
    
    public DeathListener(SDeathInventory plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer == null || killer.equals(victim)) {
            return;
        }
        
        DeathInventoryData victimData = new DeathInventoryData(victim);
        DeathInventoryData killerData = new DeathInventoryData(killer);
        
        UUID victimViewId = UUID.randomUUID();
        UUID killerViewId = UUID.randomUUID();
        
        plugin.getInventoryManager().storeInventory(victimViewId, killerData);
        plugin.getInventoryManager().storeInventory(killerViewId, victimData);
        
        sendMessageToVictim(victim, killer.getName(), victimViewId);
        sendMessageToKiller(killer, victim.getName(), killerViewId);
    }
    
    private void sendMessageToVictim(Player victim, String killerName, UUID inventoryId) {
        String message = plugin.getConfigManager().getKilledMessage().replace("{killer}", killerName);
        Text.sendClickable(victim, message,
                plugin.getConfigManager().getClickHere(),
                "/sdeathinventory view " + inventoryId,
                plugin.getConfigManager().getClickHereHover());
    }

    private void sendMessageToKiller(Player killer, String victimName, UUID inventoryId) {
        String message = plugin.getConfigManager().getKillerMessage().replace("{victim}", victimName);
        Text.sendClickable(killer, message,
                plugin.getConfigManager().getClickHere(),
                "/sdeathinventory view " + inventoryId,
                plugin.getConfigManager().getClickHereHover());
    }
}