
package com.yellowfortyfour.spigot.lightmaker.tasks;

import com.yellowfortyfour.spigot.lightmaker.LightMakerPlugin;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BulbButtonCleaner extends BukkitRunnable {

	private final LightMakerPlugin plugin;

	public BulbButtonCleaner(Plugin plugin)
	{
		this.plugin = (LightMakerPlugin) plugin;
  }
  
  @Override
  public void run() {
    plugin.getLogger().info("run()");
    
    Server server = plugin.getServer();
    List<World> worldList = server.getWorlds();    

    //Block block = world.getBlockAt(-1536, 80, -2457);
    //plugin.getLogger().info(block.getType().toString());


    for(World world : worldList) {

      ArrayList<String> buttons = plugin.getBlockStorage().getButtonIds(world.getName());
      for (String butt : buttons) {

        HashMap<String, Integer> buttonLocation  = plugin.getBlockStorage().getButtonLocation(world.getName(), butt);
        Block block = world.getBlockAt(buttonLocation.get("x"), buttonLocation.get("y"), buttonLocation.get("z"));
        plugin.getLogger().info(block.getType().toString());
        
        Material blockType = block.getType();
        if(blockType != Material.STONE_BUTTON && blockType != Material.WOOD_BUTTON) {
          plugin.getLogger().info("this is not a button, lets delete it.");
          plugin.getBlockStorage().deleteButton(butt);
        }

        
      }
    }

  }

}