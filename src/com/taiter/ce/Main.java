package com.taiter.ce;

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


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.taiter.ce.CItems.AssassinsBlade;
import com.taiter.ce.CItems.Bandage;
import com.taiter.ce.CItems.BearTrap;
import com.taiter.ce.CItems.BeastmastersBow;
import com.taiter.ce.CItems.CItem;
import com.taiter.ce.CItems.Deathscythe;
import com.taiter.ce.CItems.DruidBoots;
import com.taiter.ce.CItems.Flamethrower;
import com.taiter.ce.CItems.HealingShovel;
import com.taiter.ce.CItems.HermesBoots;
import com.taiter.ce.CItems.HookshotBow;
import com.taiter.ce.CItems.Landmine;
import com.taiter.ce.CItems.LivefireBoots;
import com.taiter.ce.CItems.Medikit;
import com.taiter.ce.CItems.Minigun;
import com.taiter.ce.CItems.NecromancersStaff;
import com.taiter.ce.CItems.PiranhaTrap;
import com.taiter.ce.CItems.PoisonIvy;
import com.taiter.ce.CItems.PotionLauncher;
import com.taiter.ce.CItems.Powergloves;
import com.taiter.ce.CItems.PricklyBlock;
import com.taiter.ce.CItems.Pyroaxe;
import com.taiter.ce.CItems.RocketBoots;
import com.taiter.ce.CItems.ThorsAxe;
import com.taiter.ce.Enchantments.CEnchantment;
import com.taiter.ce.Enchantments.CEnchantment.Application;
import com.taiter.ce.Enchantments.CEnchantment.Cause;
import com.taiter.ce.Enchantments.Armor.Enlighted;
import com.taiter.ce.Enchantments.Armor.Frozen;
import com.taiter.ce.Enchantments.Armor.Hardened;
import com.taiter.ce.Enchantments.Armor.Molten;
import com.taiter.ce.Enchantments.Armor.Poisoned;
import com.taiter.ce.Enchantments.Boots.Gears;
import com.taiter.ce.Enchantments.Boots.Springs;
import com.taiter.ce.Enchantments.Boots.Stomp;
import com.taiter.ce.Enchantments.Bow.Bombardment;
import com.taiter.ce.Enchantments.Bow.Firework;
import com.taiter.ce.Enchantments.Bow.Lightning;
import com.taiter.ce.Enchantments.Global.Autorepair;
import com.taiter.ce.Enchantments.Global.Block;
import com.taiter.ce.Enchantments.Global.Cripple;
import com.taiter.ce.Enchantments.Global.Deathbringer;
import com.taiter.ce.Enchantments.Global.Deepwounds;
import com.taiter.ce.Enchantments.Global.Gooey;
import com.taiter.ce.Enchantments.Global.Ice;
import com.taiter.ce.Enchantments.Global.Lifesteal;
import com.taiter.ce.Enchantments.Global.Poison;
import com.taiter.ce.Enchantments.Global.Shockwave;
import com.taiter.ce.Enchantments.Global.Thunderingblow;
import com.taiter.ce.Enchantments.Global.Vampire;
import com.taiter.ce.Enchantments.Helmet.Glowing;
import com.taiter.ce.Enchantments.Helmet.Implants;
import com.taiter.ce.Enchantments.Tools.Explosive;
import com.taiter.ce.Enchantments.Tools.Smelting;



public final class Main extends JavaPlugin {
	
	
	public static Plugin 			plugin;
	public static FileConfiguration config;
	public static Tools 			tools;
	public static CEListener        listener;
	public static CeCommand         commandC;

	
	
	public static String 			lorePrefix;
	public static List<CItem> 		items;
	public static List<CEnchantment>enchantments;
	public static int 				maxEnchants = -1;
	
	
	//The inventories for the Enchantment Menu
	public static Inventory 		CEMainMenu;
	
	public static Inventory 		CEEnchantmentMainMenu;
	public static Inventory 		CEItemMenu;
	public static Inventory 		CEConfigMenu;
	
	public static Inventory 		CEArmorMenu;
	public static Inventory 		CEBootsMenu;
	public static Inventory 		CEBowMenu;
	public static Inventory 		CEGlobalMenu;
	public static Inventory 		CEHelmetMenu;
	public static Inventory 		CEToolMenu;
	
