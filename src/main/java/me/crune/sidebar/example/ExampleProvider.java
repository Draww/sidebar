package me.crune.sidebar.example;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.crune.sidebar.api.provider.Provider;
import me.crune.sidebar.api.scoreboard.provider.SidebarProvider;
import me.crune.sidebar.api.service.SidebarService;

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
            List<String> toReturn = Lists.newArrayList();

            toReturn.add("&7&m-------------------------------------");
            toReturn.add("&6Online Players&7: &f" + Bukkit.getOnlinePlayers().size());

            service.getImmutableCooldowns(player).forEach(boardCooldown -> {
                if (boardCooldown.hasEnded()) {
                    service.removeBoardCooldown(player, boardCooldown.getName());
                    return;
                }

                toReturn.add(boardCooldown.getDisplayName());
            });

            toReturn.add("");
            toReturn.add("&6www.cleo.rip");
            toReturn.add("&7&m-------------------------------------");
            return toReturn;
        };
    }

    @Override
    public Provider<Player, Boolean> isShown() {
        return player -> true;
    }
}
