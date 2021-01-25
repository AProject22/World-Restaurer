package main.java.br.com.alsupreme.wrestaurer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;

public class UpdateChecker {
	private JavaPlugin plugin;
	private int resourceID;
	
	public UpdateChecker(JavaPlugin plugin, int resourceID) {
		this.plugin = plugin;
		this.resourceID = resourceID;
	}
	public void getVersion(final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			try(InputStream inputstream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceID).openStream(); Scanner scanner = new Scanner(inputstream)){
				if(scanner.hasNext()) {
					consumer.accept(scanner.next());
				}
			} catch (MalformedURLException e) {
				 this.plugin.getLogger().info("Cannot look for updates: " + e.getMessage());
			} catch (IOException e) {
				 this.plugin.getLogger().info("Cannot look for updates: " + e.getMessage());
			}
		});
	}
}
