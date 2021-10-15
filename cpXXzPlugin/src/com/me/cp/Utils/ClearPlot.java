package com.me.cp.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.me.cp.Main.Main;

public class ClearPlot {
	
	static Plugin plugin = Main.getPlugin();
	
	public static void Clearplot(Plot plot, List<String> list) {
		
		Player p = Bukkit.getPlayer(list.get(0));
		World world = Bukkit.getWorld(plot.getWorldName());
		
		Location top = plot.getTopAbs();
		Location bottom = plot.getBottomAbs();
		
		int minX = bottom.getX();
		int maxX = top.getX();
		
		int minZ = bottom.getZ();
		int maxZ = top.getZ();
		
		int maxY = Integer.parseInt(list.get(1));

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			int x = minX;
			int z = minZ;
			boolean task = true;
			int y = maxY;

			public void run() {
				if(task == true) {
						for(int i = 0; i < maxZ-minZ+1; i++) {
							for(String material : plugin.getConfig().getStringList("ItemClearOnCP.BlockMaterials")) {
								if(world.getBlockAt(x, y, z+i).getType() == Material.getMaterial(material)) {
									world.getBlockAt(x, y, z+i).setType(Material.AIR);
								}	
							}
						}
							x++;
							if (x > maxX) {
								x = minX;
								z+= maxZ-minZ+1;
								if (z > maxZ) {
									z = minZ;
									y--;
									Main.map.get(plot).set(1, Integer.toString(y));
									if (y == 0) {
										if (p != null) {
											p.sendMessage(plugin.getConfig().getString("Messages.EndClearCP").replace("&", "§"));
										}
										task = false;									
									}
								}
							}
						}
				}
			}, 1L, 1L);	
		}
}
