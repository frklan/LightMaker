
package com.yellowfortyfour.spigot.lightmaker;

import org.bukkit.entity.Player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class LighMakerCommand implements CommandExecutor { 


  private Plugin plugin;

  LighMakerCommand(Plugin plugin)
  {
    //super();
    this.plugin = plugin;
    plugin.getLogger().info(ChatColor.RED + "--->> LightMaker command \"lm\" created <<---" + ChatColor.RESET);
  }

  // This method is called, when somebody uses our command
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    plugin.getLogger().info("got command: " + command);
    
    
    if(!(sender instanceof Player)) 
    {
        sender.sendMessage("Command only valid for players, sorry!");
        return false;
    }

    Player player = (Player) sender;
    player.sendMessage("got command: " + command);
    if(args.length < 1)
    {
      printHelpMessage(player);
      return true;
    }

    switch(args[0])
    {
      case "create":
      {
       
        break;
      }

    }

    
    return true;
  }

  private void printHelpMessage(Player player)
  {
    player.sendMessage("lm commands are:");
    player.sendMessage("\tlm create [bulb id]");
    player.sendMessage("\tlm list");
    player.sendMessage("\tlm delete");
  }
}
