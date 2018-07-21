package com.yellowfortyfour.spigot.lightmaker;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Configuration {

  private final File ymlFile;
  private final Plugin plugin;
  private YamlConfiguration configuration;

  public Configuration(Plugin plugin) {
      this.plugin = plugin;
      this.ymlFile = resolveConfigFile();
      reload();
  }

  public void reload() {
    this.configuration = YamlConfiguration.loadConfiguration(this.ymlFile);
  }

  public String getString(String path, String def) {
    return this.configuration.getString(path, def);
  }

  public boolean getBoolean(String path, boolean def) {
    return this.configuration.getBoolean(path, def);
  }

  public List<String> getList(String path, List<String> def) {
    List<String> ret = this.configuration.getStringList(path);
    return ret == null ? def : ret;
  }


  public List<String> getSectionKeys(String path, List<String> def) {
      ConfigurationSection section = this.configuration.getConfigurationSection(path);
      if (section == null) {
          return def;
      }

      Set<String> keys = section.getKeys(false);
      return keys == null ? def : new ArrayList<>(keys);
  }

  public HashMap<String, String> getSection(String path, HashMap<String, String> def) {
    List<String> sectionKeys = getSectionKeys(path, null);
    if(sectionKeys == null) {
      return def;
    }

    HashMap<String, String> section = new HashMap<>();
    sectionKeys.forEach((key) -> {
      section.put(key, getString(path + "." + key, null));
    });
    return section;
  }

  private File resolveConfigFile() {
    File configFile = new File(plugin.getDataFolder(), "config.yml");
    if (!configFile.exists()) {
      plugin.getDataFolder().mkdirs();
      plugin.saveResource("config.yml", true);
    }
    return configFile;
  }
}
