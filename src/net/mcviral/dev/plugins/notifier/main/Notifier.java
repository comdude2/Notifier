package net.mcviral.dev.plugins.notifier.main;

import java.util.UUID;

import net.mcviral.dev.plugins.notifier.util.Log;

import org.bukkit.plugin.java.JavaPlugin;

public class Notifier extends JavaPlugin{
	
	public Log log = new Log(this.getName());
	
	public void onEnable(){
		
	}
	
	public void onDisable(){
		
	}
	
	public boolean newNotification(UUID uuid){
		return false;
	}
	
}
