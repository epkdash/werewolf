package com.asiainfo.werewolfweb.repository;

import com.asiainfo.werewolfweb.bean.Player;
import com.asiainfo.werewolfweb.bean.PlayerStats;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
@Repository
public class PlayerRepository {
    private final MongoOperations operations;

    @Autowired
    public PlayerRepository(MongoOperations operations) {
        this.operations = operations;
    }


    public static MongoDatabase getMongoDatabase() {
        MongoClientURI connectionString = new MongoClientURI("mongodb://127.0.0.1:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("werewolf");
        return database;
    }

    /**
     * 获得全部玩家资料
     */
    public List<Player> getPlayers() {
        List<Player> players = operations.findAll(Player.class);
        return players;
    }

    /**
     * 获得指定玩家资料
     */
    public Player getPlayer(String playerId) {
        Query query = query(where("playerId").is(playerId));
        Player player = operations.findOne(query, Player.class);
        return player;
    }

    /**
     * 获得指定玩家资料
     */
    public Player getPlayerByPassport(String passport) {
        if (passport == null || passport.equals("")) {
            return null;
        } else {
            Query query = query(where("passport").is(passport));
            Player player = operations.findOne(query, Player.class);
            return player;
        }
    }

    /**
     * 获得玩家统计
     */
    public List<PlayerStats> getPlayerStatses() {
        List<PlayerStats> playerStatses = operations.findAll(PlayerStats.class);
        return playerStatses;
    }


    /**
     * 获得单个玩家统计
     */
    public PlayerStats getPlayerStats(String playerId) {
        Query query = query(where("playerId").is(playerId));
        PlayerStats playerStats = operations.findOne(query, PlayerStats.class);
        return playerStats;
    }

}