	public static Inventory 		CEEnchantingMenu;
	public static Inventory 		CEApproveMenu;
	//

	

	//Economy
	public  static Economy 			econ 			= null;
	public  static Boolean 			hasEconomy 		= false;
	public  static Plugin 			econPl;
	//
	
	//Updater
	private URL updateListURL;
	private URL updateDownloadURL;
	
	private String currentVersion;
	private String newVersion;
	private String newMD5;
	
	public Boolean hasUpdate  = false;
	public Boolean hasChecked = false;
	//
	
    @Override
    public void onEnable(){
    	    
    	plugin		= this;
    	commandC	= new CeCommand(this);
    	
    	items		 = new ArrayList<CItem>();
    	enchantments = new ArrayList<CEnchantment>();
    	
        
    	this.saveDefaultConfig();
    	config = this.getConfig();
    	config.options().copyDefaults(true);
    	this.saveConfig();
    	
    	//This needs to be created after the config is created (See the variables RepeatDelay and RepeatPotionEffects)
    	tools = new Tools();
    	
    	if(config.contains("enchantments"))
    		tools.convertOldConfig();
    	
    	initializeListener();
    	
    	//Get the maximum amount of Enchantments on an Item
    	maxEnchants = Integer.parseInt(config.getString("Global.Enchantments.MaximumCustomEnchantments"));
    	
    	//Set the Loreprefix
    	lorePrefix = tools.resolveEnchantmentColor();
	    
    	//Check and set up the Economy
    	if(setupEconomy()) 
	    	hasEconomy = true;
    	
    	//Make the list of Enchantments
    	makeLists(true, true);
    
    	writePermissions();
    
    	tools.generateInventories();
    
    	currentVersion = plugin.getDescription().getVersion();
    	try {
    		updateListURL  = new URL("https://api.curseforge.com/servermods/files?projectIds=54406");
    	} catch (MalformedURLException e) {}
    
    	if(Boolean.parseBoolean(config.getString("Global.Updates.CheckOnStartup"))) {
    		new BukkitRunnable() {
    			@Override
    			public void run() {
	    			if(!hasChecked)
	    				updateCheck();
	    		}
    		}.runTaskLater(plugin, Integer.parseInt(config.getString("Global.Updates.CheckDelay")));
    	}

    }
    
    @Override
    public void onDisable() {
    	getServer().getScheduler().cancelAllTasks();
    }
    
