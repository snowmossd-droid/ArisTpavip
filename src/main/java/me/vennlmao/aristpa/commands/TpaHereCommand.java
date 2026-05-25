package me.vennlmao.aristpa.commands;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.managers.TpaRequest;
import me.vennlmao.aristpa.utils.GuiUtil;
import me.vennlmao.aristpa.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TpaHereCommand implements CommandExecutor {

    private final AristTpa plugin;
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public TpaHereCommand(AristTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length == 0) return true;

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            MessageUtil.sendChatList(player, "target_offline");
            return true;
        }

        if (target.equals(player)) return true;

        if (plugin.getRequestManager().isTpahereDisabled(target)) {
            MessageUtil.sendChatList(player, "target_tpahere_disabled",
                    s -> s.replace("{player}", target.getName()));
            MessageUtil.sendActionbar(player, "target_tpahere_disabled_ab",
                    s -> s.replace("{player}", target.getName()));
            return true;
        }

        TpaRequest request = new TpaRequest(player, target, TpaRequest.Type.TPAHERE);
        plugin.getRequestManager().addRequest(request);

        MessageUtil.sendChatList(player, "request_sent_tpahere",
                s -> s.replace("{player}", target.getName()));
        MessageUtil.sendActionbar(player, "request_sent_tpahere_ab",
                s -> s.replace("{player}", target.getName()));

        if (plugin.getRequestManager().isTpautoEnabled(target)) {
            plugin.getWarmupManager().startWarmup(target, player, true);
            return true;
        }

        sendClickableRequest(target, player, "accept_tpahere");
        MessageUtil.sendChatList(target, "request_received_tpahere",
                s -> s.replace("{player}", player.getName()));

        target.getScheduler().run(plugin, t -> {
            target.openInventory(GuiUtil.buildRequestGui(plugin, request));
        }, null);

        return true;
    }

    private void sendClickableRequest(Player receiver, Player requester, String key) {
        List<String> lines = plugin.getConfig().getStringList("clickable_messages." + key + ".text");
        for (String line : lines) {
            String replaced = line.replace("{player}", requester.getName());
            String colored = replaced.replaceAll("#([0-9A-Fa-f]{6})", "<color:#$1>")
                    .replace("&7", "<gray>").replace("&a", "<green>");
            Component component = MM.deserialize(colored)
                    .clickEvent(ClickEvent.runCommand("/tpaccept " + requester.getName()));
            receiver.sendMessage(component);
        }
    }
}
