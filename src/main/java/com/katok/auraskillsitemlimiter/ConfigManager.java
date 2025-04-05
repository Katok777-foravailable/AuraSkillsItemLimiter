package com.katok.auraskillsitemlimiter;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigManager {
    private final JavaPlugin plugin_instance;

    private YamlConfiguration config_cfg = new YamlConfiguration();
    private ConfigurationSection message_cfg;

    public static String color(String message) {
        if (message == null) {
            return "";
        }

        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = matcher.group(1);
            String replacement = ChatColor.of("#" + color).toString();
            message = message.replace("&#" + color, replacement);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public ConfigManager(JavaPlugin instance) {
        plugin_instance = instance;
    }

    public void load_configuration() {
        File config_file = new File(plugin_instance.getDataFolder(), "config.yml");

        if(!config_file.exists()) plugin_instance.saveResource("config.yml", false);

        config_cfg = YamlConfiguration.loadConfiguration(config_file);
        message_cfg = config_cfg.getConfigurationSection("messages");
    }

    public String get_message(String path) {
        if(message_cfg.isList(path)) {
            StringBuilder message = new StringBuilder();
            List<String> lines = message_cfg.getStringList(path);

            for(String line: lines) {
                message.append(color(line)).append(color("&r\n"));
            }
            return message.toString();
        }

        String message = message_cfg.getString(path);

        if(message == null) {
            return color("&cЗначение отсутствует&r");
        }

        return color(message);
    }

    public YamlConfiguration getConfig_cfg() {
        return config_cfg;
    }
}
