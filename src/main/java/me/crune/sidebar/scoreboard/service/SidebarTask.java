package me.crune.sidebar.scoreboard.service;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SidebarTask implements Runnable {

    private CommonSidebarService service;

    public SidebarTask(CommonSidebarService service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.getBoardMap().forEach((uuid, sidebar) -> {
            if (isOnline(uuid)) {
                Player player = Bukkit.getPlayer(uuid);

                if (service.getProvider().isShown().get(player)) {
                    if (!sidebar.isShown()) {
                        sidebar.show();
                    }

                    sidebar.updateTitle(service.getProvider().getTitle().get(player));
                    sidebar.updateList(service.getProvider().getLines().get(player));
                } else {
                    sidebar.hide();
                }
            }
        });
    }

    private boolean isOnline(UUID uuid) {
        Server server = service.getPlugin().getServer();
        return server.getPlayer(uuid) != null && server.getPlayer(uuid).isOnline();
    }
}
