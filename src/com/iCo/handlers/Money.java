package com.iCo.handlers;

import com.iCo.Constants;
import com.iCo.command.Handler;
import com.iCo.command.Parser.Argument;
import com.iCo.command.exceptions.InvalidUsage;
import com.iCo.iConomy;
import com.iCo.system.Account;
import com.iCo.system.Accounts;
import com.iCo.util.Messaging;
import com.iCo.util.Template;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class Money extends Handler {

    private Accounts Accounts = new Accounts();

    public Money(iConomy plugin) {
        super(plugin, plugin.Template);
    }

    @Override
    public boolean perform(CommandSender sender, LinkedHashMap<String, Argument> arguments) throws InvalidUsage {
        if(Constants.Nodes.useHoldingsPermission.getBoolean())
            if(!hasPermissions(sender, "money"))
                throw new InvalidUsage("You do not have permission to do that.");

        String name = arguments.get("name").getStringValue();
        String tag = template.color(Template.Node.TAG_MONEY);

        if(name.equals("0")) {
            if(isConsole(sender)) {
                Messaging.send(sender, "`rCannot check money on non-living organism.");
                return false;
            }

            Player player = (Player) sender;

            if(player == null)
                return false;

            Account account = new Account(player.getName(), player.getUniqueId());
            account.getHoldings().showBalance(null);
            return false;
        }

        if(!hasPermissions(sender, "money+"))
            throw new InvalidUsage("You do not have permission to do that.");

        if(!Accounts.exists(name)) {
            template.set(Template.Node.ERROR_ACCOUNT);
            template.add("name", name);

            Messaging.send(sender, tag + template.parse());
            return false;
        }

        Account account = new Account(name);
        account.getHoldings().showBalance(sender);
        return false;
    }
}
