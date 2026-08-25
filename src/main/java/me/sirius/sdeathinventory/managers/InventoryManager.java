package me.sirius.sdeathinventory.managers;

import me.sirius.sdeathinventory.SDeathInventory;
import me.sirius.sdeathinventory.models.DeathInventoryData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManager {
    
    private final SDeathInventory plugin;
    private final Map<UUID, DeathInventoryData> inventoryCache;
    private final Map<UUID, Long> viewTimeCache;
    private BukkitTask cleanupTask;
    
    public InventoryManager(SDeathInventory plugin) {
        this.plugin = plugin;
        this.inventoryCache = new ConcurrentHashMap<>();
        this.viewTimeCache = new ConcurrentHashMap<>();
    }
    
    public void storeInventory(UUID viewId, DeathInventoryData data) {
        inventoryCache.put(viewId, data);
        viewTimeCache.put(viewId, System.currentTimeMillis());
    }
    
    public DeathInventoryData getInventory(UUID viewId) {
        DeathInventoryData data = inventoryCache.get(viewId);
        if (data != null && isExpired(viewId)) {
            removeInventory(viewId);
            return null;
        }
        return data;
    }
    
    public boolean isMessageExpired(UUID viewId) {
        Long timestamp = viewTimeCache.get(viewId);
        if (timestamp == null) return true;
        
        long messageTime = plugin.getConfigManager().getMessageTimeSeconds() * 1000L;
        return System.currentTimeMillis() - timestamp > messageTime;
    }
    
    public void removeInventory(UUID viewId) {
        inventoryCache.remove(viewId);
        viewTimeCache.remove(viewId);
    }
    
    private boolean isExpired(UUID viewId) {
        Long timestamp = viewTimeCache.get(viewId);
        if (timestamp == null) return true;
        
        long expirationTime = plugin.getConfigManager().getCacheExpiration() * 1000L;
        return System.currentTimeMillis() - timestamp > expirationTime;
    }
    
    public void startCleanupTask() {
        int intervalSeconds = plugin.getConfigManager().getCleanupInterval();
        cleanupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            long currentTime = System.currentTimeMillis();
            long expirationTime = plugin.getConfigManager().getCacheExpiration() * 1000L;
            
            viewTimeCache.entrySet().removeIf(entry -> {
                if (currentTime - entry.getValue() > expirationTime) {
                    inventoryCache.remove(entry.getKey());
                    return true;
                }
                return false;
            });
        }, intervalSeconds * 20L, intervalSeconds * 20L);
    }
    
    public void cleanup() {
        if (cleanupTask != null) {
            cleanupTask.cancel();
        }
        inventoryCache.clear();
        viewTimeCache.clear();
    }
    
    public Inventory createInventoryGUI(Player viewer, DeathInventoryData data) {
        String title = plugin.getConfigManager().getGuiTitle()
            .replace("{player}", data.getPlayerName());
        
        Inventory inv = Bukkit.createInventory(null, 54, title);
        
        ItemStack helmet = data.getHelmet();
        if (helmet != null) {
            ItemStack display = helmet.clone();
            addDurabilityLore(display);
            inv.setItem(0, display);
        }
        
        ItemStack chestplate = data.getChestplate();
        if (chestplate != null) {
            ItemStack display = chestplate.clone();
            addDurabilityLore(display);
            inv.setItem(1, display);
        }
        
        ItemStack leggings = data.getLeggings();
        if (leggings != null) {
            ItemStack display = leggings.clone();
            addDurabilityLore(display);
            inv.setItem(2, display);
        }
        
        ItemStack boots = data.getBoots();
        if (boots != null) {
            ItemStack display = boots.clone();
            addDurabilityLore(display);
            inv.setItem(3, display);
        }
        
        if (data.getOffHand() != null) {
            inv.setItem(4, data.getOffHand().clone());
        }
        
        ItemStack pingInfo = new ItemStack(Material.CLOCK);
        ItemMeta pingMeta = pingInfo.getItemMeta();
        pingMeta.setDisplayName(plugin.getConfigManager().getPingDisplay()
            .replace("{ping}", String.valueOf(data.getPing())));
        pingInfo.setItemMeta(pingMeta);
        inv.setItem(5, pingInfo);
        
        ItemStack healthInfo = new ItemStack(Material.RED_DYE);
        ItemMeta healthMeta = healthInfo.getItemMeta();
        healthMeta.setDisplayName(plugin.getConfigManager().getHealthDisplay()
            .replace("{health}", String.format("%.1f", data.getHealth())));
        healthInfo.setItemMeta(healthMeta);
        inv.setItem(6, healthInfo);
        
        ItemStack foodInfo = new ItemStack(Material.COOKED_BEEF);
        ItemMeta foodMeta = foodInfo.getItemMeta();
        foodMeta.setDisplayName(plugin.getConfigManager().getFoodDisplay()
            .replace("{food}", String.valueOf(data.getFoodLevel())));
        foodInfo.setItemMeta(foodMeta);
        inv.setItem(7, foodInfo);
        
        SimpleDateFormat sdf = new SimpleDateFormat(plugin.getConfigManager().getDateFormat());
        String dateTime = sdf.format(new Date(data.getTimestamp()));
        
        ItemStack dateInfo = new ItemStack(Material.PAPER);
        ItemMeta dateMeta = dateInfo.getItemMeta();
        dateMeta.setDisplayName(plugin.getConfigManager().getDateDisplay()
            .replace("{date}", dateTime));
        dateInfo.setItemMeta(dateMeta);
        inv.setItem(8, dateInfo);
        
        Material decorativeMaterial;
        try {
            decorativeMaterial = Material.valueOf(plugin.getConfigManager().getDecorativeMaterial().toUpperCase());
        } catch (IllegalArgumentException e) {
            decorativeMaterial = Material.RED_STAINED_GLASS_PANE;
        }
        
        ItemStack decorative = new ItemStack(decorativeMaterial);
        ItemMeta decorativeMeta = decorative.getItemMeta();
        decorativeMeta.setDisplayName(plugin.getConfigManager().getDecorativeName());
        decorative.setItemMeta(decorativeMeta);
        
        for (int i = 9; i < 18; i++) {
            inv.setItem(i, decorative);
        }
        
        ItemStack[] contents = data.getContents();
        
        for (int i = 9; i <= 17; i++) {
            int contentIndex = i - 9;
            if (contentIndex < contents.length && contents[contentIndex] != null) {
                inv.setItem(i + 36, contents[contentIndex].clone());
            }
        }
        
        for (int i = 9; i < 36; i++) {
            if (i < contents.length && contents[i] != null) {
                inv.setItem(i + 9, contents[i].clone());
            }
        }
        
        return inv;
    }
    
    private void addDurabilityLore(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;
        
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable)) return;
        
        Damageable damageable = (Damageable) meta;
        int maxDurability = item.getType().getMaxDurability();
        int damage = damageable.getDamage();
        int remaining = maxDurability - damage;
        
        if (maxDurability > 0) {
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            lore.add(plugin.getConfigManager().getDurabilityDisplay()
                .replace("{durability}", remaining + "/" + maxDurability));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }
    
    public int getCacheSize() {
        return inventoryCache.size();
    }
}