package com.redstoner.bungee.joinmessages;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

public class JoinMessages
  extends Plugin
  implements Listener
{
  private String loginString = "&a&l+ &7%s";
  private String logoutString = "&c&l- &7%s";
  
  public void onEnable()
  {
    getProxy().registerChannel("JoinMessage");
    getProxy().getPluginManager().registerCommand(this, new CommandGoto());
    getProxy().getPluginManager().registerListener(this, this);
  }
  
  @EventHandler
  public void onServerConnect(ServerConnectEvent e)
  {
    ServerInfo target = e.getTarget();
    ProxiedPlayer jp = e.getPlayer();
    if (CommandGoto.justSwitched.contains(jp)) {
      return;
    }
    String joinMessage = ChatColor.translateAlternateColorCodes('&', String.format(this.loginString, new Object[] { jp.getName() }));
    for (ProxiedPlayer p : target.getPlayers()) {
      p.sendMessage(joinMessage);
    }
    jp.sendMessage(joinMessage);
  }
  
  @EventHandler
  public void onServerDisconnect(ServerDisconnectEvent e)
  {
    ServerInfo target = e.getTarget();
    ProxiedPlayer jp = e.getPlayer();
    if (CommandGoto.justSwitched.contains(jp))
    {
      CommandGoto.justSwitched.remove(jp);
      return;
    }
    String leaveMessage = ChatColor.translateAlternateColorCodes('&', String.format(this.logoutString, new Object[] { jp.getName() }));
    for (ProxiedPlayer p : target.getPlayers()) {
      p.sendMessage(leaveMessage);
    }
  }
}
