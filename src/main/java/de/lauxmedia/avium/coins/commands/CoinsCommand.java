package de.lauxmedia.avium.coins.commands;

import de.lauxmedia.avium.coins.Coins;
import de.lauxmedia.avium.coins.util.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CoinsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // check console
        if (!(sender instanceof Player) && args.length == 0) { sender.sendMessage("You need to be a Player to perform this command!"); return true; }
        // check for command
        if (command.getName().equalsIgnoreCase("coins")) {
            // check args
            if (args.length == 0) {
                Player player = (Player) sender;
                int coins = Coins.getApi().getCoins(player.getUniqueId().toString());
                sender.sendMessage("§7You currently have §6" + coins + " Coins §7in your bank account.");
                return true;
            }
            else if (!sender.hasPermission("system.coins")) {
                sender.sendMessage("§cPlease use: /coins");
                return true;
            }
            // set coins
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 3) {
                    String playerName = args[1];
                    int amount = Integer.parseInt(args[2]);
                    if (amount > 999999999) {
                        sender.sendMessage("§cSorry, the maximum amount of coins is 999999999");
                        return true;
                    }
                    else if (UUIDFetcher.getUUID(playerName) != null) {
                        UUID uuid = UUIDFetcher.getUUID(playerName);
                        sender.sendMessage("§7Coins for §a" + playerName.toLowerCase() + "§7 have been set to §6" + amount + "§7." );
                        Coins.getApi().setCoins(String.valueOf(uuid), amount);
                    }
                    else {
                        sender.sendMessage("§cThis player does not exist!");
                    }
                }
                else {
                    sender.sendMessage("§cPlease use: /coins set <player> <amount>");
                }
                return true;
            }
            // get coins
            else if (args[0].equalsIgnoreCase("get")) {
                if (args.length == 2) {
                    String playerName = args[1];
                    if (UUIDFetcher.getUUID(playerName) != null) {
                        UUID uuid = UUIDFetcher.getUUID(playerName);
                        int amount = Coins.getApi().getCoins(uuid.toString());
                        sender.sendMessage("§7Player §a" + playerName.toLowerCase() + "§7 currently has §6" + amount + "§7 Coins.");
                    }
                    else {
                        sender.sendMessage("§cThis player does not exist!");
                    }
                }
                else {
                    sender.sendMessage("§cPlease use: /coins get <player>");
                }
                return true;
            }
        }
        return true;
    }
}
