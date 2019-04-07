package com.redstoner.bungee.joinmessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandServer extends Command {
	
  private String switchto = "&c&l- &7%s &o(to %s)";
  private String switchfrom = "&a&l+ &7%s &o(from %s)";
  
  public static List<ProxiedPlayer> justSwitched = new ArrayList<>();
  
  public CommandServer() {
    super("server", "rser.command.server", new String[] { "join", "goto" });
  }
  
  @SuppressWarnings("deprecation")
  public void execute(CommandSender sender, String[] args) {
    
	if (args.length == 0) {
		Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
    	
    	if ((sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§8[§2Bungee§8]§7 You are currently connected to §a" + ((ProxiedPlayer)sender).getServer().getInfo().getName() );
          }
          ComponentBuilder serverList = new ComponentBuilder("").append(TextComponent.fromLegacyText("§8[§2Bungee§8]§7 You can connect to the following servers: "));
          boolean first = true;
          for (ServerInfo server : servers.values()) {
            if (server.canAccess(sender))
            {
              TextComponent serverTextComponent = new TextComponent((!first?"§7, §e": "§e") + server.getName());
              int count = server.getPlayers().size();
              serverTextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(count + (count == 1 ? " player" : " players") + "\n")
              
                .append("Click to connect to the server").italic(true)
                .create()));
              serverTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server.getName()));
              serverList.append(serverTextComponent);

            }
            first = false;
          }
          sender.sendMessage(serverList.create());
          return;
    }
	
    if (!(sender instanceof ProxiedPlayer)) {
    	sendMessage(sender, true, "You must be a player!");
    	return;
    }
    
    ServerInfo from = ((ProxiedPlayer)sender).getServer().getInfo();
    ServerInfo target = ProxyServer.getInstance().getServerInfo(args[0]);
    
    if (target == null) {
      sendMessage(sender, true, "That server doesn't exist!");
    }
    else if (from.getName().equals(target.getName())) {
      sendMessage(sender, true, "You're already connected to that server!");
    }
    else {
      ProxiedPlayer s = (ProxiedPlayer)sender;
      
      String fromMessage = String.format(this.switchfrom, new Object[] { sender.getName(), from.getName() });
      for (ProxiedPlayer p : target.getPlayers())
        sendRawMessage(p, fromMessage);
      
      String toMessage = String.format(this.switchto, new Object[] { sender.getName(), target.getName() });
      for (ProxiedPlayer p : from.getPlayers())
        sendRawMessage(p, toMessage);
      
      sendMessage(sender, false, "&6Connecting you to &e" + target.getName());
      sendRawMessage(s, fromMessage);
      justSwitched.add(s);
      s.connect(target);
    }
  }
  
  @SuppressWarnings("deprecation")
  private void sendMessage(CommandSender sender, boolean error, String msg)
  {
    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&" + (error ? "c" : "2") + "Bungee&8]&7 " + msg));
  }
  
  @SuppressWarnings("deprecation")
  private void sendRawMessage(ProxiedPlayer p, String msg)
  {
    p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
  }
  
}
