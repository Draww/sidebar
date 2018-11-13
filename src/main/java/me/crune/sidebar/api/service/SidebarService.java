package me.crune.sidebar.api.service;

import me.crune.sidebar.api.scoreboard.provider.SidebarProvider;
import me.crune.sidebar.scoreboard.cooldown.Cooldown;
import org.bukkit.entity.Player;

import java.util.Set;

public interface SidebarService {

    void disable();

    void addCooldown(Player player, Cooldown cooldown);

    void removeCooldown(Player player, String name);

    Set<Cooldown> getCooldowns(Player player);

    void setProvider(SidebarProvider provider);

}
