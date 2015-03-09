package net.mcviral.dev.plugins.notifier.main;

import java.util.UUID;

public class Notification {
	
	private UUID uuid = null;
	private String name = null; //the name of the notification
	private String[] description = null; //the description (lore)
	private String material = null;
	private boolean offline = false;
	
	public Notification(UUID id, String n, String[] desc, boolean canbeoffline){
		uuid = id;
		name = n;
		description = desc;
		offline = canbeoffline;
	}
	
	public Notification(UUID id, String n, String[] desc, String mat, boolean canbeoffline){
		uuid = id;
		name = n;
		description = desc;
		material = mat;
		offline = canbeoffline;
	}
	
	public UUID getPlayerUUID(){
		return uuid;
	}
	
	public String getName(){
		return name;
	}
	
	public String[] getDescription(){
		return description;
	}
	
	public String getMaterial(){
		if (material == null){
			return "WOOL";
		}else{
			return material;
		}
	}
	
	public boolean getOffline(){
		return offline;
	}
	
}
