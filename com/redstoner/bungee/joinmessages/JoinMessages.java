package com.redstoner.bungee.joinmessages;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class JoinMessages extends Plugin implements Listener {
	
  private String loginString = "&a&l+ &7%s";
  private String logoutString = "&c&l- &7%s";
  
  public void onEnable() {
    getProxy().registerChannel("JoinMessage");
    getProxy().getPluginManager().registerCommand(this, new CommandServer());
    getProxy().getPluginManager().registerListener(this, this);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  @SuppressWarnings("deprecation")
  public void onServerConnect(ServerConnectEvent e) {
	  
	if (e.isCancelled())
		return;
	
    ServerInfo target = e.getTarget();
    ProxiedPlayer jp = e.getPlayer();
    
    if (CommandServer.justSwitched.contains(jp))
      return;
    
    String joinMessage = ChatColor.translateAlternateColorCodes('&',
    		                String.format(this.loginString, new Object[] { jp.getName() }));
    
    for (ProxiedPlayer p : target.getPlayers())
      p.sendMessage(joinMessage);
    
    jp.sendMessage(joinMessage);
  }
  
  @EventHandler
  @SuppressWarnings("deprecation")
  public void onServerDisconnect(ServerDisconnectEvent e) {
	  
    ServerInfo target = e.getTarget();
    ProxiedPlayer jp = e.getPlayer();
    
    if (CommandServer.justSwitched.contains(jp)) {
      CommandServer.justSwitched.remove(jp);
      return;
    }
    
    String leaveMessage = ChatColor.translateAlternateColorCodes('&',
    		                 String.format(this.logoutString, new Object[] { jp.getName() }));
    
    for (ProxiedPlayer p : target.getPlayers())
      p.sendMessage(leaveMessage);
  }
}
