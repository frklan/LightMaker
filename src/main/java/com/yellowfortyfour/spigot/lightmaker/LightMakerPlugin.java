package com.yellowfortyfour.spigot.lightmaker;


import com.yellowfortyfour.spigot.lightmaker.api.RestApi;
import com.yellowfortyfour.spigot.lightmaker.commands.BlockCommand;
import com.yellowfortyfour.spigot.lightmaker.events.*;
import com.yellowfortyfour.spigot.lightmaker.db.BlockStorage;
import com.yellowfortyfour.spigot.lightmaker.tasks.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;
import java.io.File;

public class LightMakerPlugin extends JavaPlugin 
{
	private HashMap<UUID, String> pendingCommand = new HashMap<>();
	private Configuration config;
	private String db;
	private BlockStorage blockStorage;
	BukkitTask bulbButtonCleaner;

	@Override
	public void onEnable()
	{
		getLogger().severe("-----------------------------");
		getLogger().severe("LightMakerPlugin is enabled");
		getLogger().severe("-----------------------------");
		
		config = new Configuration(this);
		RestApi.setBaseApi(config.getString("api", "http://localhost:5000/api/v1/"));
		RestApi.setJwtToken(config.getString("api-token", ""));
		blockStorage = new BlockStorage(this, new File(getDataFolder(), config.getString("db-name", "LightMaker.db")));

		Bukkit.getServer().getPluginManager().registerEvents(new BlockBreakHandler(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractHandler(this), this);
		this.getCommand("b").setExecutor(new BlockCommand(this));

	}

	public Configuration getConfiguration()
	{
		return config;
	}

	public BlockStorage getBlockStorage() {
		return blockStorage;
	}

}
