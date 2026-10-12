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
    // Un solo mapa: la hora de la muerte ya viaja dentro de DeathInventoryData. Antes
    // habia un segundo mapa con la misma clave solo para la hora, y los dos podian
    // quedar desparejados.
    private final Map<UUID, DeathInventoryData> inventoryCache = new ConcurrentHashMap<>();
    private final Set<UUID> viewers = ConcurrentHashMap.newKeySet();
    private BukkitTask cleanupTask;

    public InventoryManager(SDeathInventory plugin) {
        this.plugin = plugin;
    }

    public void storeInventory(UUID viewId, DeathInventoryData data) {
        inventoryCache.put(viewId, data);
    }

    public DeathInventoryData getInventory(UUID viewId) {
        DeathInventoryData data = inventoryCache.get(viewId);
        if (data != null && olderThan(data, plugin.getConfigManager().getCacheExpiration())) {
            inventoryCache.remove(viewId);
            return null;
        }
        return data;
    }

    public boolean isMessageExpired(UUID viewId) {
        DeathInventoryData data = inventoryCache.get(viewId);
        return data == null || olderThan(data, plugin.getConfigManager().getMessageTimeSeconds());
    }

    public void removeInventory(UUID viewId) {
        inventoryCache.remove(viewId);
    }

    private static boolean olderThan(DeathInventoryData data, int seconds) {
        return System.currentTimeMillis() - data.getTimestamp() > seconds * 1000L;
    }

    public void startCleanupTask() {
        int intervalSeconds = plugin.getConfigManager().getCleanupInterval();
        // La caducidad se lee aqui, en el hilo principal: la config no se toca desde el async.
        cleanupTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int expiration = plugin.getConfigManager().getCacheExpiration();
            inventoryCache.values().removeIf(data -> olderThan(data, expiration));
        }, intervalSeconds * 20L, intervalSeconds * 20L);
    }

    public void open(Player viewer, DeathInventoryData data) {
        Inventory inventory = createInventoryGUI(viewer, data);
        viewer.openInventory(inventory);
        // Despues de abrir: openInventory cierra el inventario anterior, y ese cierre
        // borraria la marca si se pusiera antes.
        viewers.add(viewer.getUniqueId());
    }

    public boolean isViewing(UUID player) {
        return !viewers.isEmpty() && viewers.contains(player);
    }

    public void closed(UUID player) {
        viewers.remove(player);
    }

    public void cleanup() {
        if (cleanupTask != null) {
            cleanupTask.cancel();
        }
        inventoryCache.clear();
        for (UUID uuid : List.copyOf(viewers)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.closeInventory();
            }
        }
        viewers.clear();
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