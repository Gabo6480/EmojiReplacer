package me.gabo6480.emojireplacer;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.ConfigReloadedEvent;
import github.scarsz.discordsrv.api.events.GameChatMessagePostProcessEvent;
import org.bukkit.plugin.Plugin;
import github.scarsz.discordsrv.api.ListenerPriority;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordSRVListener {
    private final Plugin plugin;

    public DiscordSRVListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe(priority = ListenerPriority.NORMAL)
    public void configReloaded(ConfigReloadedEvent event){
        //Event called when discordsrv's config get reloaded.
        plugin.getLogger().info("Reloading EmojiReplacer.");
        ConfigUtil.load();
    }

    @Subscribe(priority = ListenerPriority.NORMAL)
    public void aMessageWasSentInADiscordGuildByTheBot(GameChatMessagePostProcessEvent event) {
        // Example of logging a message sent in Minecraft (being sent to Discord)

        AtomicReference<String> processedMessage = new AtomicReference<>(event.getProcessedMessage());

        ConfigUtil.emojiMap.forEach((emoji, emojiReplace) -> {
            processedMessage.set(putEmoji(emoji, emojiReplace, processedMessage.get().toLowerCase(Locale.ROOT), processedMessage.get()));
        });

        event.setProcessedMessage(processedMessage.get());
    }

    private String putEmoji(String emoji, String emojiReplace, String origin, String target){
        Matcher matcher = Pattern.compile(emoji).matcher(origin);

        if(!matcher.results().findAny().isPresent())
            return target;
        matcher.reset();

        int lastIndex = 0;
        StringBuilder output = new StringBuilder();
        while (matcher.find()) {
            output.append(target, lastIndex, matcher.start())
                    .append(emojiReplace);

            lastIndex = matcher.end();
        }
        if (lastIndex < target.length()) {
            output.append(target, lastIndex, target.length());
        }

        return output.toString();
    }
}
