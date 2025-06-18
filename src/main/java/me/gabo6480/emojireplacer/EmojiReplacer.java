package me.gabo6480.emojireplacer;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Emote;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public final class EmojiReplacer extends JavaPlugin {

    private final DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);

    public EmojiReplacer(){
        INSTANCE = this;
    }


    @Override
    public void onEnable() {
        LOGGER = getLogger();
        ConfigUtil.load();

        //ConfigUtil.clearGuildEmoteMap();

        List<Guild> guilds = DiscordSRV.getPlugin().getJda().getGuilds();
        LOGGER.info("Found " + guilds.size() + " connected guilds!");
        for (Guild guild : guilds) {
            LOGGER.info("Scanning Guild '" + guild.getName() + "' for emotes.");

            for (Emote emote : guild.getEmotes()) {
                LOGGER.info("Found emote '" + emote.getName() + "'.");

                ConfigUtil.addGuildEmote(guild.getId(), emote.getName(), emote.getId());

                LOGGER.info(emote.getImageUrl());
                downloadImage(emote.getImageUrl(),  this.getDataFolder().getAbsolutePath() + File.pathSeparatorChar + "files" + File.pathSeparatorChar + guild.getId() + File.pathSeparatorChar + emote.getName() + (emote.isAnimated() ? ".gif" : ".png"));
            }
        }
        ConfigUtil.save();

        DiscordSRV.api.subscribe(discordsrvListener);
    }

    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(discordsrvListener);
    }

    public static JavaPlugin INSTANCE;
    public static Logger LOGGER;

    private void downloadImage(String url, String filename){
        try{
            File f = new File(filename);
            boolean ign1 = f.getParentFile().mkdirs();
            boolean ign2 = f.createNewFile();
        }
        catch (IOException e) {
            LOGGER.warning("Couldn't create file: \n" + e.getMessage());
        }

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            LOGGER.warning("Couldn't download file: \n" + e.getMessage());
        }
    }
}
