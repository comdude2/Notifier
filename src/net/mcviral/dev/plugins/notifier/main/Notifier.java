package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.io.IOException;

import net.mcviral.dev.plugins.notifier.util.Log;

import org.bukkit.plugin.java.JavaPlugin;

public class Notifier extends JavaPlugin{
	
	public Log log = new Log(this.getName());
	private NotificationManager notman = null;
	
	public void onEnable(){
		notman = new NotificationManager(this);
	}
	
	public void onDisable(){
		
	}
	
	public NotificationManager getNotificationManager(){
		return notman;
	}
	
	public void createYaml(String name){
		try{ 
			boolean isCreated = false;
			log.info("Creating file: " + name + ".yml");
			File file = new File(name + ".yml"); 
			log.info("Created file already exists: " + file.exists()); 
			isCreated = file.createNewFile(); 
			log.info("Created file created: " + isCreated); 
			log.info("Created file exists: " + file.exists());
		}catch(IOException e){ 
			e.printStackTrace(); 
		}
	}
	
}
