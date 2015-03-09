package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener{
	
	private Notifier notifier = null;
	private NotificationManager notificationmanager = null;
	
	public Listeners(Notifier n, NotificationManager notman){
		notifier = n;
		notificationmanager = notman;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		//event.getPlayer().sendMessage("");
		File f = new File(notifier.getDataFolder() + "/Notifications/", event.getPlayer().getUniqueId().toString() + ".yml");
		if (!f.exists()){
			notifier.log.info("Notification file not found for " + event.getPlayer().getName() + ", creating one...");
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			notifier.log.info("Getting notification count for " + event.getPlayer().getName());
			int count = notificationmanager.getNotificationCountForPlayer(event.getPlayer());
			if (count == -1){
				//no notifications
			}
			if (count > 0){
				event.getPlayer().sendMessage(notifier.message("You have " + ChatColor.RED + count + " notifications waiting to be checked!"));
				event.getPlayer().sendMessage(notifier.message("To check them type " + ChatColor.YELLOW + "/notifications"));
			}else{
				//no notifications
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		if (notificationmanager.isHavingNotificationsDisplayed(event.getPlayer())){
			notificationmanager.unloadNotificationsForPlayer(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event){
		Player p = null;
		if (event.getPlayer() instanceof Player){
			p = (Player) event.getPlayer();
		}
		if (notificationmanager.isHavingNotificationsDisplayed(p)){
			notificationmanager.unloadNotificationsForPlayer(p);
		}
	}
	
}
