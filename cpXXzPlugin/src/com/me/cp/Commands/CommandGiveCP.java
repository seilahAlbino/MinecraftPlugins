package com.me.cp.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.me.cp.Main.Main;

public class CommandGiveCP implements CommandExecutor {

	static Plugin plugin = Main.getPlugin();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
				
			Player p = (Player) sender;
			
			if (cmd.getName().equalsIgnoreCase("givecp")) {
				if (p.hasPermission("givecp.perm")) {
				if(args.length < 2) {
					p.sendMessage(plugin.getConfig().getString("Messages.CmdUsage").replace("&", "§"));
					return false;
				}
				
				Player target = Bukkit.getPlayer(args[0]);
				
				if(target == null) {
					p.sendMessage(plugin.getConfig().getString("Messages.IsntPlayer").replace("&", "§"));
					return true;
				}
				
				int amount;
				
				try {
					amount = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					p.sendMessage(plugin.getConfig().getString("Messages.OnlyNumbers").replace("&", "§"));
					return true;
				}

				if (amount < 0) {
					p.sendMessage(plugin.getConfig().getString("Messages.OnlyPositiveNumbers").replace("&", "§"));
					return true;
				}
				
				@SuppressWarnings("deprecation")
				ItemStack cp = new ItemStack(Material.getMaterial(plugin.getConfig().getInt("cpItemStack.itemid")), amount, (short) plugin.getConfig().getInt("cpItemStack.itemdata"));
				ItemMeta cpMeta = cp.getItemMeta();
				
				cpMeta.setDisplayName(plugin.getConfig().getString("cpItemStack.name").replace("&", "§"));
				List<String> lore = new ArrayList<String>();
				for(String lr : plugin.getConfig().getStringList("cpItemStack.lore")) {
					lore.add(lr.replace("&", "§"));
				}
				cpMeta.setLore(lore);
				
				cp.setItemMeta(cpMeta);
				p.getInventory().addItem(cp);
				p.sendMessage(plugin.getConfig().getString("Messages.GiveCP").replace("{player}", target.getName()).replace("{amount}", Integer.toString(amount)).replace("&", "§"));
			}else {
				p.sendMessage(plugin.getConfig().getString("Messages.NoPerm").replace("&", "§"));
				return false;
			}
		}
		
		return false;
	}

}
