package org.pronze.hypixelify.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.pronze.hypixelify.message.Messages;
import org.pronze.hypixelify.utils.ShopUtil;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.game.TeamColor;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.Arrays;
import java.util.List;

public class ShoutCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED +  "This Command can only be done as a Player!");
            return true;
        }

        if(args.length < 1){
            sender.sendMessage(ChatColor.RED + "Invalid usage, /shout {message} is the format!");
            return true;
        }

        Player player = (Player) sender;

        if(!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player)){
            ShopUtil.sendMessage(player, Messages.message_not_in_game);
            return true;
        }

        Game game = BedwarsAPI.getInstance().getGameOfPlayer(player);

        if(game.getTeamOfPlayer(player) == null){
            player.sendMessage(ChatColor.RED + "You cannot do this command while spectating");
            return true;
        }

        RunningTeam team = game.getTeamOfPlayer(player);
        String color = TeamColor.valueOf(team.getColor().name()).chatColor.toString();

        String st = Messages.shoutFormat
                .replace("{color}", color)
                .replace("{team}", team.getName())
                .replace("{player}", player.getName())
                .replace("{message}", Arrays.toString(args));

        for(Player pl : game.getConnectedPlayers()){
            pl.sendMessage(st);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}