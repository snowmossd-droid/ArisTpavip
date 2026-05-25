package me.vennlmao.aristpa.listeners;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final AristTpa plugin;

    public MoveListener(AristTpa plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getWarmupManager().isInWarmup(player)) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        if (from.getBlockX() != to.getBlockX()
                || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ()) {
            plugin.getWarmupManager().cancelWarmup(player.getUniqueId());
            MessageUtil.sendChatList(player, "teleport_cancelled_moved");
            MessageUtil.sendActionbar(player, "teleport_cancelled_moved_ab");
        }
    }
}
