package com.me.cp.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.me.cp.Commands.CommandGiveCP;
import com.me.cp.Events.ClickInventory;
import com.me.cp.Utils.ClearPlot;

public class Main extends JavaPlugin{
	
	private File file = null;
	private FileConfiguration fileConfiguration = null;
	public static Map<Plot, List<String>> map = new HashMap<Plot, List<String>>();

	@Override
	public void onEnable() {
		map.clear();
		File verificar = new File(getDataFolder(), "db.yml");
        if (!verificar.exists()) {
        	saveResource("db.yml", false);
    		getDB().set("Plots", 0);
    		saveDB();
        }
        
        	ConfigurationSection a = getDB().getConfigurationSection("Plots");
		if(a != null) {
	        for(String str : getDB().getConfigurationSection("Plots").getKeys(false)) {
	        	
	        	Location loc = new Location();
	        	loc.setX(getDB().getInt("Plots."+str+".Location.X"));
	        	loc.setY(getDB().getInt("Plots."+str+".Location.Y"));
	        	loc.setZ(getDB().getInt("Plots."+str+".Location.Z"));        	
	        	loc.setWorld(getDB().getString("Plots."+str+".Location.World"));
	        	
	        	int y = getDB().getInt("Plots."+str+".Y");
	        	Player p = Bukkit.getPlayer(getDB().getString("Plots."+str+".Player"));
	        	Plot plot = Plot.getPlot(loc);
	        	
	        	List<String> list = new ArrayList<>();
	        	list.add(p.getName());
	        	list.add(Integer.toString(y));
	        	
	        	map.put(plot, list);
	        }
		}

	    	for(Plot plot : map.keySet()) {
	    		ClearPlot.Clearplot(plot, map.get(plot));
	    	}
	    
		saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage("§acpXXz iniciado");
		getCommand("givecp").setExecutor(new CommandGiveCP());
		Bukkit.getPluginManager().registerEvents(new ClickInventory(), this);
	}

	@Override
	public void onDisable() {
		
        this.file = new File(getDataFolder(), "db.yml");
		this.file.delete();
		saveResource("db.yml", false);
		saveDB();

	    if(!map.isEmpty()) {	 
	    	
	        for(Plot plot : map.keySet()) {
	        
	        	int a = Integer.parseInt(map.get(plot).get(1));

	        	if(a <= 0) {
		    		getDB().set("Plots."+plot.toString(), null);
		    		saveDB();
	        	}else {
		    		getDB().set("Plots."+plot.toString()+".Player", map.get(plot).get(0));
		    		
		    		getDB().set("Plots."+plot.toString()+".Y", a);

		    		getDB().set("Plots."+plot.toString()+".Location.X", plot.getTopAbs().getX());
		    		getDB().set("Plots."+plot.toString()+".Location.Y", plot.getTopAbs().getY());
		    		getDB().set("Plots."+plot.toString()+".Location.Z", plot.getTopAbs().getZ());
		    		getDB().set("Plots."+plot.toString()+".Location.World", plot.getWorldName());
		    		saveDB();

	        	}
	        }
	    }
	}
	
	public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("cpXXz");
    }
	
	public FileConfiguration getDB() {
        if (this.fileConfiguration == null) {
            this.file = new File(getDataFolder(), "db.yml");
            this.fileConfiguration = (FileConfiguration) YamlConfiguration.loadConfiguration(this.file);
        }
        return this.fileConfiguration;
    }
	
	public void saveDB() {
        try {
            getDB().save(this.file);
        } catch (Exception exception) {
        }
    }
}
