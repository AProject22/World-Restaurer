package main.java.br.com.alsupreme.wrestaurer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor{
	Plugin plugin = WorldRestaurer.getPlugin(WorldRestaurer.class);
	FileConfiguration config = plugin.getConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command com, String lab, String[] args) {
		

		if(com.getName().equalsIgnoreCase("saveworld")) {
			if(args.length == 1) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					if(player.hasPermission("WR.saveworld") || player.hasPermission("WR.adminuse")) {saveWorld(args[0], sender);}
				}else {
					saveWorld(args[0], sender);
				}
				
			}else {
				
				if(sender instanceof Player) {
					
					Player player = (Player) sender;
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5The correct use of this command is &c&l/saveworld <world name>"));
				}else {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5The correct use of this command is &c&l/saveworld <world name>"));
				}
			}
		}
		
		if(com.getName().equalsIgnoreCase("restaureworld")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("WR.restaureworld") || player.hasPermission("WR.adminuse")) {restaureWorld(sender);}
			}else {
				restaureWorld(sender);
			}
		}
		
		if(com.getName().equalsIgnoreCase("deleteworld")) {
			if(args.length == 1) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					if(player.hasPermission("WR.deleteworld") || player.hasPermission("WR.adminuse")) {deleteWorld(sender, args[0]);}
				}else {
					deleteWorld(sender, args[0]);
				}
			}else {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5The correct use of this command is &c/deleteworld <world name>"));
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5The correct use of this command is &c&l/deleteworld <world name>"));
				}else {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5The correct use of this command is &c&l/deleteworld <world name>"));
				}
			}
		}
		
		
		return true;
	}
	
	private void saveWorld(String WorldName, CommandSender sender) {
			File world = new File(WorldName);
			if(!world.exists()) {
				
				if(sender instanceof Player) {
					
					Player player = (Player) sender;
					player.sendMessage(ChatColor.DARK_RED + "World doesn't exist");
					
				}else {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "World doesn't exist");
					
				}
			}else {
				
				File copyWorld = new File("plugins/WorldRestaurer/copyWorld/" + WorldName);
				List<String> allworlds = new ArrayList<String>();
				
				for(String worldsname : config.getStringList("Worlds")) {
					if(!worldsname.equals(WorldName)) {
						
						allworlds.add(worldsname);
					
					}
				}
				

				allworlds.add(WorldName);
				config.set("Worlds", allworlds);

				
				config.options().copyDefaults(true);
				plugin.saveConfig();

				if(sender instanceof Player) {
					
					Player player = (Player) sender;
					player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&2World &6&l"+ WorldName +" &2Has been salved"));
				}
				plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&2World &6&l"+ WorldName +" &2Has been salved"));
				WorldRestaurer.saveWorld(world, copyWorld);
			}
	}
	
	
	private void restaureWorld(CommandSender sender) {
		
		
		for(String worldnames : config.getStringList("Worlds")) {
			plugin.getServer().dispatchCommand(sender, "mv unload " + worldnames);
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unloading &c&l" + worldnames));
			
			File world = new File(worldnames);
			File copyWorld = new File("plugins/WorldRestaurer/copyWorld/" + worldnames);
			
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Starting the restaure of world: &c&l" + worldnames));
			}
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Starting the restaure of world: &c&l" + worldnames));
			
			
			WorldRestaurer.loadWorld(world, copyWorld);
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Loading &c&l" + worldnames));
			plugin.getServer().dispatchCommand(sender, "mv load " + worldnames);
		}
		
	}
	private void deleteWorld(CommandSender sender, String WorldName) {
		File world = new File("plugins/WorldRestaurer/copyWorld/" + WorldName);
		if(world.exists()) {
			List<String> WorldList = new ArrayList<String>();
		
			for(String worldanames : config.getStringList("Worlds")) {
				if(!worldanames.equals(WorldName)) {
					WorldList.add(worldanames);
				}
			}
		
			config.set("Worlds", WorldList);
			plugin.saveConfig();
		


		
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c&l"+ WorldName + "'s &5backup has been deleted"));
			}
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l"+WorldName + "'s &5backup has been deleted"));
			
			WorldRestaurer.deleteWorld(world);
		}else {
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l"+WorldName + "'s &5NOT EXIST"));
		}
	}
	
}

