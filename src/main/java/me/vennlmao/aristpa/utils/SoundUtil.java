package me.vennlmao.aristpa.utils;

import me.vennlmao.aristpa.ArisTpa;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtil {

    private static ArisTpa plugin;

    public static void init(ArisTpa pl) {
        plugin = pl;
    }

    public static void play(Player player, String key) {
        String soundName = plugin.getConfig().getString("sounds." + key + ".sound", "");
        float volume = (float) plugin.getConfig().getDouble("sounds." + key + ".volume", 1.0);
        float pitch = (float) plugin.getConfig().getDouble("sounds." + key + ".pitch", 1.0);

        if (soundName.isEmpty()) return;

        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
