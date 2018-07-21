
package com.yellowfortyfour.spigot.lightmaker.commands;

import com.yellowfortyfour.spigot.lightmaker.LightMakerPlugin;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor; 
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;


public class BlockCommand implements CommandExecutor { 

  private final LightMakerPlugin plugin;

  public BlockCommand(Plugin plugin)
  {
    this.plugin = (LightMakerPlugin) plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
     
    if(!(sender instanceof Player)) {
      sender.sendMessage("This command can only be used by players, sorry");
      return true;
    }
    Player player = (Player) sender;
    
    if(args.length < 1) {
      //printHelp(player);
      return false;
    } 
    
    switch(args[0]) {
      case "c":
      {
        if(createNewSwitch(player, Arrays.copyOfRange(args, 1, args.length))) {
          return true;
        } else {
          //printHelp(player);
          return false;
        }
      }
    }
    return true;
  }

  private Boolean createNewSwitch(Player player, String[] bulbs) {
    if(bulbs.length < 1) {
      //printHelp(player);
      return false;
    }

    if(!player.hasPermission("lightmaker.create")) {
      player.sendMessage("Sorry you do not have permission to use this command.");
      return true;
    }
    
    ArrayList<Integer> bulbIds = new ArrayList<Integer>();
    for(String a : bulbs) {
      try {
        Integer b = Integer.parseInt(a);
        if(b < 1 || b > 99999999){ // Do we need to check this better?
          player.sendMessage("Invalid bulb id");
          return false;
        }
        bulbIds.add(b);
      } catch(NumberFormatException e){} // ignore illegal values
    }

    player.sendMessage("Creating switch..");
    plugin.getBlockStorage().createButton(player.getUniqueId(), bulbIds);
    player.sendMessage("..done. Now hit the switch with your right hand to finish.");    

    return true;
  }
}
