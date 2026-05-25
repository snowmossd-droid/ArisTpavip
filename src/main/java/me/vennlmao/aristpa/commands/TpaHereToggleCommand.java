package me.vennlmao.aristpa.commands;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaHereToggleCommand implements CommandExecutor {

    private final AristTpa plugin;

    public TpaHereToggleCommand(AristTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        boolean current = plugin.getRequestManager().isTpahereDisabled(player);
        boolean nowDisabled = !current;
        plugin.getRequestManager().setTpahereDisabled(player, nowDisabled);

        if (!nowDisabled) {
            MessageUtil.sendChatList(player, "tpaheretoggle_enabled");
            MessageUtil.sendActionbar(player, "tpaheretoggle_enabled_ab");
        } else {
            MessageUtil.sendChatList(player, "tpaheretoggle_disabled");
            MessageUtil.sendActionbar(player, "tpaheretoggle_disabled_ab");
        }

        return true;
    }
}
