package me.crune.sidebar.example;

import me.crune.sidebar.api.provider.Provider;
import me.crune.sidebar.api.scoreboard.provider.SidebarProvider;
import me.crune.sidebar.api.service.SidebarService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExampleProvider implements SidebarProvider {

    private SidebarService service;

    public ExampleProvider(SidebarService service) {
        this.service = service;
    }

    @Override
    public Provider<Player, String> getTitle() {
        return player -> "&6&lExample";
    }

    @Override
    public Provider<Player, List<String>> getLines() {
        return player -> {
            List<String> toReturn = new ArrayList<>();

            toReturn.add("&7&m-------------------------------------");
            toReturn.add("&6Online Players&7: &f" + Bukkit.getOnlinePlayers().size());

            service.getCooldowns(player).forEach(boardCooldown -> {
                if (boardCooldown.hasEnded()) {
                    service.removeCooldown(player, boardCooldown.getName());
                    return;
                }

                toReturn.add(boardCooldown.getDisplayName());
            });

            toReturn.add("");
            toReturn.add("&6Crune is awesome!");
            toReturn.add("&7&m-------------------------------------");
            return toReturn;
        };
    }

    @Override
    public Provider<Player, Boolean> isShown() {
        return player -> true;
    }
}
