package me.vennlmao.aristpa.commands;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.managers.TpaRequest;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaCancelCommand implements CommandExecutor {

    private final AristTpa plugin;

    public TpaCancelCommand(AristTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (plugin.getWarmupManager().isInWarmup(player)) {
            plugin.getWarmupManager().cancelWarmup(player.getUniqueId());
            MessageUtil.sendChatList(player, "request_cancelled");
            return true;
        }

        TpaRequest request = plugin.getRequestManager().getRequestBySender(player);
        if (request == null) {
            MessageUtil.sendChatList(player, "no_pending_requests");
            return true;
        }

        Player receiver = request.getReceiver();
        plugin.getRequestManager().removeRequestBySender(player);

        MessageUtil.sendChatList(player, "outgoing_request_cancelled");

        if (receiver.isOnline()) {
            MessageUtil.sendChatList(receiver, "incoming_request_cancelled",
                    s -> s.replace("{player}", player.getName()));
        }

        return true;
    }
}
