package me.jaoXXz;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class plugin extends JavaPlugin{
	
	public void onEnable(){
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(new Eventos(), this);
		Bukkit.getConsoleSender().sendMessage("§aLuckyXXz iniciado");
	}
}
