package me.gabo6480.emojireplacer;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ConfigHelper {
    private final String name;
    private File configFile;
    @Getter
    private YamlConfiguration yamlConfig;
    private final Plugin plugin;

    public ConfigHelper(@NotNull String name, @NotNull Plugin plugin) {
        this.name = name;
        this.plugin = plugin;

        load();
    }

    private void load() {
        this.configFile = new File(plugin.getDataFolder(), this.name + ".yaml");
        try {
            if (!this.configFile.exists()) {
                boolean ign1 = this.configFile.getParentFile().mkdirs();
                boolean ign2 = this.configFile.createNewFile();
            }

            this.yamlConfig = YamlConfiguration.loadConfiguration(this.configFile);

            this.yamlConfig.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning(this.name + ".yaml Config failed to load! ^^ Reason above ^^");
        }
    }

    public void save() {
        try{
            this.yamlConfig.save(this.name + ".yaml");
        }catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning(this.name + ".yaml Config failed to save! ^^ Reason above ^^");

        }
    }

    @NotNull
    public FileConfiguration reload() {
        load();
        return getYamlConfig();
    }

    public File getConfigFile() {
        return configFile;
    }
}
