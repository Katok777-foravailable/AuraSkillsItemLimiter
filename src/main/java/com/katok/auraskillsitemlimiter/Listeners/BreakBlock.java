package com.katok.auraskillsitemlimiter.Listeners;

import com.katok.auraskillsitemlimiter.ConfigManager;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreakBlock implements Listener {
    private final ConfigManager cfg_manager;
    private final AuraSkillsApi aura_skills = AuraSkillsApi.get();

    public BreakBlock(ConfigManager cfg_manager) {
        this.cfg_manager = cfg_manager;
    }

    public String format(String message, String skill_name, int skill) {
        String result = message;

        Pattern pattern = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(message);
        while(matcher.find()){
            String propName = matcher.group(1);
            switch (propName) {
                case "skill_name":
                    propName = skill_name;
                    break;
                case "skill":
                    propName = String.valueOf(skill);
                    break;
            }
            result = result.replace(matcher.group(), propName);
        }
        return result;
    }

    @EventHandler
    public void on_break(BlockBreakEvent e) {
        SkillsUser user = aura_skills.getUser(e.getPlayer().getUniqueId());

        String[] keys = e.getPlayer().getInventory().getItemInMainHand().getType().translationKey().split("\\.");
        String key = keys[keys.length - 1].toUpperCase();

        for(String label: cfg_manager.getConfig_cfg().getConfigurationSection("items").getKeys(false)) {
            if(!key.equalsIgnoreCase(label)) return;

            for(String skill: cfg_manager.getConfig_cfg().getConfigurationSection("items." + label).getKeys(false)) {
                if(user.getSkillLevel(Skills.valueOf(skill)) < cfg_manager.getConfig_cfg().getInt("items." + label + "." + skill)) {
                    String skill_loc = cfg_manager.getConfig_cfg().getString("replace." + skill);

                    if(skill_loc == null) skill_loc = skill;

                    e.getPlayer().sendMessage(format(cfg_manager.get_message("you-cant"), ConfigManager.color(skill_loc), cfg_manager.getConfig_cfg().getInt("items." + label + "." + skill)));
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