    public void updateCheck() {
    	try {
    		
    		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] Checking for updates...");
    		
    		URLConnection connection = updateListURL.openConnection();
    		connection.setConnectTimeout(5000);
    		connection.addRequestProperty("User-Agent", "Custom Enchantments - Update Checker");
    		connection.setDoOutput(true);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    		String response = reader.readLine();
    		JSONArray array = (JSONArray) JSONValue.parse(response);
    		JSONObject newestUpdate = (JSONObject) array.get(array.size() - 1);
    	
    		newVersion = newestUpdate.get("name").toString().replace("Custom Enchantments ", "").trim();
    		newMD5     = newestUpdate.get("md5").toString();
    		
    		int newLength     = newVersion.length();
    		int currentLength = currentVersion.length();

    		
    		double versionNew;
    		double versionCurrent;
    		
    		Boolean newHasSubVersion 		= false;
    		Boolean currentHasSubVersion 	= false;

    		try {
    			versionNew = Double.parseDouble(newVersion);
    		} catch(NumberFormatException ex) {
    			newHasSubVersion = true;
    			versionNew = Double.parseDouble(newVersion.substring(0, newVersion.length()-1));
    		}
    		

    		try {
    			versionCurrent = Double.parseDouble(currentVersion);
    		} catch(NumberFormatException ex) {
    			currentHasSubVersion = true;
    			versionCurrent = Double.parseDouble(currentVersion.substring(0, currentVersion.length()-1));
    		}
    		
    		if( ( versionNew > versionCurrent ) 
    			|| ((versionNew == versionCurrent) && newHasSubVersion && currentHasSubVersion && ((byte) newVersion.toCharArray()[newLength-1] > (byte) currentVersion.toCharArray()[currentLength-1])) ) {
    			hasUpdate = true;
    			updateDownloadURL = new URL(newestUpdate.get("downloadUrl").toString().replace("\\.", ""));
    			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] A new update is available!");
    			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] The new version is " + ChatColor.AQUA +  newVersion + ChatColor.GREEN + ".");
    			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] You are currently using " + ChatColor.AQUA + currentVersion + ChatColor.GREEN + ".");
    			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] You can use '/ce update applyupdate' to update automatically.");

    		} else {
    			hasUpdate = false;
    			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] You are using the latest Version of CE!");
    		}
    		hasChecked = true;
    	} catch (IOException ioex) {
    		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] Failed to check for updates");
    	}
    	
    }
    
    public void update() {
    	
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] Updating to Version " + newVersion  + " started");
    	
    	BufferedInputStream input = null;
    	FileOutputStream   output = null;
    	    	
    	try {
    		
        	Boolean notify = Boolean.parseBoolean(config.getString("Global.Updates.UpdateNotifications"));

    		int updateSize = updateDownloadURL.openConnection().getContentLength();
    		File file = new File(plugin.getDataFolder().getParent(), "CustomEnchantments.jar");
    		input = new BufferedInputStream(updateDownloadURL.openStream());
    		output = new FileOutputStream(file);
    		
    		
    		
    		int bufferSize = (int) Math.ceil(updateSize / 100);
    		
    		byte[] data = new byte[bufferSize];
    		
    		int downloaded = 0;
    		int cRead;
    		
    		while ((cRead = input.read(data, 0, bufferSize)) != -1) {
    			
    			output.write(data, 0, cRead);
    			
    			downloaded += cRead;
    			
    			if(notify) {
    				int percentage = ((downloaded*100)/updateSize);
    				if(percentage % 25 == 0)
    					Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] Downloaded " + percentage + "% (" + downloaded + "/" + updateSize + " Bytes).");
    			}
    		}
    		
    		testFile(file, bufferSize);

    		
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CE] Update " + newVersion +  " successfully downloaded. Restart/Reload the Server to apply changes.");
			currentVersion = newVersion;
			hasUpdate = false;
    		
			
    		input.close();
    		output.close();
    		
    		
    	} catch(Exception e) {
    		try {
    		if(input != null)
    			input.close();
    		if(output != null)
    			output.close();
    		} catch (IOException ex){}
    		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CE] Updating to Version " + newVersion + " failed");
    	}
    }
    

    private boolean testFile(File f, int bufferSize) throws Exception {
    	
        InputStream fis =  new FileInputStream(f);

        byte[] buffer = new byte[bufferSize];
        MessageDigest md = MessageDigest.getInstance("MD5");
        int cRead;

        while((cRead = fis.read(buffer)) != -1)
        	md.update(buffer, 0, cRead);

        fis.close();
        
        byte[] result = md.digest();
        
        String md5 = "";

        for (int i=0; i < result.length; i++)
        	md5 += Integer.toString( ( result[i] & 0xff ) + 0x100, 16).substring( 1 );
                
        if(md5.equals(newMD5))
        	return true;
        
        return false;
    }
    
    public static WorldGuardPlugin getWorldGuard() {
    	Plugin worldguard = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if(worldguard != null && worldguard instanceof WorldGuardPlugin && worldguard.isEnabled()) 
			return (WorldGuardPlugin) worldguard;
		return null;
    }
    
    public void initializeListener() {
    	
    	listener = new CEListener();
    	
    	//Register the events
	    getServer().getPluginManager().registerEvents(listener, this);
	    
	    
	    //Unregister unused events
	    
	    	//EnchantItemEvent may be used
	    	if(!getConfig().getBoolean("Global.Enchantments.CEnchantmentTable"))
	    		EnchantItemEvent.getHandlerList().unregister(listener);
	    	
	    	//These are taking up ressources unnecessarily
	    	//BlockExpEvent.getHandlerList().unregister(listener);
	    	PlayerDeathEvent.getHandlerList().unregister(listener);
	    	EntityDeathEvent.getHandlerList().unregister(listener);
    	
    }
    
    private void writePermissions() {
    	Permission mainNode = new Permission("ce.*", "The main permission node for Custom Enchantments.", PermissionDefault.OP);
    	
    	Permission cmdNode  = new Permission("ce.cmd.*",  "The permission node for CE's commands.", PermissionDefault.OP);
    	Permission enchNode = new Permission("ce.ench.*", "The permission node for CE's enchantments.", PermissionDefault.OP);
    	Permission itemNode = new Permission("ce.item.*", "The permission node for CE's  items.", PermissionDefault.OP);
    	
    	cmdNode.addParent(mainNode, true);
    	enchNode.addParent(mainNode, true);
    	itemNode.addParent(mainNode, true);
    	
    	Permission cmdMenu  = new Permission("ce.cmd.menu",  "The permission for the CE command 'menu'");
    	Permission cmdList = new Permission("ce.cmd.reload", "The permission for the CE command 'reload'");
    	Permission cmdGive = new Permission("ce.cmd.give", "The permission for the CE command 'give'");
    	Permission cmdChange = new Permission("ce.cmd.change", "The permission for the CE command 'change'");
    	Permission cmdEnchant = new Permission("ce.cmd.enchant", "The permission for the CE command 'enchant'");
    	
    	cmdMenu.addParent(cmdNode, true);
    	cmdList.addParent(cmdNode, true);
    	cmdGive.addParent(cmdNode, true);
    	cmdChange.addParent(cmdNode, true);
    	cmdEnchant.addParent(cmdNode, true);
    	
    	Bukkit.getServer().getPluginManager().addPermission(mainNode);
    	
    	Bukkit.getServer().getPluginManager().addPermission(cmdNode);
    	Bukkit.getServer().getPluginManager().addPermission(enchNode);
    	Bukkit.getServer().getPluginManager().addPermission(itemNode);
    	
    	Bukkit.getServer().getPluginManager().addPermission(cmdMenu);
    	Bukkit.getServer().getPluginManager().addPermission(cmdList);
    	Bukkit.getServer().getPluginManager().addPermission(cmdGive);
    	Bukkit.getServer().getPluginManager().addPermission(cmdChange);
    	Bukkit.getServer().getPluginManager().addPermission(cmdEnchant);
    	
    	for(CItem ci : items) {
    		Permission itemTemp = new Permission("ce.item." + ci.getOriginalName(), "The permission for the CE Item '" + ci.getOriginalName() + "'.");
    		itemTemp.addParent(itemNode, true);
    		Bukkit.getServer().getPluginManager().addPermission(itemTemp);
    	}
    	
    	for(CEnchantment ce : enchantments) {
    		Permission itemTemp = new Permission("ce.ench." + ce.getOriginalName(), "The permission for the CE Enchantment '" + ce.getOriginalName() + "'.");
    		itemTemp.addParent(itemNode, true);
    		Bukkit.getServer().getPluginManager().addPermission(itemTemp);
    	}
    	
    }
    

    
    //Naming method
    public static ItemStack setName(ItemStack item, String name, List<String> lore) {
    	ItemMeta meta = item.getItemMeta();
    	if (name != null) {
    		meta.setDisplayName(name);
    	}
    	if (lore != null) {
    		meta.setLore(lore);
    	}
    	item.setItemMeta(meta);
    	return item;
}
    
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return econ != null;
    }
    
    public static void makeLists(boolean finalize, boolean printSuccess) {
    	
    	long time = System.currentTimeMillis();
    	
    	//Global Enchantments, 0 - 11
    	enchantments.add(new Lifesteal("Lifesteal",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Gooey("Gooey",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Deathbringer("Deathbringer",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Poison("Poison",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Block("Block",Application.GLOBAL,Cause.CLICK,5,100));
    	enchantments.add(new Shockwave("Shockwave",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Deepwounds("Deep Wounds",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Thunderingblow("Thunderingblow",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Cripple("Crippling Strike",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Ice("Ice Aspect",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	enchantments.add(new Autorepair("Autorepair",Application.GLOBAL,Cause.MOVE,5,100));
    	enchantments.add(new Vampire("Vampire",Application.GLOBAL,Cause.DAMAGEGIVEN,5,100));
    	
    	//Armor Enchantments, 12 - 16
    	enchantments.add(new Enlighted("Enlighted",Application.ARMOR,Cause.DAMAGETAKEN,4,100));
    	enchantments.add(new Frozen("Frozen",Application.ARMOR,Cause.DAMAGETAKEN,4,100));
    	enchantments.add(new Hardened("Hardened",Application.ARMOR,Cause.DAMAGETAKEN,4,100));
    	enchantments.add(new Molten("Molten",Application.ARMOR,Cause.DAMAGETAKEN,4,100));
    	enchantments.add(new Poisoned("Poisoned",Application.ARMOR,Cause.DAMAGETAKEN,4,100));
    	
    	//Bow Enchantments, 17 - 19
    	enchantments.add(new Bombardment("Bombardment",Application.BOW,Cause.BOW,10,100));
    	enchantments.add(new Firework("Firework",Application.BOW,Cause.BOW,20,100));
    	enchantments.add(new Lightning("Lightning",Application.BOW,Cause.BOW,10,100));
    	
    	//Tool Enchantments, 20 - 22
    	enchantments.add(new Smelting("Smelting",Application.TOOL,Cause.BLOCKBREAK,50,100));
    	enchantments.add(new Explosive("Explosive",Application.TOOL,Cause.BLOCKBREAK,50,100));
    	
    	//Special Enchantments
    	
    		//Boots
			enchantments.add(new Gears("Gears",Application.BOOTS,Cause.MOVE,4,100));
			enchantments.add(new Springs("Springs",Application.BOOTS,Cause.MOVE,4,100));
			enchantments.add(new Stomp("Stomp",Application.BOOTS,Cause.DAMAGETAKEN,4,100));
    	
    		//Helmet
    		enchantments.add(new Glowing("Glowing",Application.HELMET,Cause.MOVE,4,100));
    		enchantments.add(new Implants("Implants",Application.HELMET,Cause.MOVE,4,100));
    		
    		if(finalize)
    		for(CEnchantment ce : enchantments) 
    			ce.finalizeEnchantment();
    		
    		tools.resolveEnchantmentLists();
    		
    		if(printSuccess)
    			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[CE] All Enchantments have been loaded.");
    		
    		//ITEMS
        	
        	//Bow 0-2
        	items.add(new Minigun("Minigun", ChatColor.AQUA, "Fires a Volley of Arrows", 0, Material.BOW));
        	items.add(new BeastmastersBow("Beastmaster's Bow", ChatColor.AQUA, "Tame the wilderness;and turn nature against your foes!", 0, Material.BOW));
        	items.add(new HookshotBow("Hookshot Bow", ChatColor.AQUA, "Everyone is just one hook away", 0, Material.BOW));
        	
        	//Boots 3-6
        	items.add(new HermesBoots("Hermes Boots", ChatColor.GOLD, "These boots are made for walkin'", 100, Material.DIAMOND_BOOTS));
        	items.add(new LivefireBoots("Livefire Boots", ChatColor.DARK_RED, "Leave a burning trail...;Because it's fun!", 0, Material.DIAMOND_BOOTS));
        	items.add(new RocketBoots("Rocket Boots", ChatColor.AQUA, "Up we go!; ;WARNING: May cause dismemberment,;            death;            and explosions", 0, Material.DIAMOND_BOOTS));
        	items.add(new DruidBoots("Druid Boots", ChatColor.DARK_GREEN, "Let the nature rejuvenate you!", 0, Material.DIAMOND_BOOTS));
        	
        	//Flint + Steel 7
        	items.add(new Flamethrower("Flamethrower", ChatColor.DARK_RED, "Burn, baby, burn!", 0, Material.FLINT_AND_STEEL));
        	
        	//Stick 8
        	items.add(new NecromancersStaff("Necromancer's Staff of Destruction", ChatColor.AQUA, "Wreak chaos everywhere,;Because why not?", 0, Material.STICK));
        	
        	//Armor 9
        	//items.add((CItem) new Swimsuit("Scuba Helmet", ChatColor.BLUE, "Just stay underwater for a while,;Take your time!", 60, Material.IRON_HELMET));
        	
        	//Axe 10-11
        	items.add(new ThorsAxe("Thor's Axe", ChatColor.GOLD, "Smite your enemies down with mighty thunder!;Note: Batteries not included.", 0, Material.DIAMOND_AXE));
        	items.add(new Pyroaxe("Pyroaxe", ChatColor.DARK_RED, "Are your enemies burning?;Do you want to make their situation worse?;Then this is just perfect for you!", 0, Material.DIAMOND_AXE));
        	
        	//Sword 12
        	items.add(new AssassinsBlade("Assassin's Blade", ChatColor.AQUA, "Sneak up on your enemies and hit them hard!; ;(High chance of failure against Hacked Clients)", 200, Material.GOLD_SWORD));
        	
        	//Shovel 13
        	items.add(new HealingShovel("Healing Shovel", ChatColor.GREEN, "Smacking other people in the face;has never been healthier!", 600, Material.GOLD_SPADE));
        	
        	//Block 14-18
        	items.add(new BearTrap("Bear Trap", ChatColor.GRAY, "Just hope that it does not contain bears...", 0, Material.IRON_PLATE));
        	items.add(new PiranhaTrap("Piranha Trap", ChatColor.GRAY, "Who came up with this?", 0, Material.WOOD_PLATE));
        	items.add(new PoisonIvy("Poison Ivy", ChatColor.DARK_GREEN, "If you're too cheap to afford ladders,;just take this, it'll work just fine!", 0, Material.VINE));
        	items.add(new PricklyBlock("Prickly Block", ChatColor.LIGHT_PURPLE, "Just build a labyrinth out of these,;people will love you for it!", 0, Material.SAND));
        	items.add(new Landmine("Landmine", ChatColor.GRAY, "Just don't trigger it yourself, please.", 0, Material.GOLD_PLATE));
        	
        	//Any 19-23
        	items.add(new Powergloves("Powergloves", ChatColor.AQUA, "Throw all your problems away!", 500, Material.QUARTZ));
        	items.add(new Medikit("Medikit", ChatColor.GREEN, "Treats most of your ailments,;it even has a box of juice!", 2000, Material.NETHER_STALK));
        	items.add(new Bandage("Bandage", ChatColor.GREEN, "It has little hearts on it,;so you know it's good", 1000, Material.PAPER));
        	items.add(new Deathscythe("Deathscythe", ChatColor.DARK_GRAY, "An ancient evil lies within...", 400, Material.GOLD_HOE));
        	items.add(new PotionLauncher("Potion Launcher", ChatColor.DARK_GRAY, "Instructions: Put potion into the righthand slot;                of the potion launcher,;                aim and fire!; ;Manufactured by " + ChatColor.MAGIC + "Taiterio", 20, Material.HOPPER));
        	//
        	
        	
        	if(finalize)
        		for(CItem ci : items) 
        			ci.finalizeItem();
    		
        	if(printSuccess)
        		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[CE] All Items have been loaded.");
        	
        	deleteInactive();
        	
        	if(printSuccess)
        		if(Boolean.parseBoolean(Main.config.getString("Global.Logging.Enabled"))) 
        			Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[CE] Took " + (System.currentTimeMillis() - time) +"ms to initialize Custom Enchantments.");
    		 
        	
    }
    
    private static void deleteInactive() {
    	for(int i = 0; i < enchantments.size(); i++) {
    		CEnchantment ce = enchantments.get(i);
    		if(!Boolean.parseBoolean(config.getString("Enchantments." + ce.getOriginalName() + ".Enabled"))) {
    			enchantments.remove(i);
    			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[CE] Custom Enchantment " + ce.getOriginalName() + " is disabled in the config.");
    		}
    	}
    	for(int i = 0; i < items.size(); i++) {
    		CItem ci = items.get(i);
    		if(!Boolean.parseBoolean(config.getString("Items." + ci.getOriginalName() + ".Enabled"))) {
    			items.remove(i);
    			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[CE] Custom Item " + ci.getOriginalName() + " is disabled in the config.");
    		}
    	}
    }
    

    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {	
    	if(cmd.getName().equalsIgnoreCase("ce") || cmd.getName().equalsIgnoreCase("customenchantments")) {
    		String result = commandC.processCommand(sender, args);
    		if(result != "")
    			sender.sendMessage(result);
    		return true;
    	}
		return false;
    }
    
    
    
    
    
    
}
