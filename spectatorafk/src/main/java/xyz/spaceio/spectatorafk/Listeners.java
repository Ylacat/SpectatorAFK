package xyz.spaceio.spectatorafk;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Listeners implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(Main.afk.contains(e.getPlayer())) {
			Main.removeFromAfk(e.getPlayer());
		}
	}
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(e.getCause().equals(TeleportCause.SPECTATE) && Main.afk.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if(Main.afk.contains(e.getPlayer())) {
			Main.removeFromAfk(e.getPlayer());
		}
	}
}
