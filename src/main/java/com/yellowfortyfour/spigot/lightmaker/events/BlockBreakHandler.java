package com.yellowfortyfour.spigot.lightmaker.events;

import com.yellowfortyfour.spigot.lightmaker.LightMakerPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;


/**
 * This object fires when a block breaks and removes a bulb
 * attached to that block.
 * 
 * TODO: handle breakage due to e.g. mobs or TNT etc.
 */
public class BlockBreakHandler implements Listener
{
	private final LightMakerPlugin plugin;

	public BlockBreakHandler(Plugin plugin)
	{
		super();
		this.plugin = (LightMakerPlugin) plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		Player player = event.getPlayer();
		if(player == null)
			return;

		Block block = event.getBlock();
		Location blockPosition = block.getLocation();
		if(blockPosition == null)
			return;

		String blockWorld = block.getWorld().getName();
		if(blockWorld == null)
			return;

		// i.e. player is breaking a block. remove all buttons here!
		ArrayList<String> buttonIds = plugin.getBlockStorage().getButtonIdsAtPos(blockPosition, blockWorld);
		
		buttonIds.forEach((b) -> {
			plugin.getBlockStorage().deleteButton(b);
		});
	}
}
