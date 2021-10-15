package com.me.cp.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;

import com.me.cp.Main.Main;
import com.me.cp.Utils.ClearPlot;

public class ClickInventory implements Listener {

	static Plugin plugin = Main.getPlugin();
	PlotAPI api = new PlotAPI();

	@EventHandler
	public void inventoryClick(PlayerInteractEvent e){
		
		Player p = (Player) e.getPlayer();
		Plot plot = api.getPlot(p); 
		ItemStack iteminhand = p.getItemInHand();

		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){			
			if(iteminhand != null) {
				if(iteminhand.hasItemMeta()) {
					if(iteminhand.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("cpItemStack.name").replace("&", "§"))) {
						if(plot != null) {
							if(plot.isAdded(p.getUniqueId()) || plot.isOwner(p.getUniqueId())) { 
								
								if (Main.map.containsKey(plot)) {
									p.sendMessage(plugin.getConfig().getString("Messages.BeingCleaned").replace("&", "§"));
									return;
								}
								
								List<String> list = new ArrayList<>();
								
								list.add(p.getName());
								list.add(Integer.toString(plugin.getConfig().getInt("ItemClearOnCP.StartY")));
								
							    Main.map.put(plot, list);
								
								if (p.getItemInHand().getAmount() > 1) {
									p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
								}else {
									p.getInventory().remove(p.getItemInHand());
								}
								p.sendMessage(plugin.getConfig().getString("Messages.ActivateCP").replace("&", "§"));
								ClearPlot.Clearplot(plot, list);
							}else {
								p.sendMessage(plugin.getConfig().getString("Messages.NotYourPlot").replace("&", "§"));
								return;
							}
						}
					}
				}
			}
		}
		
	}
	
}
