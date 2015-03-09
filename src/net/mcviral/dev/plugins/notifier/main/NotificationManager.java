package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.util.LinkedList;

import org.bukkit.entity.Player;

public class NotificationManager {
	
	private Notifier notifier = null;
	
	public NotificationManager(Notifier n){
		notifier = n;
		if (!folderExists(notifier.getDataFolder() + "/Notifications/")){
			createFolder(notifier.getDataFolder() + "/Notifications/");
		}
	}
	
	public LinkedList <Notification> getNotificationsForPlayer(Player p){
		if (folderExists(notifier.getDataFolder() + "/Notifications/" + p.getUniqueId().toString())){
			
		}
		return null;
	}
	
	public void newNotification(){
		
	}
	
	public void createFolder(String path){
		File f = new File(path);
		f.mkdirs();
	}
	
	public boolean folderExists(String path){
		File f = new File(path);
		if (f.exists()){
			return true;
		}
		return false;
	}
	
}
