package xyz.spaceio.spectatorafk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public static HashMap<Player, Location> locations = new HashMap<>();
	public static HashMap<Player, Integer> seconds = new HashMap<>();
	public static List<Player> afk = new ArrayList<>();
	
	@Override
	public void onDisable() {
		for(Player p : afk) {
			if(p.isOnline()) {
				p.setGameMode(GameMode.SURVIVAL);
				p.setWalkSpeed(0.2f);
				p.setFlySpeed(0.2f);
			}
		}
	}
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new Listeners(), this);
		
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			for(Player player : Bukkit.getOnlinePlayers()) {
				Location ploc = player.getLocation();
				if(locations.containsKey(player)) {
					if(ploc.getX() == locations.get(player).getX() &&
									ploc.getZ() == locations.get(player).getZ()) {
						int before = seconds.getOrDefault(player, 0);
						seconds.put(player, before + 1);
					}else {
						seconds.remove(player);
						if(afk.contains(player)) {
							removeFromAfk(player);
						}
					}
				}
				locations.put(player, ploc);
				
				int time = seconds.getOrDefault(player, 0);
				if(time >= 120 && !afk.contains(player) && !player.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.IRON_BLOCK)) {
					// put afk
					if(player.getName().equals("GiraffePain"))
						continue;
					afk.add(player);
					player.teleport(player.getLocation().add(0, 0.2, 0));
					player.setGameMode(GameMode.SPECTATOR);
					player.setWalkSpeed(0);
					player.setFlySpeed(0);
					player.sendMessage("§aDu bist nun AFK!");
				}
			}
		}, 20L, 20L);
	}
	
	public static void removeFromAfk(Player player) {
		player.setWalkSpeed(0.2f);
		player.setFlySpeed(0.2f);
		player.setGameMode(GameMode.SURVIVAL);
		Main.afk.remove(player);
		Main.seconds.remove(player);
		
		player.sendMessage("§aWelcome back!");
		
	}
}
