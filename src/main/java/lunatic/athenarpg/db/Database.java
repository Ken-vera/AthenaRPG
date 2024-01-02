package lunatic.athenarpg.db;

import lunatic.athenarpg.Main;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class Database {
    private final Main plugin;

    private Connection connection;

    public Database(Main plugin) {
        this.plugin = plugin;
    }

    public Connection getConnection() throws SQLException {

        if (connection != null) {
            return connection;
        }
        String database = plugin.getConfig().getString("database.database");
        String host = plugin.getConfig().getString("database.host");
        String port = plugin.getConfig().getString("database.port");

        String username = plugin.getConfig().getString("database.user");
        String password = plugin.getConfig().getString("database.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        this.connection = DriverManager.getConnection(url, username, password);

        return this.connection;
    }

    public void initializeDatabase() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        // Create the table if it does not exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_reputation(player_uuid varchar(36) primary key, player_name varchar(36), bryzleReputation int, articReputation int)";
        statement.executeUpdate(createTableSQL);
        System.out.println("Table not found, Creating new table.");

        statement.close();
    }
    public void createPlayerReputation(UUID playerUUID, String playerName, int bryzleReputation, int articReputation) throws SQLException {
        Connection connection = getConnection();
        String insertSQL = "INSERT INTO player_reputation (player_uuid, player_name, bryzleReputation, articReputation) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, playerUUID.toString());
            preparedStatement.setString(2, playerName);
            preparedStatement.setInt(3, bryzleReputation);
            preparedStatement.setInt(4, articReputation);

            preparedStatement.executeUpdate();
        }
    }

    public void updatePlayerReputation(UUID playerUUID, int bryzleReputation, int articReputation) throws SQLException {
        Connection connection = getConnection();
        String updateSQL = "UPDATE player_reputation SET bryzleReputation=?, articReputation=? WHERE player_uuid=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, bryzleReputation);
            preparedStatement.setInt(2, articReputation);
            preparedStatement.setString(3, playerUUID.toString());

            preparedStatement.executeUpdate();
        }
    }
    public void deletePlayerReputation(UUID playerUUID) throws SQLException {
        Connection connection = getConnection();
        String deleteSQL = "DELETE FROM player_reputation WHERE player_uuid=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, playerUUID.toString());

            preparedStatement.executeUpdate();
        }
    }
    public void addReputation(Player player, int bryzleAmount, int articAmount) {
        UUID playerUUID = player.getUniqueId();

        try {
            int currentBryzleReputation = getPlayerReputation(playerUUID, "bryzleReputation");
            int newBryzleReputation = currentBryzleReputation + bryzleAmount;

            int currentArticReputation = getPlayerReputation(playerUUID, "articReputation");
            int newArticReputation = currentArticReputation + articAmount;

            updatePlayerReputation(playerUUID, newBryzleReputation, newArticReputation);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., logging, sending a message to the player)
        }
    }

    public int getPlayerReputation(UUID playerUUID, String reputationType) throws SQLException {
        Connection connection = getConnection();
        String selectSQL = "SELECT " + reputationType + " FROM player_reputation WHERE player_uuid=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, playerUUID.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(reputationType);
                }
            }
        }
        return 0; // Default value if the player is not found
    }

}
