package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.Warp;
import net.niftymonkey.niftywarp.WarpType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * User: Mark
 * Date: 6/13/11
 * Time: 2:34 AM
 */
public class AddWarpCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new addwarp command instance
     *
     * @param plugin The base plugin
     */
    public AddWarpCommand(NiftyWarp plugin)
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
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_ADD, false, true))
        {
            // make sure we have one argument, which is the name for the warp we're about to add
            if (args.length > 0)
            {
                // get the first argument which is the warp name
                String warpName = args[0];
                // get a default warp type
                //WarpType warpType = WarpType.getDefaultWarpType(plugin.getConfiguration()); //this needs to be fixed once warp type fixed
                WarpType warpType = WarpType.getDefaultWarpType();

                // if we have two arguments, they're passing in warp type
                if(args.length == 2)
                {
                    // get the second argument which is the warp type
                    String warpTypeStr = args[1];
                    // get a type that matches the param
                    WarpType warpParamType = WarpType.getTypeForString(warpTypeStr);
                    if(warpParamType != null)
                        warpType = warpParamType;
                }

                int maxWarps = plugin.getConfig().getInt(AppStrings.PROPERTY_WARP_MAXWARPS, 20);
                int warpsForPlayerCount = plugin.getWarpManager().getPersistenceProvider().getWarpsForPlayerCount(player);

                // get the addon message prefix
                String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

                if(maxWarps > warpsForPlayerCount)
                {
                    // add this warp to the warp map
                    Warp warp = plugin.getWarpManager().addWarp(warpName,
                                                                player,
                                                                warpType,
                                                                player.getLocation());


                    String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.WARP_ADDED);
                    Object[] formatValues = new Object[] { warp.getWarpType().getTypeColor() + warpName };
                    String message = MessageFormat.format(msgFromBundle, formatValues);

                    // let them know that we successfully created the warp
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.GREEN + message);
                }
                else
                {
                    String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.ERR_ALL_WARP_SLOTS_USED);
                    Object[] formatValues = new Object[] { maxWarps };
                    String message = MessageFormat.format(msgFromBundle, formatValues);

                    // let them know they aren't allowed to make any more
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.RED + message);
                }

                retVal = true;
            }
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}

