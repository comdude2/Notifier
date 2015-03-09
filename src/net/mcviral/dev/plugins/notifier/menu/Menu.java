package net.mcviral.dev.plugins.notifier.menu;

import java.util.UUID;

@Deprecated
public class Menu {
	
	private UUID uuid = null;
	private IconMenu iconmenu = null;
	
	public Menu(UUID id, IconMenu icmenu){
		uuid = id;
		iconmenu = icmenu;
	}
	
	public UUID getPlayerUUID(){
		return uuid;
	}
	
	public void setIconMenu(IconMenu icmenu){
		iconmenu = icmenu;
	}
	
	public IconMenu getIconMenu(){
		return iconmenu;
	}
	
}
