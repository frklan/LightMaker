
package com.yellowfortyfour.spigot.lightmaker;

import org.bukkit.entity.Player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class LighMakerBulbOffCommand implements CommandExecutor { 


  private Plugin plugin;

  LighMakerBulbOffCommand(Plugin plugin)
  {
    this.plugin = plugin;
    plugin.getLogger().info(ChatColor.RED + "--->> LightMaker command \"lmoff\" created <<---" + ChatColor.RESET);
  }

 // This method is called, when somebody uses our command
 @Override
 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
   if(!sender.hasPermission("lightmaker.use") && (sender instanceof Player))
    {
      plugin.getLogger().severe("Unutorized use of LightMaker command by " + sender.getName());
      return false;
    }

   if(args.length != 1)
   {
     printHelpMessage(sender);
     return true;
   }

   RestApi.BulbOff(args[0]);

   return true;
 }

  private void printHelpMessage(CommandSender sedner)
  {
    sedner.sendMessage("Please use 'lmoff [bulbid]'");
  }
}
