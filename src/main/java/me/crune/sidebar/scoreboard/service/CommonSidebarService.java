package me.crune.sidebar.scoreboard.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import me.crune.sidebar.api.scoreboard.provider.SidebarProvider;
import me.crune.sidebar.api.service.SidebarService;
import me.crune.sidebar.scoreboard.CommonSidebar;
import me.crune.sidebar.scoreboard.cooldown.Cooldown;
import me.crune.sidebar.scoreboard.provider.DefaultProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Map;
import java.util.UUID;

public final class CommonSidebarService implements SidebarService {

    private Map<UUID, CommonSidebar> boardMap;
    private final int task;
    private Plugin plugin;
    private SidebarProvider provider;

    private CommonSidebarService() {
        task = -1;
    }

    public CommonSidebarService(Plugin plugin) {
        this(plugin, null);
    }

    public CommonSidebarService(Plugin plugin, SidebarProvider provider) {
        this.boardMap = Maps.newHashMap();
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
    public void addBoardCooldown(Player player, Cooldown cooldown) {
        getImmutableBoardMap().get(player.getUniqueId()).addCooldown(cooldown);
    }

    @Override
    public void removeBoardCooldown(Player player, String name) {
        getImmutableBoardMap().get(player.getUniqueId()).removeCooldown(name);
    }

    @Override
    public ImmutableSet<Cooldown> getImmutableCooldowns(Player player) {
        return getImmutableBoardMap().get(player.getUniqueId()).getImmutableCooldowns();
    }

    @Override
    public void setProvider(SidebarProvider provider) {
        this.provider = provider;
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
        commonSidebar.getImmutableCooldowns().forEach(boardCooldown -> {
            commonSidebar.removeCooldown(boardCooldown.getName());
        });
    }

    ImmutableMap<UUID, CommonSidebar> getImmutableBoardMap() {
        return ImmutableMap.copyOf(boardMap);
    }
}
