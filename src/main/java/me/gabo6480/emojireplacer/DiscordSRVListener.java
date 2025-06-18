package me.gabo6480.emojireplacer;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.ConfigReloadedEvent;
import github.scarsz.discordsrv.api.events.GameChatMessagePostProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.GuildChannel;
import org.bukkit.plugin.Plugin;
import github.scarsz.discordsrv.api.ListenerPriority;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordSRVListener {
    private final Plugin plugin;
    private final Pattern emotePattern = Pattern.compile(":.*?:");

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

        String message = event.getProcessedMessage();

        plugin.getLogger().fine("Replacing emojis: " + message);

        GuildChannel guildChannel = null;
        try{
            String channelId =  DiscordSRV.getPlugin().getChannels().get(event.getChannel());

            guildChannel = DiscordSRV.getPlugin().getJda().getGuildChannelById(channelId);
        }catch (Exception ignored){
        }
        String guildId  = null;
        if(guildChannel == null) {
            plugin.getLogger().warning("Failed to find guild for channel: " + event.getChannel());
        }
        else {
            guildId = guildChannel.getGuild().getId();
        }

        Matcher matcher = emotePattern.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()){
            String replacement = ConfigUtil.getEmoteIdForEmote(guildId, matcher.group());

            if(replacement != null){
                matcher.appendReplacement(sb, "");

                sb.append('<');
                sb.append(matcher.group());
                sb.append(replacement);
                sb.append('>');
            }
            else {
                matcher.appendReplacement(sb, matcher.group());
                sb.append('¿');
            }
        }
        matcher.appendTail(sb);

        event.setProcessedMessage(sb.toString());
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
