package com.taiter.ce.CItems;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;


public class RocketBoots extends CItem {

	String[] states = {	
			ChatColor.DARK_GRAY + "Power: " + ChatColor.RED	  + "" + ChatColor.ITALIC + "OFFLINE",
			ChatColor.DARK_GRAY + "Power: " + ChatColor.GREEN + "" + ChatColor.ITALIC + "ONLINE",
			ChatColor.RED + "Out of Fuel"
			};
	
	public RocketBoots(String originalName, ChatColor color, String lDescription, long lCooldown, Material mat) {
		super(originalName, color, lDescription, lCooldown, mat);
	}

	@Override
	public boolean effect(Event event, final Player player) {
		
		PlayerInteractEvent e = (PlayerInteractEvent) event;

		ItemStack rocketBoots = e.getPlayer().getInventory().getBoots();
		
		if(isRocketBoots(rocketBoots)) {
			final ItemMeta im = rocketBoots.getItemMeta();
			List<String> lore = im.getLore();
		if(lore.contains(states[1])) {
			if(e.getAction().toString().startsWith("RIGHT") && player.isSneaking()) {
				
				if (!player.getGameMode().equals(GameMode.CREATIVE)) {
					
					int currentDurability 	= rocketBoots.getDurability();
					int maxDurability 	 	= rocketBoots.getType().getMaxDurability() - 1;
					
					if(currentDurability == (maxDurability * 0.75)) {
						player.sendMessage(ChatColor.RED + "Fuel at 25%");
					} else if(currentDurability == (maxDurability * 0.9)) {
						player.sendMessage(ChatColor.RED + "Fuel at 10%");
					} else if(currentDurability == (maxDurability * 0.95)) {
						player.sendMessage(ChatColor.RED + "Fuel at 5%");
					}
					
					if(currentDurability == maxDurability) {
						
						lore.add(states[2]);
						lore.set(lore.indexOf(states[1]), states[0]);
						im.setLore(lore);
						player.sendMessage(ChatColor.GRAY + "Out of Fuel");
						player.updateInventory();
						player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, 0.2f, 0f);
						return false;
					} else {
						rocketBoots.setDurability((short) (rocketBoots.getDurability() + 1));
						player.updateInventory();
					}
				}
				
				
					player.setFallDistance(-10);
					player.setVelocity(player.getLocation().getDirection().setY(0.5));
					player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
					player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 40);
					player.getWorld().playSound(player.getLocation(), Sound.FIRE, 5f, 5f);
					if(player.getGameMode().equals(GameMode.CREATIVE)) 
						return false;
					
					final Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 1, player.getLocation().getZ());
					
					if(!player.hasMetadata("ce.flightControl")) {
					
						player.setMetadata("ce.flightControl", new FixedMetadataValue(main, null));
						
					new BukkitRunnable() {
						@Override
						public void run() {
							if(player.isFlying()) {
								player.setFlying(false);
							}
							if(loc.getBlock().getRelative(0,-1,0).getType().equals(Material.AIR) && !player.getAllowFlight()) {
								player.setAllowFlight(true);
							} else {
								player.setAllowFlight(false);
								player.removeMetadata("ce.flightControl", main);
								this.cancel();
							}
						}
					}.runTaskTimer(main, 0l, 10l);
					
					}
				}
			}
		} else {
			rocketBoots = player.getItemInHand();
			final ItemMeta im = rocketBoots.getItemMeta();
			List<String> lore = im.getLore();
			e.setCancelled(true);
			if(e.getAction().toString().startsWith("LEFT"))  {
			//Reload
			if(rocketBoots.getItemMeta().getLore().contains(states[2])) {
				addLock(player);
				player.getWorld().playEffect(player.getLocation(), Effect.CLICK1, 5);
				player.sendMessage(ChatColor.RED + "Reloading...");
				
				lore.remove(states[2]);
				im.setLore(lore);
				
				final ItemStack temp = rocketBoots;
				
				new BukkitRunnable() {
					
					ItemStack current = temp;
					
					@Override
					public void run() {
						ItemStack hand = player.getItemInHand();
						if(hand.equals(current)) {
							if(hand.getDurability() == 0) {
								removeLock(player);
								player.getWorld().playEffect(player.getLocation(), Effect.CLICK2, 1000);
								hand.setItemMeta(im);
								player.setItemInHand(hand);
								this.cancel();
							} else {
								hand.setDurability((short) (hand.getDurability() - 1));
								current = hand;
							}
						} else {
							removeLock(player);
							this.cancel();
						}
					}
				}.runTaskTimer(main, 0l, 5l);
				
			} else {
				
				String newStateMsg = ChatColor.GRAY + "Rocket Boots: ";
			
				if(lore.contains(states[0])) {
					newStateMsg += ChatColor.GREEN + "ONLINE";
					lore.set(lore.indexOf(states[0]), states[1]);
				} else if(lore.contains(states[1])) {
					newStateMsg += ChatColor.RED + "OFFLINE";
					lore.set(lore.indexOf(states[1]), states[0]);
				}
				
				im.setLore(lore);
				player.getItemInHand().setItemMeta(im);
				player.getWorld().playEffect(player.getLocation(), Effect.CLICK1, 5);
				player.sendMessage(newStateMsg);
					
			
			}	
				
			}
				
		}
			
		return false;
	}

	@Override
	public void initConfigEntries() {
		this.description.add(states[0]);

	}
	
	private boolean isRocketBoots(ItemStack item) {
		if(item != null && item.getType().toString().endsWith("BOOTS") && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && (item.getItemMeta().getDisplayName().equals(this.getDisplayName()) || item.getItemMeta().getDisplayName().equals(this.getOriginalName())) )
			return true;
		return false;
	}

}