package hu.archblox.login;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class ArchLoginBungee extends Plugin implements Listener {

    private static ArchLoginBungee instance;
    private DatabaseManager databaseManager;
    private final Set<UUID> authenticatedPlayers = new HashSet<>();
    private net.md_5.bungee.config.Configuration customConfig;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("4rchLogin BungeeCord elindult.");

        loadConfig();
        databaseManager = new DatabaseManager(this);

        getProxy().getPluginManager().registerListener(this, this);
        getProxy().registerChannel("archlogin:auth");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
    }

    public static ArchLoginBungee getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public Set<UUID> getAuthenticatedPlayers() {
        return authenticatedPlayers;
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        authenticatedPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (!authenticatedPlayers.contains(player.getUniqueId())) {
            String authServerName = customConfig.getString("auth_server", "auth");
            ServerInfo authServer = getProxy().getServerInfo(authServerName);
            
            if (authServer != null) {
                if (!event.getTarget().equals(authServer)) {
                    event.setTarget(authServer);
                }
            }
        }
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("archlogin:auth")) return;
        if (!(event.getReceiver() instanceof ProxiedPlayer player)) return;

        String message = new String(event.getData());
        if (message.startsWith("LOGIN:")) {
            UUID uuid = UUID.fromString(message.substring(6));
            authenticatedPlayers.add(uuid);

            String continueServer = customConfig.getString("continue_on_server", "none");
            if (continueServer != null && !continueServer.equalsIgnoreCase("none")) {
                getProxy().getScheduler().schedule(this, () -> {
                    if (player.isConnected()) {
                        ServerInfo targetServer = getProxy().getServerInfo(continueServer);
                        if (targetServer != null) {
                            player.connect(targetServer);
                        } else {
                            getLogger().warning("A continue_on_server szerver (" + continueServer + ") nem található a Bungee configban!");
                        }
                    }
                }, 50L, TimeUnit.MILLISECONDS);
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer player)) return;
        if (authenticatedPlayers.contains(player.getUniqueId())) return;

        String message = event.getMessage();
        if (message.startsWith("/")) {
            String cmd = message.substring(1).toLowerCase();
            if (!cmd.startsWith("login") && !cmd.startsWith("register") && !cmd.startsWith("reg")) {
                event.setCancelled(true);
                player.sendMessage(net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cBejelentkezés előtt nem használhatsz parancsokat!"));
            }
        } else {
            event.setCancelled(true);
            player.sendMessage(net.md_5.bungee.api.chat.TextComponent.fromLegacyText("§cBejelentkezés előtt nem beszélhetsz!"));
        }
    }

    private void loadConfig() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            customConfig = net.md_5.bungee.config.ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public net.md_5.bungee.config.Configuration getCustomConfig() {
        return customConfig;
    }
}