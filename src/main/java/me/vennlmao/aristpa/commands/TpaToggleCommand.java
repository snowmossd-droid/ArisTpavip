package me.vennlmao.aristpa.commands;

import me.vennlmao.aristpa.ArisTpa;
import me.vennlmao.aristpa.utils.MessageUtil;
import me.vennlmao.aristpa.utils.SoundUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaToggleCommand implements CommandExecutor {

    private final ArisTpa plugin;

    public TpaToggleCommand(ArisTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        boolean nowDisabled = !plugin.getRequestManager().isTpaDisabled(player);
        plugin.getRequestManager().setTpaDisabled(player, nowDisabled);

        if (!nowDisabled) {
            MessageUtil.sendChatList(player, "tpatoggle_enabled");
            MessageUtil.sendActionbar(player, "tpatoggle_enabled_ab");
            SoundUtil.play(player, "toggle_on");
        } else {
            MessageUtil.sendChatList(player, "tpatoggle_disabled");
            MessageUtil.sendActionbar(player, "tpatoggle_disabled_ab");
            SoundUtil.play(player, "toggle_off");
        }

        return true;
    }
}
