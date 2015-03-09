package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
	
	public void setNotificationsForPlayer(UUID uuid, LinkedList <Notification> notifications){
		if (folderExists(notifier.getDataFolder() + "/Notifications/")){
			notifier.log.info("Notifications folder found.");
			FileManager fm = null;
			File f = new File(notifier.getDataFolder() + "/Notifications/" + uuid.toString() + ".yml");
			if (f.exists()){
				f.delete();
				notifier.createYaml(notifier.getDataFolder() + "/Notifications/" + uuid.toString());
				fm = new FileManager(notifier, "/Notifications/", uuid.toString());
				int count = notifications.size();
				int i = 1;
				fm.getYAML().set("count", count);
				if (notifications.size() > 0){
					List <String> desc = null;
					for (Notification n : notifications){
						try{
							fm.getYAML().set(i + ".name", n.getName());
							desc = Arrays.asList(n.getDescription());
							fm.getYAML().set(i + ".description", desc);
							fm.getYAML().set(i + ".material", n.getMaterial());
							fm.getYAML().set(i + ".offline", n.getOffline());
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				fm.saveYAML();
				notifier.log.info("Notifications saved for " + uuid.toString());
			}
		}else{
			notifier.log.info("No notifications file found.");
			return;
		}
	}
	
	public LinkedList <Notification> getNotificationsForPlayer(UUID uuid){
		notifier.log.info("Getting notifications for: " + uuid);
		if (folderExists(notifier.getDataFolder() + "/Notifications/")){
			notifier.log.info("Notifications folder found.");
			FileManager fm = null;
			File f = new File(notifier.getDataFolder() + "/Notifications/" + uuid.toString() + ".yml");
			if (f.exists()){
				fm = new FileManager(notifier, "/Notifications/", uuid.toString());
				int count = fm.getYAML().getInt("count");
				if (count > 0){
					notifier.log.info("Notifications count is greater than 0.");
					LinkedList <Notification> notifications = new LinkedList <Notification> ();
					Notification temp = null;
					String name = null;
					String[] desc = null;
					String mat = null;
					boolean offline = false;
					for(int i=1; i<(count + 1); i++){
						notifier.log.info("Loading notification " + i + ".");
						temp = null;
						name = fm.getYAML().getString(i + ".name");
						desc = fm.getYAML().getStringList(i + ".description").toArray(new String[0]);
						if (fm.getYAML().getString(i + ".material") != null){
							mat = fm.getYAML().getString(i + ".material");
						}else{
							mat = null;
						}
						offline = fm.getYAML().getBoolean(i + ".offline");
						notifier.log.info("Notification " + i);
						notifier.log.info("Name: " + name);
						notifier.log.info("Description: " + desc.toString());
						notifier.log.info("Material: " + mat);
						notifier.log.info("Offline: " + offline);
						temp = new Notification(uuid, name, desc, mat, offline);
						notifications.add(temp);
	        		}
					return notifications;
				}else{
					notifier.log.info("Count field not found in file.");
				}
			}else{
				notifier.log.info("No notifications file found.");
				return null;
			}
		}
		return null;
	}
	
	public void loadNotificationsForPlayer(Player p, IconMenu m){
		notifier.log.info("Loading notifications for player: " + p.getName() + " into memory...");
		for (NotificationStore store : stores){
			if (store.getPlayerUUID().equals(p.getUniqueId())){
				notifier.log.info("Notifications for " + p.getName() + " alread stored.");
				return;
			}
		}
		LinkedList <Notification> notifications = null;
		notifications = getNotificationsForPlayer(p.getUniqueId());
		stores.add(new NotificationStore(p.getUniqueId(), notifications, m));
		notifier.log.info("Notifications stored.");
	}
	
	public void unloadNotificationsForPlayer(Player p){
		for (NotificationStore store : stores){
			if (store.getPlayerUUID().equals(p.getUniqueId())){
				stores.remove(store);
				notifier.log.info("Store for " + p.getName() + " unloaded.");
				return;
			}
		}
	}
	
	public void displayNotifications(Player p){
		notifier.log.info("Starting display of notifications for " + p.getName());
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
				notifier.log.info("Too many notifications to load into menu.");
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
			fm = new FileManager(notifier, "/Notifications/", p.getUniqueId().toString());
			int count =  fm.getYAML().getInt("count");
			notifier.log.info("Count: " + count);
			return count;
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
