package me.sirius.sdeathinventory.models;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeathInventoryData {
    
    private final String playerName;
    private final ItemStack[] contents;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack mainHand;
    private final ItemStack offHand;
    private final double health;
    private final int foodLevel;
    private final int ping;
    private final long timestamp;
    
    public DeathInventoryData(Player player) {
        this.playerName = player.getName();
        
        ItemStack[] originalContents = player.getInventory().getContents();
        this.contents = new ItemStack[36];
        for (int i = 0; i < 36 && i < originalContents.length; i++) {
            this.contents[i] = cloneItem(originalContents[i]);
        }
        
        this.helmet = cloneItem(player.getInventory().getHelmet());
        this.chestplate = cloneItem(player.getInventory().getChestplate());
        this.leggings = cloneItem(player.getInventory().getLeggings());
        this.boots = cloneItem(player.getInventory().getBoots());
        this.mainHand = cloneItem(player.getInventory().getItemInMainHand());
        this.offHand = cloneItem(player.getInventory().getItemInOffHand());
        this.health = player.getHealth();
        this.foodLevel = player.getFoodLevel();
        this.ping = player.getPing();
        this.timestamp = System.currentTimeMillis();
    }
    
    private ItemStack cloneItem(ItemStack item) {
        return item != null ? item.clone() : null;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public ItemStack[] getContents() {
        return contents;
    }
    
    public ItemStack getHelmet() {
        return helmet;
    }
    
    public ItemStack getChestplate() {
        return chestplate;
    }
    
    public ItemStack getLeggings() {
        return leggings;
    }
    
    public ItemStack getBoots() {
        return boots;
    }
    
    public ItemStack getMainHand() {
        return mainHand;
    }
    
    public ItemStack getOffHand() {
        return offHand;
    }
    
    public double getHealth() {
        return health;
    }
    
    public int getFoodLevel() {
        return foodLevel;
    }
    
    public int getPing() {
        return ping;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}