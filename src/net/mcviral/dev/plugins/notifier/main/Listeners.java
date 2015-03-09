package net.mcviral.dev.plugins.notifier.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener{
	
	@SuppressWarnings("unused")
	private Notifier notifier = null;
	
	public Listeners(Notifier n){
		notifier = n;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		//event.getPlayer().sendMessage("");
		
	}
	
}
