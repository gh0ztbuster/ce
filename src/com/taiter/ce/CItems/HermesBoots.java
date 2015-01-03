package com.taiter.ce.CItems;

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


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.taiter.ce.Main;


public class HermesBoots extends CItem {
	
	int SpeedLevel;

	public HermesBoots(String originalName, ChatColor color, String lDescription, long lCooldown, Material mat) {
		super(originalName, color, lDescription, lCooldown, mat);
		this.configEntries.add("SpeedLevel: 5");
	}

	@Override
	public boolean effect(Event event, Player player) {
		if(Main.tools.repeatPotionEffects)
			  Main.tools.repeatPotionEffect(player.getInventory().getBoots(), player, PotionEffectType.SPEED, SpeedLevel, this);
		else 
			 player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) this.cooldownTime + 20, SpeedLevel, true), true);
		return true;
	}

	@Override
	public void initConfigEntries() {
		SpeedLevel = Integer.parseInt(getConfig().getString("Items." + getOriginalName() + ".SpeedLevel")) - 1;
	}

}
