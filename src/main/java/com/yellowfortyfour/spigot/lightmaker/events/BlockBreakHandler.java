package com.yellowfortyfour.spigot.lightmaker.events;

import com.yellowfortyfour.spigot.lightmaker.LightMakerPlugin;
import com.yellowfortyfour.spigot.lightmaker.api.RestApi;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlot;

import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BlockBreakHandler implements Listener
{
	private final LightMakerPlugin plugin;

	public BlockBreakHandler(Plugin plugin)
	{
		super();
		this.plugin = (LightMakerPlugin) plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{	
		EquipmentSlot hand = event.getHand(); 
		if(hand != EquipmentSlot.HAND)
			return;

		Block clickedBlock = event.getClickedBlock();
		if(clickedBlock == null)
			return;

		Material blockType = clickedBlock.getType();
		if(blockType != Material.STONE_BUTTON && blockType != Material.WOOD_BUTTON)
			return;

		Action action = event.getAction();
		if(action == Action.LEFT_CLICK_BLOCK)
			createButton(event);
		else
			handleButtonUse(event);
	}	

	private void createButton(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player == null)
			return;

		Block block = event.getClickedBlock();
		Location blockPosition = block.getLocation();
		if(blockPosition == null)
			return;

		String blockWorld = block.getWorld().getName();
		if(blockWorld == null)
			return;

		if(plugin.getBlockStorage().hasPendingButton(player.getUniqueId())) {
			// We have a button, a tie command is pending, etc.  -- let's store it.
			event.setCancelled(true);
			
			try {
				plugin.getBlockStorage().finalizeButton(player.getUniqueId(), block);
			} catch(Exception e) {
				player.sendMessage("Could not create button, try again (" + e + ")");
			}
			player.sendMessage("A Trådfri connected button has been created");

		} 
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
	private void handleButtonUse(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player == null)
			return;

		if(!player.hasPermission("lightmaker.use") && !player.isOp()) {
			return; // quiet fail if player lacks permission to use.
		}

		Block block = event.getClickedBlock();
		if(block == null)
			return;

		Location blockPosition = block.getLocation();
		if(blockPosition == null)
			return;

		String blockWorld = block.getWorld().getName();
		if(blockWorld == null)
			return;

		ArrayList<String> bulbIds = plugin.getBlockStorage().getBulbsIdsAtPos(blockPosition, blockWorld);
		if(bulbIds.isEmpty()) // i.e. we don't have any bulbs on this button.
			return; 
		
		// We're good to go, let's iterate over all bulbs and toggle them one by one!
		try {
			for(String b : bulbIds) {
				RestApi.BulbToggle(b);
			}
		} catch(Exception e) {
			plugin.getLogger().severe("Error accessing LightMaker API: " + e.getMessage());
			player.sendMessage(ChatColor.RED + "Can't toggle lights!" + ChatColor.RESET);
			return;
		}
		player.sendMessage(ChatColor.GREEN + "Lights has been toggled." + ChatColor.RESET);
	}	
}
