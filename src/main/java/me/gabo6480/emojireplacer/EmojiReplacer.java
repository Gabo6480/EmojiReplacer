package me.gabo6480.emojireplacer;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Emote;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class EmojiReplacer extends JavaPlugin {

    private final DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);

    public EmojiReplacer(){
        INSTANCE = this;
    }


    @Override
    public void onEnable() {
        ConfigUtil.load();

        //ConfigUtil.clearGuildEmoteMap();

        List<Guild> guilds = DiscordSRV.getPlugin().getJda().getGuilds();
        getLogger().info("Found " + guilds.size() + " connected guilds!");
        for (Guild guild : guilds) {
            getLogger().info("Scanning Guild '" + guild.getName() + "' for emotes.");

            for (Emote emote : guild.getEmotes()) {
                getLogger().info("Found emote '" + emote.getName() + "'.");

                ConfigUtil.addGuildEmote(guild.getId(), emote.getName(), emote.getId());
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
}
