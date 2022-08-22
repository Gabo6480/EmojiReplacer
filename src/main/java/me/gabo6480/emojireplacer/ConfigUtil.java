package me.gabo6480.emojireplacer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfigUtil {
    static private ConfigHelper emojis = new ConfigHelper("emojis", EmojiReplacer.INSTANCE);

    public static Map<String, String> emojiMap = new HashMap<>();

    public static void load(){
        emojiMap.clear();

        emojis.getYamlConfig().getValues(false).forEach((key, value) -> {
            emojiMap.put(key.toLowerCase(Locale.ROOT), (String) value);
        });
    }
}
