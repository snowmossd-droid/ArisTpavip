package me.vennlmao.aristpa.managers;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarmupManager {

    private final AristTpa plugin;
    private final Map<UUID, ScheduledTask> warmupTasks = new HashMap<>();
    private final Map<UUID, Location> warmupLocations = new HashMap<>();
    private final Map<UUID, Integer> warmupCountdowns = new HashMap<>();

    public WarmupManager(AristTpa plugin) {
        this.plugin = plugin;
    }

    public void startWarmup(Player teleporter, Player destination, boolean toDestination) {
        UUID id = teleporter.getUniqueId();
        int seconds = plugin.getConfig().getInt("warmup", 5);

        warmupLocations.put(id, teleporter.getLocation().clone());
        warmupCountdowns.put(id, seconds);

        MessageUtil.sendChatList(teleporter, "teleporting_chat",
                s -> s.replace("{seconds}", String.valueOf(seconds)));
        MessageUtil.sendActionbar(teleporter, "teleporting_ab",
                s -> s.replace("{seconds}", String.valueOf(seconds)));

        ScheduledTask task = teleporter.getScheduler().runAtFixedRate(plugin, scheduledTask -> {
            if (!teleporter.isOnline()) {
                cancelWarmup(id);
                return;
            }

            int remaining = warmupCountdowns.getOrDefault(id, 0) - 1;

            if (remaining <= 0) {
                cancelWarmup(id);
                if (!destination.isOnline()) {
                    MessageUtil.sendChatList(teleporter, "target_offline");
                    return;
                }
                if (toDestination) {
                    teleporter.teleportAsync(destination.getLocation());
                } else {
                    destination.teleportAsync(teleporter.getLocation());
                }
                MessageUtil.sendChatList(teleporter, "teleport_success");
                MessageUtil.sendActionbar(teleporter, "teleport_success_ab");
                return;
            }

            warmupCountdowns.put(id, remaining);
            int rem = remaining;
            MessageUtil.sendActionbar(teleporter, "teleporting_ab",
                    s -> s.replace("{seconds}", String.valueOf(rem)));

        }, null, 20L, 20L);

        warmupTasks.put(id, task);
    }

    public void cancelWarmup(UUID playerId) {
        ScheduledTask task = warmupTasks.remove(playerId);
        if (task != null) task.cancel();
        warmupLocations.remove(playerId);
        warmupCountdowns.remove(playerId);
    }

    public boolean isInWarmup(Player player) {
        return warmupTasks.containsKey(player.getUniqueId());
    }

    public Location getWarmupLocation(Player player) {
        return warmupLocations.get(player.getUniqueId());
    }

    public void cancelAll() {
        warmupTasks.values().forEach(ScheduledTask::cancel);
        warmupTasks.clear();
        warmupLocations.clear();
        warmupCountdowns.clear();
    }
            }
