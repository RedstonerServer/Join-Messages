package com.redstoner.bungee.joinmessages;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

public class CommandGoto
  extends Command
{
  private String switchto = "&c&l- &7%s &o(to %s)";
  private String switchfrom = "&a&l+ &7%s &o(from %s)";
  public static List<ProxiedPlayer> justSwitched = new ArrayList();
  
  public CommandGoto()
  {
    super("goto", "rser.command.goto", new String[] { "join", "server" });
  }
  
  public void execute(CommandSender sender, String[] args)
  {
    if (args.length == 0)
    {
      sendMessage(sender, true, "Ussuage: &e/goto <server>");
      return;
    }
    if (!(sender instanceof ProxiedPlayer))
    {
      sendMessage(sender, true, "This command can only be run by players!");
      return;
    }
    ServerInfo from = ((ProxiedPlayer)sender).getServer().getInfo();
    ServerInfo target = ProxyServer.getInstance().getServerInfo(args[0]);
    if (target == null)
    {
      sendMessage(sender, true, "That server doesn't exist!");
    }
    else if (from.getName().equals(target.getName()))
    {
      sendMessage(sender, true, "You're already connected to that server!");
    }
    else
    {
      ProxiedPlayer s = (ProxiedPlayer)sender;
      
      String fromMessage = String.format(this.switchfrom, new Object[] { sender.getName(), from.getName() });
      for (ProxiedPlayer p : target.getPlayers()) {
        sendRawMessage(p, fromMessage);
      }
      String toMessage = String.format(this.switchto, new Object[] { sender.getName(), target.getName() });
      for (ProxiedPlayer p : from.getPlayers()) {
        sendRawMessage(p, toMessage);
      }
      sendMessage(sender, false, "&6Connecting you to &e" + target.getName());
      sendRawMessage(s, fromMessage);
      justSwitched.add(s);
      s.connect(target);
    }
  }
  
  private void sendMessage(CommandSender sender, boolean error, String msg)
  {
    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&" + (error ? "c" : "2") + "Bungee&8]&7 " + msg));
  }
  
  private void sendRawMessage(ProxiedPlayer p, String msg)
  {
    p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
  }
}
