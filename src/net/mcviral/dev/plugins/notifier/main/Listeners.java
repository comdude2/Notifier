package net.mcviral.dev.plugins.notifier.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
