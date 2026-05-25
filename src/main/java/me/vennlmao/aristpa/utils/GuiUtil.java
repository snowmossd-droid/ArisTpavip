package me.vennlmao.aristpa.utils;

import me.vennlmao.aristpa.AristTpa;
import me.vennlmao.aristpa.managers.TpaRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiUtil {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static Inventory buildRequestGui(AristTpa plugin, TpaRequest request) {
        int size = plugin.getConfig().getInt("gui.size", 27);
        String title = plugin.getConfig().getString("gui.title", "Confirm Request");
        Inventory inv = Bukkit.createInventory(null, size, MM.deserialize(title));

        Player sender = request.getSender();
        String type = request.getType() == TpaRequest.Type.TPA ? "confirm_tpa" : "confirm_tpahere";

        ItemStack confirm = buildItem(plugin, "gui.items." + type,
                s -> s.replace("{player}", sender.getName()));
        ItemStack cancel = buildItem(plugin, "gui.items.cancel", s -> s);
        ItemStack location = buildItem(plugin, "gui.items.location",
                s -> s.replace("{world}", sender.getWorld().getName()));
        ItemStack region = buildItem(plugin, "gui.items.region",
                s -> s.replace("{playerPing}", String.valueOf(sender.getPing())));
        ItemStack head = buildHead(plugin, sender);

        inv.setItem(10, confirm);
        inv.setItem(12, location);
        inv.setItem(13, head);
        inv.setItem(14, region);
        inv.setItem(16, cancel);

        return inv;
    }

    private static ItemStack buildItem(AristTpa plugin, String path, java.util.function.UnaryOperator<String> replacer) {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection(path);
        if (sec == null) return new ItemStack(Material.STONE);

        Material mat = Material.matchMaterial(sec.getString("material", "STONE"));
        if (mat == null) mat = Material.STONE;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        String name = sec.getString("name", "");
        meta.displayName(parseColor(replacer.apply(name)));

        List<String> rawLore = sec.getStringList("lore");
        List<Component> lore = new ArrayList<>();
        for (String l : rawLore) {
            lore.add(parseColor(replacer.apply(l)));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack buildHead(AristTpa plugin, Player player) {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("gui.items.playerhead");
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) return item;

        meta.setOwningPlayer(player);

        if (sec != null) {
            String name = sec.getString("name", "");
            meta.displayName(parseColor(name.replace("{playerName}", player.getName())));

            List<String> rawLore = sec.getStringList("lore");
            List<Component> lore = new ArrayList<>();
            for (String l : rawLore) {
                lore.add(parseColor(l.replace("{playerName}", player.getName())));
            }
            meta.lore(lore);
        }

        item.setItemMeta(meta);
        return item;
    }

    private static Component parseColor(String raw) {
        String s = raw;
        s = s.replaceAll("#([0-9A-Fa-f]{6})", "<color:#$1>");
        s = s.replace("&a", "<green>").replace("&b", "<aqua>").replace("&c", "<red>")
                .replace("&d", "<light_purple>").replace("&e", "<yellow>").replace("&f", "<white>")
                .replace("&7", "<gray>").replace("&6", "<gold>").replace("&4", "<dark_red>")
                .replace("&2", "<dark_green>").replace("&l", "<bold>").replace("&o", "<italic>")
                .replace("&n", "<underlined>").replace("&m", "<strikethrough>")
                .replace("&k", "<obfuscated>").replace("&r", "<reset>");
        return MM.deserialize(s);
    }
}
