package main.java.br.com.alsupreme.wrestaurer;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

public class WorldRestaurer extends JavaPlugin{
	
	
	@Override
	public void onEnable() {
		File configF = new File(this.getServer().getPluginManager().getPlugin("WorldRestaurer").getDataFolder(), "config.yml");
		FileConfiguration config = this.getConfig();
		
		if(!configF.exists()) {
			
			config.options().copyDefaults(true);
			this.saveConfig();
			
		}else {
			this.reloadConfig();
			config.options().copyDefaults(true);
			this.saveConfig();
		}
		


		
		
		if(config.getBoolean("SaveAndLoadOnStartServer")) {
			for(String worldsname : config.getStringList("Worlds")) {

				File world = new File(worldsname);
				File copyWorld = new File("plugins/WorldRestaurer/copyWorld/" + worldsname);
			
			if(!copyWorld.exists()) {
				saveWorld(world, copyWorld);
				this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Backuping the world: &c&l" + worldsname));
			}else {

				this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Starting the restaure of world: &c&l" + worldsname));
				loadWorld(world, copyWorld);

			}
			}
		}
		
		this.getCommand("saveworld").setExecutor(new Commands());
		this.getCommand("restaureworld").setExecutor(new Commands());
		this.getCommand("deleteworld").setExecutor(new Commands());
		

		new UpdateChecker(this, 00000).getVersion(Version -> {
			if (this.getDescription().getVersion().equalsIgnoreCase(Version)){
				this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8No update avaible for &bWorld &aRestaurer, &6Current version: &f " + Version));
			}else {
				this.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Update avaible for &bWorld &aRestaurer, &6New version: &f" + Version));
			}
		});
		
		this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[WR]  " + ChatColor.GOLD + "Loaded World Restaurer");
		
		super.onEnable();
	}
	
	
	
	public static void saveWorld(File world, File copyWorld) {
		try {
			
			FileUtils.copyDirectory(world, copyWorld);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadWorld(File world, File copyWorld) {
		try {
			FileUtils.copyDirectory(copyWorld, world, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteWorld(File world) {
		try {
			FileUtils.deleteDirectory(world);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
