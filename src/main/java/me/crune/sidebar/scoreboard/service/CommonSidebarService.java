package me.crune.sidebar.scoreboard.service;

import me.crune.sidebar.api.scoreboard.provider.SidebarProvider;
import me.crune.sidebar.api.service.SidebarService;
import me.crune.sidebar.scoreboard.CommonSidebar;
import me.crune.sidebar.scoreboard.cooldown.Cooldown;
import me.crune.sidebar.scoreboard.provider.DefaultProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class CommonSidebarService implements SidebarService {

    private Map<UUID, CommonSidebar> boardMap;
    private final int task;
    private Plugin plugin;
    private SidebarProvider provider;

    public CommonSidebarService(Plugin plugin) {
        this(plugin, null);
    }

    public CommonSidebarService(Plugin plugin, SidebarProvider provider) {
        this.boardMap = new HashMap<>();
        this.plugin = plugin;
        this.provider = provider == null ? new DefaultProvider() : provider;

        plugin.getServer().getPluginManager().registerEvents(new SidebarListener(this), plugin);

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.task = scheduler.runTaskTimerAsynchronously(plugin, new SidebarTask(this), 2L, 2L).getTaskId();
    }

    @Override
    public void disable() {
        plugin.getServer().getScheduler().cancelTask(task);
    }

    @Override
    public void addCooldown(Player player, Cooldown cooldown) {
        boardMap.get(player.getUniqueId()).addCooldown(cooldown);
    }

    @Override
    public void removeCooldown(Player player, String name) {
        boardMap.get(player.getUniqueId()).removeCooldown(name);
    }

    @Override
    public Set<Cooldown> getCooldowns(Player player) {
        return boardMap.get(player.getUniqueId()).getCooldowns();
    }

    @Override
    public void setProvider(SidebarProvider provider) {
        this.provider = provider;
    }

    public Map<UUID, CommonSidebar> getBoardMap() {
        return new HashMap<>(boardMap);
    }

    Plugin getPlugin() {
        return plugin;
    }

    SidebarProvider getProvider() {
        return provider;
    }

    void startProviding(Player player) {
        boardMap.put(player.getUniqueId(), new CommonSidebar(player));
    }

    void stopProvider(UUID uuid) {
        CommonSidebar commonSidebar = boardMap.get(uuid);
        boardMap.remove(uuid);
        commonSidebar.getCooldowns().forEach(cooldown -> {
            commonSidebar.removeCooldown(cooldown.getName());
        });
    }
}
