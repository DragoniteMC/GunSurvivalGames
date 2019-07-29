package com.ericlam.mc.gun.survival.games.implement.handler;

import com.ericlam.mc.gun.survival.games.implement.stats.GunSGGameStats;
import com.ericlam.mc.minigames.core.gamestats.GameStats;
import com.ericlam.mc.minigames.core.gamestats.GameStatsEditor;
import com.ericlam.mc.minigames.core.gamestats.GameStatsHandler;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.SQLDataSource;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GunSGStatsHandler implements GameStatsHandler {

    private final SQLDataSource sqlDataSource;

    public GunSGStatsHandler(){
        this.sqlDataSource = HyperNiteMC.getAPI().getSQLDataSource();
        CompletableFuture.runAsync(()->{
            try(Connection connection = sqlDataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `GunSG_stats` (`uuid` VARCHAR(40) NOT NULL  PRIMARY KEY, `name` TINYTEXT NOT NULL , `kills` MEDIUMINT DEFAULT 0, `deaths` MEDIUMINT DEFAULT 0, `wins` MEDIUMINT DEFAULT 0, `played` MEDIUMINT DEFAULT 0, `scores` DOUBLE DEFAULT 0)")) {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Nonnull
    @Override
    public GameStatsEditor loadGameStatsData(@Nonnull Player player) {
        try(Connection connection = sqlDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `kills`, `wins`, `deaths`, `played` FROM `GunSG_stats` WHERE `uuid`=? OR `name`=?")){
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            ResultSet set = statement.executeQuery();
            if (set.next()){
                int kills = set.getInt("kills");
                int deaths = set.getInt("deaths");
                int wins = set.getInt("wins");
                int played = set.getInt("played");
                return new GunSGGameStats(kills, deaths, played, wins);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new GunSGGameStats();
    }

    @Override
    public CompletableFuture<Void> saveGameStatsData(OfflinePlayer offlinePlayer, GameStats gameStats) {
        return CompletableFuture.runAsync(()->{
            try(Connection connection = sqlDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO `GunSG_stats` VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `name`=?, `kills`=?, `deaths`=?, `wins`=?, `played`=?, `scores`=? ")){
                setStatement(offlinePlayer, gameStats, statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> saveGameStatsData(Map<OfflinePlayer, GameStats> map) {
        return CompletableFuture.runAsync(()->{
            try(Connection connection = sqlDataSource.getConnection()){
                map.forEach((offlinePlayer, gameStats)->{
                    try(PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO `GunSG_stats` VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE `name`=?, `kills`=?, `deaths`=?, `wins`=?, `played`=?, `scores`=? ")) {
                        setStatement(offlinePlayer, gameStats, statement);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setStatement(OfflinePlayer offlinePlayer, GameStats gameStats, PreparedStatement statement) throws SQLException {
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
