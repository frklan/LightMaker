package com.yellowfortyfour.spigot.lightmaker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class LightMaker implements Listener
{
	private static final String CREATE_PERMISSION = "lightmaker.create";
	private static final String ALL_PERMISSION = "lightmaker.all";
	private Plugin plugin;

	LightMaker(Plugin plugin)
	{
		super();
		this.plugin = plugin;
	}

	
	// @EventHandler
	// public void onBlockBreakEvent(BlockBreakEvent event)
	// {
	// 	Block block = event.getBlock();
	// 	plugin.getLogger().info("data:" + block.getType());
	// }

	// @EventHandler
	// public void onPlayerInteract(PlayerInteractEvent event)
	// {
	// 	plugin.getLogger().info("action = " + event.getAction());
	// 	plugin.getLogger().info("hand = " + event.getHand());
	// 	plugin.getLogger().info("block = " + event.getClickedBlock());
		
	// 	Block block = event.getClickedBlock();
	// 	if(block == null)
	// 		return;
			
	// 	plugin.getLogger().info("meta = " + block.hasMetadata("supplyDrop"));
			
	// 	Material type = block.getType();
	// 	byte blockData = block.getData();

	// 	// if(type == Material.STONE_BUTTON || type == Material.WOOD_BUTTON )
	// 	// {
	// 		event.getPlayer().sendMessage("You clicked The magic button!");
	// 		if(block.hasMetadata("bulbId"))
	// 		{				
	// 			List<MetadataValue> meta = block.getMetadata("blockId"); //, new FixedMetadataValue(plugin, 65540));
				
	// 			event.getPlayer().sendMessage("we have a blockId: " + meta.size() + "/" + meta.toString() + "/");// + meta.get(0).asString());
				
	// 		}
		//}

		// STONE_BUTTON
		// WOOD_BUTTON
		// LEVER
		
	//}
}
