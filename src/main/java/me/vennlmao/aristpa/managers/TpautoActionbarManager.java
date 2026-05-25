package me.vennlmao.aristpa.managers;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.vennlmao.aristpa.ArisTpa;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpautoActionbarManager {

    private final ArisTpa plugin;
    private final Map<UUID, ScheduledTask> tasks = new HashMap<>();

    public TpautoActionbarManager(ArisTpa plugin) {
        this.plugin = plugin;
    }

    public void start(Player player) {
        stop(player);
        ScheduledTask task = player.getScheduler().runAtFixedRate(plugin, t -> {
            if (!player.isOnline()) {
                stop(player);
                return;
            }
            if (!plugin.getWarmupManager().isInWarmup(player)) {
                MessageUtil.sendActionbar(player, "tpauto_enabled_ab");
            }
        }, null, 1L, 40L);
        tasks.put(player.getUniqueId(), task);
    }

    public void stop(Player uuid) {
        ScheduledTask task = tasks.remove(uuid.getUniqueId());
        if (task != null) task.cancel();
    }

    public void cancelAll() {
        tasks.values().forEach(ScheduledTask::cancel);
        tasks.clear();
    }
}
