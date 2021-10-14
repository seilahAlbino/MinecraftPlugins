package me.jaoXXz;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Eventos implements Listener{

	private static plugin plugin = (me.jaoXXz.plugin) Bukkit.getPluginManager().getPlugin("LuckyXXz");

	
	@EventHandler
	public void aoQuebrarBloco(BlockBreakEvent e) {
		Player p = (Player) e.getPlayer();
		boolean allowed = false;
		Material BlockMaterial = e.getBlock().getType();
		Block bloco = e.getBlock();
		int BlocoData = bloco.getState().getData().toItemStack().getDurability();
		
				
		for(String material : plugin.getConfig().getConfigurationSection("Blocks").getKeys(false))
		{
			if(material.equalsIgnoreCase(BlockMaterial.toString())) {
				if(BlocoData == plugin.getConfig().getInt("Blocks."+material+".Block_Data")) {

					for(String world : plugin.getConfig().getStringList("Blocks."+material+".WorldName")) {
						if(world.equalsIgnoreCase(p.getWorld().getName())) {
							allowed = true;
						}
					}
					
					if(allowed == false) return;
					int i = (int) plugin.getConfig().getInt("Blocks."+material+".Percentage");
	                Random random = new Random();

	                if (i >= random.nextInt(100)) {
	                	if(plugin.getConfig().getBoolean("Blocks."+material+".RewardMessage_Enable") == true) {
	                		p.sendMessage(plugin.getConfig().getString("Blocks."+material+".Reward_Message").replace("&", "§"));
	                	}
	                	
	                	if(plugin.getConfig().getBoolean("Blocks."+material+".RewardItem_Enable") == true) {
	                		p.getInventory().addItem(new ItemStack(Material.getMaterial(plugin.getConfig().getString("Blocks."+material+".Reward")), plugin.getConfig().getInt("Blocks."+material+".Reward_Amount"), (short) plugin.getConfig().getInt("Blocks."+material+".Reward_Data")));
	                	}
	                	
	                	if(plugin.getConfig().getBoolean("Blocks."+material+".RewardCommanddEnable") == true) {
	                		for(String cmd : plugin.getConfig().getStringList("Blocks."+material+".RewardCommands")) {
	        					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("%p%", p.getName()));
	        				}
	                	}
	                }
				
				}
			}
		}	
	}
}