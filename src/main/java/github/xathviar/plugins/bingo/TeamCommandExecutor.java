package github.xathviar.plugins.bingo;

import de.laserlord.minecraft.teamsystem.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommandExecutor implements CommandExecutor {
    private Startup plugin;

    public TeamCommandExecutor(Startup plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (command.getName().equalsIgnoreCase("team")) {
                if (args.length == 0) {
                    commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " With /team help you get further information.");
                } else if (args[0].equalsIgnoreCase("help")) {
                    //TODO
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (args.length > 1) {
                        if (args.length == 2) {
                            plugin.teamManager.registerTeam(args[1]);
                            commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " You successfully created team " + ChatColor.YELLOW + args[1] + ChatColor.RESET);
                        } else if (args.length == 3) {
                            plugin.teamManager.registerTeam(args[1], args[2]);
                            commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " You successfully created team " + ChatColor.YELLOW + args[1] + ChatColor.RESET);
                        } else if (args.length == 4) {
                            commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " This feature has to be finished! Created team with name and prefix");
                            plugin.teamManager.registerTeam(args[1], args[2]);
                            commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " You successfully created team " + ChatColor.YELLOW + args[1] + ChatColor.RESET);
                        }
                    } else {
                        plugin.teamManager.registerTeam();
                        commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " You successfully created team " + ChatColor.YELLOW + "#" + (plugin.teamManager.getNextTeam() - 1) + ChatColor.RESET);
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (args.length > 1) {
                        if (args.length == 2) {
                            Team team = plugin.teamManager.getTeamByName(args[1]);
                            if (team != null) {
                                StringBuilder names = new StringBuilder();
                                for (Player player : team.getPlayerList()) {
                                    names.append(player.getDisplayName()).append("\n    ");
                                }
                                commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " Teamname: " + ChatColor.YELLOW + team.getTeamName() + ChatColor.RESET + "\n  Teammitglieder: " + ChatColor.YELLOW + names.toString() + ChatColor.RESET);
                            } else {
                                commandSender.sendMessage(ChatColor.GREEN + "[Teams]" + ChatColor.RESET + " This team does not exist." + ChatColor.RESET);
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("You need to be a player to execute this command.");
        }

        return false;
    }
}
