package me.vennlmao.aristpa.listeners;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.managers.TpaRequest;
import me.vennlmao.aristpa.utils.MessageUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiListener implements Listener {

    private final AristTpa plugin;

    public GuiListener(AristTpa plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = PlainTextComponentSerializer.plainText()
                .serialize(event.getView().title());

        String guiTitle = plugin.getConfig().getString("gui.title", "Confirm Request");
        if (!title.equals(guiTitle)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();
        if (meta == null || meta.displayName() == null) return;

        String itemName = PlainTextComponentSerializer.plainText()
                .serialize(meta.displayName());

        String confirmName = strip(plugin.getConfig().getString("gui.items.confirm_tpa.name", "Confirm"));
        String denyName = strip(plugin.getConfig().getString("gui.items.cancel.name", "Deny"));

        TpaRequest request = plugin.getRequestManager().getRequest(player);

        if (itemName.equalsIgnoreCase(strip(confirmName))) {
            player.closeInventory();
            if (request == null) return;

            Player sender = request.getSender();
            if (!sender.isOnline()) {
                MessageUtil.sendChatList(player, "requester_offline");
                plugin.getRequestManager().removeRequest(player);
                return;
            }

            plugin.getRequestManager().removeRequest(player);

            if (request.getType() == TpaRequest.Type.TPA) {
                MessageUtil.sendActionbar(player, "request_accepted_tpa_receiver_ab",
                        s -> s.replace("{player}", sender.getName()));
                plugin.getWarmupManager().startWarmup(sender, player, true);
            } else {
                MessageUtil.sendActionbar(player, "request_accepted_tpahere_receiver_ab",
                        s -> s.replace("{player}", sender.getName()));
                plugin.getWarmupManager().startWarmup(player, sender, true);
            }

        } else if (itemName.equalsIgnoreCase(strip(denyName))) {
            player.closeInventory();
            if (request == null) return;

            Player sender = request.getSender();
            plugin.getRequestManager().removeRequest(player);

            MessageUtil.sendChatList(player, "request_denied_receiver");

            if (sender.isOnline()) {
                MessageUtil.sendChatList(sender, "request_denied_sender",
                        s -> s.replace("{player}", player.getName()));
            }
        }
    }

    private String strip(String s) {
        return s.replaceAll("&[0-9a-fk-or]", "")
                .replaceAll("#[0-9A-Fa-f]{6}", "")
                .trim();
    }
}
