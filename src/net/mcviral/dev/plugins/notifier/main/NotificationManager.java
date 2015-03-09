package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.util.LinkedList;

import net.mcviral.dev.plugins.notifier.util.FileManager;

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
		if (folderExists(notifier.getDataFolder() + "/Notifications/")){
			FileManager fm = null;
			fm = new FileManager(notifier, notifier.getDataFolder() + "/Notifications/", p.getUniqueId().toString());
			int count = fm.getYAML().getInt("count");
			LinkedList <Notification> notifications = new LinkedList <Notification> ();
			Notification temp = null;
			String name = null;
			String[] desc = null;
			boolean offline = false;
			for(int i=1; i<count; i++){
				temp = null;
				name = fm.getYAML().getString(i + ".notification.name");
				desc = (String[]) fm.getYAML().getStringList(i + ".notification.desc").toArray();
				offline = fm.getYAML().getBoolean(i + ".notification.offline");
				temp = new Notification(p.getUniqueId(), name, desc, offline);
				notifications.add(temp);
	        }
			return notifications;
		}
		return null;
	}
	
	public int getNotificationCountForPlayer(Player p){
		if (folderExists(notifier.getDataFolder() + "/Notifications/")){
			FileManager fm = null;
			fm = new FileManager(notifier, notifier.getDataFolder() + "/Notifications/", p.getUniqueId().toString());
			return fm.getYAML().getInt("count");
		}
		return -1;
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
