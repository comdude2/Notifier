package net.mcviral.dev.plugins.notifier.main;

import java.util.UUID;

public class Notification {
	
	private UUID uuid = null;
	private String name = null; //the name of the notification
	private String[] description = null; //the description (lore)
	private boolean offline = false;
	
	public Notification(UUID id, String n, String[] desc, boolean canbeoffline){
		uuid = id;
		name = n;
		description = desc;
		offline = canbeoffline;
	}
	
}
