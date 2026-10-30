package hu.archblox.login;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    private final ArchLoginBungee plugin;
    private Connection connection;

    public DatabaseManager(ArchLoginBungee plugin) {
        this.plugin = plugin;
        connect();
    }

    private void connect() {
        try {
            var cfg = plugin.getCustomConfig();
            String host = cfg.getString("mysql.host", "127.0.0.1");
            int port = cfg.getInt("mysql.port", 3306);
            String db = cfg.getString("mysql.database", "minecraft");
            String user = cfg.getString("mysql.username", "root");
            String pass = cfg.getString("mysql.password", "");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
            connection = DriverManager.getConnection(url, user, pass);
            plugin.getLogger().info("MySQL adatbázis sikeresen csatolva a proxyn (Csak olvasás)!");
        } catch (SQLException e) {
            String err = e.getMessage().toLowerCase();
            if (err.contains("access denied")) {
                plugin.getLogger().severe("Hibás jelszó");
            } else if (err.contains("unknown database")) {
                plugin.getLogger().severe("Az adatbázis nem létezik/érvénytelen");
            } else {
                plugin.getLogger().severe("A működéshez be kell írni az adatbázis információit");
            }
            connection = null;
        } catch (Exception e) {
            plugin.getLogger().severe("A működéshez be kell írni az adatbázis információit");
            connection = null;
        }
    }

    public boolean isRegistered(UUID uuid) {
        if (connection == null) return false;
        try (PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM arch_users WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}