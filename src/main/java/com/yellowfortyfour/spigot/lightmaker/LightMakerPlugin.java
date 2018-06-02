package com.yellowfortyfour.spigot.lightmaker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;




public class LightMakerPlugin extends JavaPlugin 
{
	protected FileConfiguration config;
	private String baseApiUrl;

	@Override
	public void onEnable()
	{

		try
		{
			this.saveDefaultConfig();
			baseApiUrl = getConfig().getString("api");
			RestApi.setBaseApi(baseApiUrl);
		}
		catch(Exception e)
		{
			getLogger().severe(e.toString());
			return;
		}
		
		Bukkit.getServer().getPluginManager().registerEvents(new LightMaker(this), this);
		this.getCommand("lm").setExecutor(new LighMakerCommand(this));
		this.getCommand("lmon").setExecutor(new LighMakerBulbOnCommand(this));
		this.getCommand("lmoff").setExecutor(new LighMakerBulbOffCommand(this));
	}

	public String getApiUrl()
	{
		return this.baseApiUrl;
	}

}
