package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import net.mcviral.dev.plugins.notifier.util.Log;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Notifier extends JavaPlugin{
	
	public Log log = new Log(this.getName());
	private NotificationManager notman = null;
	private Listeners listeners = null;
	private boolean loadedbefore = false;
	
	public void onEnable(){
		notman = new NotificationManager(this);
		this.saveDefaultConfig();
		if (loadedbefore == false){
			listeners = new Listeners(this, notman);
			this.getServer().getPluginManager().registerEvents(listeners, this);
		}
		this.getLogger().info(this.getDescription().getName() + " Enabled!");
	}
	
	public void onDisable(){
		this.getLogger().info(this.getDescription().getName() + " Disabled!");
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("notifications")) {
			if (args.length > 0){
				//other shit
				if (args.length == 1){
					
				}else if (args.length == 2){
					if (args[0].equalsIgnoreCase("clear")){
						
					}else{
						
					}
				}else if (args.length >= 6){
					if (args[0].equalsIgnoreCase("add")){
						if (sender.hasPermission("notifier.add")){
							boolean toall = false;
							String id = args[1];
							UUID uuid = null;
							if (id != "*"){
								Player player = null;
								for (Player p : this.getServer().getOnlinePlayers()){
									if (p.getName().equalsIgnoreCase(id)){
										player = p;
										break;
									}
								}
								if (player != null){
									uuid = player.getUniqueId();
								}else{
									//Offline
									OfflinePlayer oplayer = null;
									for (OfflinePlayer p : this.getServer().getOfflinePlayers()){
										if (p.getName().equalsIgnoreCase(id)){
											oplayer = p;
											break;
										}
									}
									if (oplayer != null){
										uuid = oplayer.getUniqueId();
									}else{
										//Not found
										sender.sendMessage(message(ChatColor.RED + "Player not found."));
									}
								}
							}else{
								toall = true;
							}
							String name = args[2];
							String mat = args[3];
							boolean offline;
							try{
								offline = Boolean.getBoolean(args[4]);
							}catch(Exception e){
								offline = false;
							}
							LinkedList <String> descv = new LinkedList <String> ();
							String[] desc = null;
							int i = 1;
							for (String s : args){
								if (i >= 5){
									descv.add(s);
								}
								i++;
							}
							desc = descv.toArray(new String[0]);
							if (toall == false){
								LinkedList <Notification> notifications = notman.getNotificationsForPlayer(uuid);
								if (notifications == null){
									createYaml(this.getDataFolder() + "/Notifications/" + uuid.toString());
									notifications = new LinkedList <Notification> ();
								}
								notifications.add(new Notification(uuid, name, desc, mat, offline));
								notman.setNotificationsForPlayer(uuid, notifications);
								sender.sendMessage(message("Notification added."));
							}else{
								//All players
								sender.sendMessage(message(ChatColor.RED + "This had not been implemented yet."));
							}
						}else{
							sender.sendMessage(message(ChatColor.RED + "You don't have permission to do this."));
						}
					}
				}else{
					
				}
			}else{
				log.info("Checking notifications for: " + sender.getName());
				//Check notifications
				if (sender instanceof Player){
					notman.displayNotifications((Player) sender);
				}else{
					sender.sendMessage(message(ChatColor.RED + "You need to be a player to perform this command!"));
				}
			}
			return true;
		}
		return false;
	}
	
	public String message(String message){
		return (ChatColor.YELLOW + "[" + ChatColor.AQUA + this.getDescription().getName() + ChatColor.YELLOW + "] " + ChatColor.GREEN + message);
	}
	
}
