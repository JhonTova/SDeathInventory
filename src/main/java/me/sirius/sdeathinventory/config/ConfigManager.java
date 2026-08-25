package me.sirius.sdeathinventory.config;

import me.sirius.sdeathinventory.SDeathInventory;
import me.sirius.core.text.Text;

public class ConfigManager {
    
    private final SDeathInventory plugin;
    
    public ConfigManager(SDeathInventory plugin) {
        this.plugin = plugin;
    }
    
    public String getKilledMessage() {
        return Text.legacy(plugin.getConfig().getString("messages.killed", "&cFuiste asesinado por: &e{killer}&c. Ver inventario "));
    }
    
    public String getKillerMessage() {
        return Text.legacy(plugin.getConfig().getString("messages.killer", "&aAsesinaste a: &e{victim}&a. Ver inventario "));
    }
    
    public String getClickHere() {
        return Text.legacy(plugin.getConfig().getString("messages.click-here", "&6&l[AQUÍ]"));
    }
    
    public String getClickHereHover() {
        return Text.legacy(plugin.getConfig().getString("messages.click-here-hover", "&eClick para ver el inventario"));
    }
    
    public String getGuiTitle() {
        return Text.legacy(plugin.getConfig().getString("gui.title", "&8&lSharpness &7| &cInventory Death of &e{player}"));
    }
    
    public String getHealthDisplay() {
        return Text.legacy(plugin.getConfig().getString("gui.health-display", "&cVida: &f{health}"));
    }
    
    public String getFoodDisplay() {
        return Text.legacy(plugin.getConfig().getString("gui.food-display", "&6Comida: &f{food}"));
    }
    
    public String getPingDisplay() {
        return Text.legacy(plugin.getConfig().getString("gui.ping-display", "&ePing: &f{ping}ms"));
    }
    
    public String getDateDisplay() {
        return Text.legacy(plugin.getConfig().getString("gui.date-display", "&7Muerte: &f{date}"));
    }
    
    public String getDurabilityDisplay() {
        return Text.legacy(plugin.getConfig().getString("gui.durability-display", "&7Durabilidad: &f{durability}"));
    }
    
    public String getDecorativeMaterial() {
        return plugin.getConfig().getString("gui.decorative-material", "RED_STAINED_GLASS_PANE");
    }
    
    public String getDecorativeName() {
        return Text.legacy(plugin.getConfig().getString("gui.decorative-name", " "));
    }
    
    public String getDateFormat() {
        return plugin.getConfig().getString("gui.date-format", "dd/MM/yyyy HH:mm:ss");
    }
    
    public String getExpiredMessage() {
        return Text.legacy(plugin.getConfig().getString("messages.expired", "&cEste inventario ya no está disponible."));
    }
    
    public String getMessageExpiredMessage() {
        return Text.legacy(plugin.getConfig().getString("messages.message-expired", "&cEl tiempo para ver este inventario ha expirado."));
    }
    
    public int getCacheExpiration() {
        return plugin.getConfig().getInt("cache.expiration-seconds", 300);
    }
    
    public int getMessageTimeSeconds() {
        return plugin.getConfig().getInt("cache.message-time-seconds", 60);
    }
    
    public int getCleanupInterval() {
        return plugin.getConfig().getInt("cache.cleanup-interval-seconds", 30);
    }
    
    public String getReloadMessage() {
        return Text.legacy(plugin.getConfig().getString("messages.reload", "&aConfiguración recargada correctamente!"));
    }
    
    public String getNoPermission() {
        return Text.legacy(plugin.getConfig().getString("messages.no-permission", "&cNo tienes permiso para usar este comando."));
    }
}