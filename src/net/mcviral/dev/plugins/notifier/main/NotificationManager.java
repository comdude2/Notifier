package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import net.mcviral.dev.plugins.notifier.menu.IconMenu;
import net.mcviral.dev.plugins.notifier.util.FileManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NotificationManager {
	
	private Notifier notifier = null;
	private LinkedList <NotificationStore> stores = new LinkedList <NotificationStore> ();
	
	public NotificationManager(Notifier n){
		notifier = n;
		if (!folderExists(notifier.getDataFolder() + "/Notifications/")){
			createFolder(notifier.getDataFolder() + "/Notifications/");
		}
	}
	
	public LinkedList <Notification> getNotificationsForPlayer(UUID uuid){
		if (folderExists(notifier.getDataFolder() + "/Notifications/")){
			FileManager fm = null;
			fm = new FileManager(notifier, notifier.getDataFolder() + "/Notifications/", uuid.toString());
			int count = fm.getYAML().getInt("count");
			LinkedList <Notification> notifications = new LinkedList <Notification> ();
			Notification temp = null;
			String name = null;
			String[] desc = null;
			String mat = null;
			boolean offline = false;
			for(int i=1; i<count; i++){
				temp = null;
				name = fm.getYAML().getString(i + ".notification.name");
				desc = (String[]) fm.getYAML().getStringList(i + ".notification.desc").toArray();
				if (fm.getYAML().getString(i + ".notification.material") != null){
					mat = fm.getYAML().getString(i + ".notification.material");
				}else{
					mat = null;
				}
				offline = fm.getYAML().getBoolean(i + ".notification.offline");
				temp = new Notification(uuid, name, desc, mat, offline);
				notifications.add(temp);
	        }
			return notifications;
		}
		return null;
	}
	
	public void loadNotificationsForPlayer(Player p, IconMenu m){
		for (NotificationStore store : stores){
			if (store.getPlayerUUID().equals(p.getUniqueId())){
				return;
			}
		}
		LinkedList <Notification> notifications = null;
		notifications = getNotificationsForPlayer(p.getUniqueId());
		stores.add(new NotificationStore(p.getUniqueId(), notifications, m));
	}
	
	public void unloadNotificationsForPlayer(Player p){
		for (NotificationStore store : stores){
			if (store.getPlayerUUID().equals(p.getUniqueId())){
				stores.remove(store);
				return;
			}
		}
	}
	
	public void displayNotifications(Player p){
		LinkedList <Notification> notifications = getNotificationsForPlayer(p.getUniqueId());
		int size = -1;
		if (notifications != null){
			if (notifications.size() < 54){
				if (notifications.size() > 0){
					if (notifications.size() < 9){
						//display
						size = 9;
					}else{
						if (notifications.size() < 18){
							//display
							size = 18;
						}else{
							if (notifications.size() < 27){
								//display
								size = 27;
							}else{
								if (notifications.size() < 36){
									//display
									size = 36;
								}else{
									if (notifications.size() < 45){
										//display
										size = 45;
									}else{
										//Display at size of 54 as it's already been tested against.
										size = 54;
									}
								}
							}
						}
					}
				}else{
					//Lower than or equal to 0
					return;
				}
				IconMenu menu = new IconMenu("Notifications - " + p.getName(), size, new IconMenu.OptionClickEventHandler() {
			    @Override
			    public void onOptionClick(IconMenu.OptionClickEvent event) {}
			    }, notifier);
				int i = 0;
				//Add support for coloured notifications
			    for (Notification n : notifications){
			    	menu.setOption(i, new ItemStack(Material.getMaterial(n.getMaterial()), 1), ChatColor.GREEN + n.getName(), n.getDescription());
			    	i++;
			    }
			    loadNotificationsForPlayer(p, menu);
				menu.open(p);
			}else{
				//Notifications are above maximum inventory size, this needs to be handled ASAP
			}
		}
	}
	
	public void wipeNotificationsForPlayer(Player p){
		File f = new File(notifier.getDataFolder() + "/Notifications/", p.getUniqueId().toString() + ".yml");
		if (f.exists()){
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (NotificationStore store : stores){
			if (store.getPlayerUUID().equals(p.getUniqueId())){
				stores.remove(store);
			}
		}
	}
	
	public void addNotificationToAll(Notification n){
		File folder = new File(notifier.getDataFolder() + "/Notifications/");
		File[] listOfFiles = folder.listFiles();
		LinkedList <File> files = new LinkedList <File> ();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().endsWith(".yml")){
					files.add(listOfFiles[i]);
				}
			}
		}
		LinkedList <Notification> notifications = null;
		for (File f : files){
			try{
				notifications = getNotificationsForPlayer(UUID.fromString(f.getName().substring(0, f.getName().length() - 5)));
				if (notifications != null){
					notifications.add(n);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//MENU NEEDS UPDATING AFTER THIS!!!!!
		
		for (NotificationStore store : stores){
			store.getNotifications().add(n);
			Player p = notifier.getServer().getPlayer(store.getPlayerUUID());
			if (p != null){
				p.closeInventory();
				//Create new menu
			}else{
				//Failed to get player to update inventory
			}
		}
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
	
	public boolean isHavingNotificationsDisplayed(Player p){
		for (NotificationStore store : stores){
			if(store.getPlayerUUID().equals(p.getUniqueId())){
				return true;
			}
		}
		return false;
	}
	
}
