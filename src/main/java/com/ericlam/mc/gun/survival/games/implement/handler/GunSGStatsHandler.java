package com.ericlam.mc.gun.survival.games.implement.handler;

import com.ericlam.mc.gun.survival.games.implement.stats.GunSGGameStats;
import com.ericlam.mc.minigames.core.gamestats.GameStats;
import com.ericlam.mc.minigames.core.gamestats.GameStatsEditor;
import com.ericlam.mc.minigames.core.gamestats.GameStatsHandler;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.SQLDataSource;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.sql.*;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GunSGStatsHandler implements GameStatsHandler {

    private final SQLDataSource sqlDataSource;

    private static final String createTableStatement =
            """
                    CREATE TABLE IF NOT EXISTS `GunSG_stats` 
                    (`uuid` VARCHAR(40) PRIMARY KEY NOT NULL, 
                    `name`TINYTEXT NOT NULL , 
                    `kills` MEDIUMINT DEFAULT 0, 
                    `deaths` MEDIUMINT DEFAULT 0, 
                    `wins` MEDIUMINT DEFAULT  0, 
                    `played` MEDIUMINT DEFAULT 0, 
                     `scores` DOUBLE DEFAULT 0)
                    """;
    private static final String selectStatement = "SELECT * FROM `GunSG_stats` WHERE `uuid`=? OR `name`=?";
    private static final String saveStatement = """
            INSERT INTO `GunSG_stats` VALUES (?,?,?,?,?,?,?) 
            ON DUPLICATE KEY UPDATE `name`=?, `kills`=?, `deaths`=?, `wins`=?, `played`=?, `scores`=?
            """;
    private static final String createRecordStatement = """
                CREATE TABLE IF NOT EXISTS `GunSG_Log` 
                (`id` int primary key auto_increment, 
                `uuid` VARCHAR(40) NOT NULL, 
                `time` LONG NOT NULL, 
                `kills` MEDIUMINT DEFAULT 0, 
                `deaths` MEDIUMINT DEFAULT 0, 
                `wins` MEDIUMINT DEFAULT  0, 
                `scores` DOUBLE DEFAULT 0)
            """;
    private static final String saveRecordStatement = """
            INSERT INTO `GunSG_Log` VALUES (NULL,?,?,?,?,?,?)
            """;




    public GunSGStatsHandler() {
        this.sqlDataSource = DragoniteMC.getAPI().getSQLDataSource();
        CompletableFuture.runAsync(() -> {
            try (Connection connection = sqlDataSource.getConnection();
                 PreparedStatement createTable = connection.prepareStatement(createTableStatement);
                 PreparedStatement createLogTable = connection.prepareStatement(createRecordStatement)) {
                createTable.execute();
                createLogTable.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Nonnull
    @Override
    public GameStatsEditor loadGameStatsData(@Nonnull Player player) {
        try (Connection connection = sqlDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectStatement)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                int kills = set.getInt("kills");
                int deaths = set.getInt("deaths");
                int wins = set.getInt("wins");
                int played = set.getInt("played");
                double score = set.getDouble("scores");
                return new GunSGGameStats(kills, deaths, played, wins, score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new GunSGGameStats();
    }

    @Override
    public CompletableFuture<Void> saveGameStatsData(OfflinePlayer offlinePlayer, GameStats gameStats) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = sqlDataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(saveStatement)) {
                saveStatsStatement(offlinePlayer, gameStats, statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> saveGameStatsData(Map<OfflinePlayer, GameStats> map) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = sqlDataSource.getConnection()) {
                map.forEach((offlinePlayer, gameStats) -> {
                    try (PreparedStatement statement = connection.prepareStatement(saveStatement)) {
                        saveStatsStatement(offlinePlayer, gameStats, statement);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> saveGameStatsRecord(OfflinePlayer offlinePlayer, GameStats gameStats, Timestamp timestamp) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(saveRecordStatement)) {
                var gsgStats = gameStats.castTo(GunSGGameStats.class);
                saveRecordStatement(statement, offlinePlayer, gsgStats, timestamp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> saveGameStatsRecord(Map<OfflinePlayer, GameStats> map, Map<OfflinePlayer, Timestamp> map1) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(saveRecordStatement)) {
                for (Map.Entry<OfflinePlayer, GameStats> entry : map.entrySet()) {
                    var player = entry.getKey();
                    var gsgStats = entry.getValue().castTo(GunSGGameStats.class);
                    var ts = map1.getOrDefault(player, Timestamp.from(Instant.now()));
                    saveRecordStatement(statement, player, gsgStats, ts);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveRecordStatement(PreparedStatement statement, OfflinePlayer player, GunSGGameStats gugStats, Timestamp ts) throws SQLException {
        statement.setString(1, player.getUniqueId().toString());
        statement.setLong(2, ts.getTime());
        statement.setInt(3, gugStats.getKills());
        statement.setInt(4, gugStats.getDeaths());
        statement.setInt(5, gugStats.getWins());
        statement.setDouble(6, gugStats.getScores());
        statement.executeUpdate();
    }

    private void saveStatsStatement(OfflinePlayer offlinePlayer, GameStats gameStats, PreparedStatement statement) throws SQLException {
        statement.setString(1, offlinePlayer.getUniqueId().toString());
        statement.setString(2, offlinePlayer.getName());
        statement.setInt(3, gameStats.getKills());
        statement.setInt(4, gameStats.getDeaths());
        statement.setInt(5, gameStats.getWins());
        statement.setInt(6, gameStats.getPlayed());
        statement.setDouble(7, gameStats.getScores());
        statement.setString(8, offlinePlayer.getName());
        statement.setInt(9, gameStats.getKills());
        statement.setInt(10, gameStats.getDeaths());
        statement.setInt(11, gameStats.getWins());
        statement.setInt(12, gameStats.getPlayed());
        statement.setDouble(13, gameStats.getScores());
        statement.execute();
    }
}
