package me.sirius.sdeathinventory;

import me.sirius.sdeathinventory.commands.SDeathInventoryCommand;
import me.sirius.sdeathinventory.config.ConfigManager;
import me.sirius.sdeathinventory.listeners.DeathListener;
import me.sirius.sdeathinventory.listeners.InventoryListener;
import me.sirius.sdeathinventory.managers.InventoryManager;
import org.bukkit.command.PluginCommand;
import me.sirius.core.SiriusPlugin;

public class SDeathInventory extends SiriusPlugin {
    
    private static SDeathInventory instance;
    private ConfigManager configManager;
    private InventoryManager inventoryManager;
    
    @Override
    protected void onStart() {
        instance = this;
        
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.inventoryManager = new InventoryManager(this);
        
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(inventoryManager), this);
        
        SDeathInventoryCommand commandExecutor = new SDeathInventoryCommand(this);
        PluginCommand command = getCommand("sdeathinventory");
        if (command != null) {
            command.setExecutor(commandExecutor);
            command.setTabCompleter(commandExecutor);
        }
        
        inventoryManager.startCleanupTask();
        
        getLogger().info("§b╔═══════════════════════════════════════╗");
        getLogger().info("§b║ §d          ✦ SDeathInventory ✦             §b║");
        getLogger().info("§b║ §7      by §bSirius §8| §7GitHub: §fJhonTova      §b║");
        getLogger().info("§b║ §7       Discord: §fksirius                   §b║");
        getLogger().info("§b║ §a     Plugin habilitado con éxito!           §b║");
        getLogger().info("§b╚═══════════════════════════════════════╝");
    }
    
    @Override
    protected void onStop() {
        if (inventoryManager != null) {
            inventoryManager.cleanup();
        }
        getLogger().info("§b╔═══════════════════════════════════════╗");
        getLogger().info("§b║ §d          ✦ SDeathInventory ✦             §b║");
        getLogger().info("§b║ §7      by §bSirius §8| §7GitHub: §fJhonTova      §b║");
        getLogger().info("§b║ §7       Discord: §fksirius                   §b║");
        getLogger().info("§b║ §a     Plugin deshabilitado con éxito!           §b║");
        getLogger().info("§b╚═══════════════════════════════════════╝");
    }
    
    public static SDeathInventory getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}