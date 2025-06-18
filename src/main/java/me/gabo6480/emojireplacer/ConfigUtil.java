package me.gabo6480.emojireplacer;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigUtil {
    static private final ConfigHelper emotes = new ConfigHelper("emotes", EmojiReplacer.INSTANCE);

    @Getter
    public static Map<String, Map<String, String>> guildEmoteMap = new HashMap<>();

    public static void load(){
        emotes.reload();

        guildEmoteMap.clear();

        ConfigurationSection section = emotes.getYamlConfig();

        if(section != null) {
            section.getValues(false).forEach((key, value) -> {
                ConfigurationSection subSection =  section.getConfigurationSection(key);
                if(subSection != null) {
                    Map<String,Object> map = subSection.getValues(false);
                    Map<String,String> newMap = map.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> (String)e.getValue()));
                    guildEmoteMap.put(key, newMap );
                }
                else guildEmoteMap.put(key, new HashMap<>());
            });
        }
    }

    public static void save(){

        boolean deleted = emotes.getConfigFile().delete();

        guildEmoteMap.forEach(((s, stringStringMap) -> emotes.getYamlConfig().set(s, stringStringMap)));

        emotes.save();
    }

    public static void clearGuildEmoteMap(){
        guildEmoteMap.clear();
    }

    public static void addGuildEmote(String guildId, String emoteName, String emoteId){
        if(!guildEmoteMap.containsKey(guildId)){
            guildEmoteMap.put(guildId, new HashMap<>());
        }

        emoteName = emoteName.toLowerCase().replace(":", "");

        guildEmoteMap.get(guildId).put(emoteName, emoteId);
    }

    @Nullable
    public static String getEmoteIdForEmote(@Nullable String guildId, String emoteName){
        emoteName = emoteName.toLowerCase().replace(":", "");

        if(guildEmoteMap.containsKey(guildId)){
            Map<String, String> emoteMap = guildEmoteMap.get(guildId);

            if(emoteMap.containsKey(emoteName)){
                return emoteMap.get(emoteName);
            }
        }

        for (Map<String, String> emoteMap : guildEmoteMap.values()) {
            if(emoteMap.containsKey(emoteName)){
                return emoteMap.get(emoteName);
            }
        }

        return null;
    }
}
