package me.crune.sidebar.api.service;

import com.google.common.collect.ImmutableSet;
import me.crune.sidebar.api.scoreboard.provider.SidebarProvider;
import org.bukkit.entity.Player;
import me.crune.sidebar.scoreboard.cooldown.Cooldown;

public interface SidebarService {

    void disable();

    void addBoardCooldown(Player player, Cooldown cooldown);

    void removeBoardCooldown(Player player, String name);

    ImmutableSet<Cooldown> getImmutableCooldowns(Player player);

    void setProvider(SidebarProvider provider);

}
