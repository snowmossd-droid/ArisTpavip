package me.vennlmao.aristpa.commands;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpautoCommand implements CommandExecutor {

    private final AristTpa plugin;

    public TpautoCommand(AristTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        boolean current = plugin.getRequestManager().isTpautoEnabled(player);
        boolean newState = !current;
        plugin.getRequestManager().setTpauto(player, newState);

        if (newState) {
            MessageUtil.sendChatList(player, "tpauto_enabled");
            MessageUtil.sendActionbar(player, "tpauto_enabled_ab");
        } else {
            MessageUtil.sendChatList(player, "tpauto_disabled");
            MessageUtil.sendActionbar(player, "tpauto_disabled_ab");
        }

        return true;
    }
}
