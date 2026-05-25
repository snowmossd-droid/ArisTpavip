package me.vennlmao.aristpa.commands;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.managers.TpaRequest;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpAcceptCommand implements CommandExecutor {

    private final AristTpa plugin;

    public TpAcceptCommand(AristTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        TpaRequest request = plugin.getRequestManager().getRequest(player);
        if (request == null) {
            MessageUtil.sendChatList(player, "no_pending_requests");
            return true;
        }

        Player requester = request.getSender();
        if (!requester.isOnline()) {
            MessageUtil.sendChatList(player, "requester_offline");
            plugin.getRequestManager().removeRequest(player);
            return true;
        }

        plugin.getRequestManager().removeRequest(player);

        if (request.getType() == TpaRequest.Type.TPA) {
            MessageUtil.sendActionbar(player, "request_accepted_tpa_receiver_ab",
                    s -> s.replace("{player}", requester.getName()));
            plugin.getWarmupManager().startWarmup(requester, player, true);
        } else {
            MessageUtil.sendActionbar(player, "request_accepted_tpahere_receiver_ab",
                    s -> s.replace("{player}", requester.getName()));
            plugin.getWarmupManager().startWarmup(player, requester, true);
        }

        return true;
    }
}
