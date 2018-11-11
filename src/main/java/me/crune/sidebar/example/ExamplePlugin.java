package me.crune.sidebar.example;

import me.crune.sidebar.api.service.SidebarService;
import me.crune.sidebar.scoreboard.cooldown.Cooldown;
import me.crune.sidebar.scoreboard.service.CommonSidebarService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private SidebarService service;

    @Override
    public void onEnable() {
        this.service = new CommonSidebarService(this);
        this.service.setProvider(new ExampleProvider(this.service));

        getCommand("test-cooldown").setExecutor(this);
    }

    @Override
    public void onDisable() {
        this.service.disable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Player only!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /test-cooldown <time>");
            return true;
        }

        try {
            Cooldown cooldown = new Cooldown("test", "&aTest&7: &f", args[0]);
            this.service.addBoardCooldown((Player) sender, cooldown);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid Date! Example: \"/test-cooldown 10minute,5second\"");
        }

        return true;
    }
}
