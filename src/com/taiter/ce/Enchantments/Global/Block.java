package com.taiter.ce.Enchantments.Global;

/*
* This file is part of Custom Enchantments
* Copyright (C) Taiterio 2015
*
* This program is free software: you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as published by the
* Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
* FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
* for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/



import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.taiter.ce.Enchantments.CEnchantment;



public class Block extends CEnchantment {

	int	strength;
	int	cooldown;

	public Block(Application app) {
		super(app);		
		configEntries.add("Strength: 1");
		configEntries.add("Cooldown: 600");
		triggers.add(Trigger.INTERACT_RIGHT);
	}

	@Override
	public void effect(Event e, ItemStack item, final int level) {
		PlayerInteractEvent event = (PlayerInteractEvent) e;
		final Player owner = event.getPlayer();

		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ANVIL_LAND, 10, 10);
		new BukkitRunnable() {

			PotionEffect	resistance	= new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, strength + level);

			@Override
			public void run() {
				if(owner.isBlocking()) {
					owner.addPotionEffect(resistance);
				} else {
					generateCooldown(owner, cooldown);
					this.cancel();
				}
			}
		}.runTaskTimer(getPlugin(), 0l, 15l);
	}

	@Override
	public void initConfigEntries() {

		strength = Integer.parseInt(getConfig().getString("Enchantments." + getOriginalName() + ".Strength"))-1;
		cooldown = Integer.parseInt(getConfig().getString("Enchantments." + getOriginalName() + ".Cooldown"));
		
	}
}
