package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * User: Mark
 * Date: 6/30/11
 * Time: 12:42 AM
 */
public class HomeCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new home command instance
     *
     * @param plugin The base plugin
     */
    public HomeCommand(NiftyWarp plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Called when a command is sent
     *
     * @param sender  The sender (aka player)
     * @param command The command
     * @param label   The label
     * @param args    The arguments used
     * @return Whether the command succeeded or not
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean retVal = false;

        // Cast to the player object
        Player player = (Player) sender;

        // Check permission
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_HOME, false, true))
        {
            // find that in the warp map
            Warp warp = plugin.getWarpManager().getWarp(AppStrings.HOME_WARP_NAME, player);

            // get the addon message prefix
            String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

            if (warp != null)
            {
                plugin.getWarpManager().sendPlayerToWarp(player, warp.getLocation(), player);

                String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.WARPED_TO);
                Object[] formatValues = new Object[] { warp.getWarpType().getTypeColor() + AppStrings.HOME_WARP_NAME };
                String message = MessageFormat.format(msgFromBundle, formatValues);

                // let them know it worked
                player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                   ChatColor.GREEN + message);
            }
            else
            {
                String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.ERR_WARP_NOT_FOUND);
                Object[] formatValues = new Object[] { AppStrings.HOME_WARP_NAME };
                String message = MessageFormat.format(msgFromBundle, formatValues);

                // let them know that we couldn't find that warp
                player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                   ChatColor.RED + message);
            }

            retVal = true;
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}