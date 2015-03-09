package net.mcviral.dev.plugins.notifier.main;

import java.util.LinkedList;
import java.util.UUID;

import net.mcviral.dev.plugins.notifier.menu.IconMenu;

public class NotificationStore {
	
	private UUID uuid = null;
	private LinkedList <Notification> notifications = new LinkedList <Notification> ();
	private IconMenu menu = null;
	
	public NotificationStore(UUID id, LinkedList <Notification> notif, IconMenu m){
		uuid = id;
		notifications = notif;
		menu = m;
	}
	
	public UUID getPlayerUUID(){
		return uuid;
	}
	
	public void setNotifications(LinkedList <Notification> notif){
		notifications =  notif;
	}
	
	public LinkedList <Notification> getNotifications(){
		return notifications;
	}
	
	public IconMenu getIconMenu(){
		return menu;
	}
	
}
