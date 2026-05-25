package me.vennlmao.aristpa;

import me.vennlmao.aristpa.commands.*;
import me.vennlmao.aristpa.listeners.DamageListener;
import me.vennlmao.aristpa.listeners.GuiListener;
import me.vennlmao.aristpa.listeners.MoveListener;
import me.vennlmao.aristpa.managers.RequestManager;
import me.vennlmao.aristpa.managers.TpautoActionbarManager;
import me.vennlmao.aristpa.managers.WarmupManager;
import me.vennlmao.aristpa.utils.MessageUtil;
import me.vennlmao.aristpa.utils.SoundUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class ArisTpa extends JavaPlugin {

    private static ArisTpa instance;
    private RequestManager requestManager;
    private WarmupManager warmupManager;
    private TpautoActionbarManager tpautoActionbarManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        requestManager = new RequestManager(this);
        warmupManager = new WarmupManager(this);
        tpautoActionbarManager = new TpautoActionbarManager(this);

        MessageUtil.init(this);
        SoundUtil.init(this);

        TpaCommand tpaCommand = new TpaCommand(this);
        getCommand("tpa").setExecutor(tpaCommand);
        getCommand("tpa").setTabCompleter(tpaCommand);

        TpaHereCommand tpaHereCommand = new TpaHereCommand(this);
        getCommand("tpahere").setExecutor(tpaHereCommand);
        getCommand("tpahere").setTabCompleter(tpaHereCommand);

        getCommand("tpaccept").setExecutor(new TpAcceptCommand(this));
        getCommand("tpacancel").setExecutor(new TpaCancelCommand(this));
        getCommand("tpauto").setExecutor(new TpautoCommand(this));
        getCommand("tpatoggle").setExecutor(new TpaToggleCommand(this));
        getCommand("tpaheretoggle").setExecutor(new TpaHereToggleCommand(this));

        getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
    }

    @Override
    public void onDisable() {
        warmupManager.cancelAll();
        tpautoActionbarManager.cancelAll();
    }

    public static ArisTpa getInstance() { return instance; }
    public RequestManager getRequestManager() { return requestManager; }
    public WarmupManager getWarmupManager() { return warmupManager; }
    public TpautoActionbarManager getTpautoActionbarManager() { return tpautoActionbarManager; }
}
