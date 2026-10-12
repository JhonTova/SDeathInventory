package me.sirius.sdeathinventory.commands;

import me.sirius.core.text.Text;

import me.sirius.sdeathinventory.SDeathInventory;
import me.sirius.sdeathinventory.models.DeathInventoryData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SDeathInventoryCommand implements CommandExecutor, TabCompleter {
    
    private final SDeathInventory plugin;
    
    public SDeathInventoryCommand(SDeathInventory plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            Text.send(sender, "§7§m------------------§r §6SDeathInventory §7§m------------------");
            Text.send(sender, "§e/sdi reload §7- Recargar configuración");
            Text.send(sender, "§7Desarrollado por §bSirius");
            Text.send(sender, "§7§m------------------------------------------------");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("sdeathInventory.reload")) {
                Text.send(sender, plugin.getConfigManager().getNoPermission());
                return true;
            }
            
            plugin.reloadConfig();
            plugin.getConfigManager();
            Text.send(sender, plugin.getConfigManager().getReloadMessage());
            return true;
        }
        
        if (args[0].equalsIgnoreCase("view")) {
            // Validar que sea un jugador
            if (!(sender instanceof Player)) {
                Text.send(sender, "§cEste comando solo puede ser usado por jugadores.");
                return true;
            }
            
            Player player = (Player) sender;
            
            // Validar permiso ANTES de procesar
            if (!player.hasPermission("sdeathInventory.use")) {
                Text.send(player, plugin.getConfigManager().getNoPermission());
                return true;
            }
            
            if (args.length < 2) {
                Text.send(player, "§cUso: /sdi view <id>");
                return true;
            }
            
            UUID inventoryId;
            
            try {
                inventoryId = UUID.fromString(args[1]);
            } catch (IllegalArgumentException e) {
                Text.send(player, "§cID de inventario inválido.");
                return true;
            }
            
            if (plugin.getInventoryManager().isMessageExpired(inventoryId)) {
                Text.send(player, plugin.getConfigManager().getMessageExpiredMessage());
                plugin.getInventoryManager().removeInventory(inventoryId);
                return true;
            }
            
            DeathInventoryData data = plugin.getInventoryManager().getInventory(inventoryId);
            
            if (data == null) {
                Text.send(player, plugin.getConfigManager().getExpiredMessage());
                return true;
            }
            
            plugin.getInventoryManager().open(player, data);
            
            return true;
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            if (sender.hasPermission("sdeathInventory.reload")) {
                completions.add("reload");
            }
        }
        
        return completions;
    }
}