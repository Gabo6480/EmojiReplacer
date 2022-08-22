package me.gabo6480.emojireplacer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfigUtil {
    static private final ConfigHelper emojis = new ConfigHelper("emojis", EmojiReplacer.INSTANCE);

    public static Map<String, String> emojiMap = new HashMap<>();

    public static void load(){
        emojis.reload();

        emojiMap.clear();

        emojis.getYamlConfig().getValues(false).forEach((key, value) -> {

            EmojiReplacer.INSTANCE.getLogger().info("Emoji Loaded: " + key.toLowerCase(Locale.ROOT) + " as " + (String) value);
            emojiMap.put(key.toLowerCase(Locale.ROOT), (String) value);
        });
    }
}
