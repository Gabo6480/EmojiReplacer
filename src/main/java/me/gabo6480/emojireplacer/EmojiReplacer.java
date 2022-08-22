package me.gabo6480.emojireplacer;

import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.plugin.java.JavaPlugin;

public final class EmojiReplacer extends JavaPlugin {

    private final DiscordSRVListener discordsrvListener = new DiscordSRVListener(this);

    public EmojiReplacer(){
        INSTANCE = this;
    }


    @Override
    public void onEnable() {
        ConfigUtil.load();

        DiscordSRV.api.subscribe(discordsrvListener);
    }

    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(discordsrvListener);
    }

    public static JavaPlugin INSTANCE;
}
