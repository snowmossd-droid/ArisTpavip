package me.vennlmao.aristpa.listeners;

import me.vennlmao.aristpa.ArisTpa;
import me.vennlmao.aristpa.utils.MessageUtil;
import me.vennlmao.aristpa.utils.SoundUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final ArisTpa plugin;

    public DamageListener(ArisTpa plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!plugin.getWarmupManager().isInWarmup(player)) return;

        plugin.getWarmupManager().cancelWarmup(player.getUniqueId());
        MessageUtil.sendChatList(player, "teleport_cancelled_damaged");
        MessageUtil.sendActionbar(player, "teleport_cancelled_damaged_ab");
        SoundUtil.play(player, "damaged");
    }
}
