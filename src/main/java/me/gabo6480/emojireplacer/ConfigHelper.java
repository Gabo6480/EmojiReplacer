package me.gabo6480.emojireplacer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ConfigHelper {
    private final String name;
    private File configFile;
    private YamlConfiguration yamlConfig;
    private final Plugin plugin;

    public ConfigHelper(@NotNull String name, @NotNull Plugin plugin) {
        this.name = name;
        this.plugin = plugin;

        load();
    }

    private void load() {
        this.configFile = new File(plugin.getDataFolder(), this.name + ".yml");
        try {
            if (!this.configFile.exists()) {
                if (this.configFile.getParentFile().exists() || this.configFile.getParentFile().mkdirs()) {
                    plugin.saveResource(this.name + ".yml", true);
                    this.configFile.createNewFile();
                }
            }

            this.yamlConfig = new YamlConfiguration();
            this.yamlConfig.load(this.configFile);

        }catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning(this.name + ".yml Config failed to load! ^^ Reason above ^^");
        }
    }

    public void save() {
        try{
            if (this.configFile.exists()) {
                this.yamlConfig.save(this.configFile);
            }
            else load();
        }catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning(this.name + ".yml Config failed to save! ^^ Reason above ^^");

        }
    }

    @NotNull
    public FileConfiguration reload() {
        load();
        return getYamlConfig();
    }

    public YamlConfiguration getYamlConfig() {
        return yamlConfig;
    }

    public File getConfigFile() {
        return configFile;
    }
}
