package me.vennlmao.aristpa;

import me.vennlmao.aristpa.commands.*;
import me.vennlmao.aristpa.listeners.DamageListener;
import me.vennlmao.aristpa.listeners.MoveListener;
import me.vennlmao.aristpa.managers.RequestManager;
import me.vennlmao.aristpa.managers.WarmupManager;
import me.vennlmao.aristpa.utils.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class AristTpa extends JavaPlugin {

    private static AristTpa instance;
    private RequestManager requestManager;
    private WarmupManager warmupManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        requestManager = new RequestManager(this);
        warmupManager = new WarmupManager(this);

        MessageUtil.init(this);

        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpahere").setExecutor(new TpaHereCommand(this));
        getCommand("tpaccept").setExecutor(new TpAcceptCommand(this));
        getCommand("tpacancel").setExecutor(new TpaCancelCommand(this));
        getCommand("tpauto").setExecutor(new TpautoCommand(this));
        getCommand("tpatoggle").setExecutor(new TpaToggleCommand(this));
        getCommand("tpaheretoggle").setExecutor(new TpaHereToggleCommand(this));

        getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new me.vennlmao.aristpa.listeners.GuiListener(this), this);
    }

    @Override
    public void onDisable() {
        warmupManager.cancelAll();
    }

    public static AristTpa getInstance() {
        return instance;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public WarmupManager getWarmupManager() {
        return warmupManager;
    }
}
