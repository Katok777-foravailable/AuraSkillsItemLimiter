package com.katok.auraskillsitemlimiter;

import com.katok.auraskillsitemlimiter.Listeners.BreakBlock;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuraSkillsItemLimiter extends JavaPlugin {
    private ConfigManager cfg_manager;

    @Override
    public void onEnable() {
        cfg_manager = new ConfigManager(this);
        cfg_manager.load_configuration();

        Bukkit.getConsoleSender().sendMessage(ConfigManager.color("[" + getName() + "] &9&lплагин специально создан для братишки Мрака - @Marsek1700 :3"));

        Bukkit.getPluginManager().registerEvents(new BreakBlock(cfg_manager), this);
    }
}
