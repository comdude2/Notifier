package net.mcviral.dev.plugins.notifier.main;

import java.util.LinkedList;
import java.util.UUID;

public class NotificationStore {
	
	private UUID uuid = null;
	private LinkedList <Notification> notifications = new LinkedList <Notification> ();
	
	public NotificationStore(UUID id, LinkedList <Notification> notif){
		uuid = id;
		notifications = notif;
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
	
}
