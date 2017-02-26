package com.asiainfo.werewolfweb.repository;


import com.asiainfo.werewolfweb.bean.GameTale;
import com.asiainfo.werewolfweb.bean.Player;
import com.asiainfo.werewolfweb.bean.PlayerStats;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/1/3
 */
@Repository
public class WereWolfRepository {
    private final MongoOperations operations;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    public WereWolfRepository(MongoOperations operations) {
        this.operations = operations;
    }

    private static MongoClient mongoClient;
    private static MongoDatabase database;


    public static MongoDatabase getMongoDatabase() {
        if (mongoClient == null) {
            MongoClientURI connectionString = new MongoClientURI("mongodb://127.0.0.1:27017");
            mongoClient = new MongoClient(connectionString);
        }
        database = mongoClient.getDatabase("werewolf");
        return database;
    }

    /**
     * 玩家的狼人胜利次数统计
     */
    public Integer groupCountByWolfWinForPlayer(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        final int[] total = {0};
        String mapFunc = "function () {\n" +
                "        for (var p of this.player) {\n" +
                "            if (p.role == 'wolf' && p.name == '" + player + "') {\n" +
                "                emit(p.name, 1);\n" +
                "            }\n" +
                "        }\n" +
                "    }";
        String reduceFunc = "function (key, value) {\n" +
                "        return Array.sum(value);\n" +
                "    }";
        Block<Document> documentBlock = document -> {
            if (document != null) {
                total[0] = document.getDouble("value").intValue();
            }
        };
        collection.mapReduce(mapFunc, reduceFunc).filter
                (Document.parse("{'settlement.ending': 'bloody'}")).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家的狼人游戏次数统计
     */
    public Integer groupCountByWolfPlayForPlayer(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        String mapFunc = "function () {\n" +
                "        emit('" + player + "', 1);\n" +
                "    }";
        String reduceFunc = "function (key, value) {\n" +
                "        return Array.sum(value);\n" +
                "    }";

        Block<Document> documentBlock = document -> {
            if (document != null) {
                total[0] = document.getDouble("value").intValue();
            }
        };
        collection.mapReduce(mapFunc, reduceFunc).filter
                (Document.parse("{'player': {$elemMatch: {'name': '" + player + "', 'role': 'wolf', " +
                        "'lovers': 'no'}}}")).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家参与游戏次数统计
     */
    public Integer groupCountByPlayForPlayer(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        String mapFunc = "function () {\n" +
                "        emit('" + player + "', 1);\n" +
                "    }";
        String reduceFunc = "function (key, value) {\n" +
                "        return Array.sum(value);\n" +
                "    }";

        Block<Document> documentBlock = document -> {
            if (document != null) {
                total[0] = document.getDouble("value").intValue();
            }
        };
        collection.mapReduce(mapFunc, reduceFunc).filter
                (Document.parse("{'player': {$elemMatch: {'name': '" + player + "'}}}")).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家的游戏胜利次数统计
     */
    public Integer groupCountWinForPlayer(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        final int[] total = {0};
        String mapFunc = "function () {\n" +
                "        for (var winner of this.settlement.winner) {\n" +
                "            if (winner == '" + player + "') {\n" +
                "                emit(winner.name, 1);\n" +
                "            }\n" +
                "        }\n" +
                "    }";
        String reduceFunc = "function (key, value) {\n" +
                "        return Array.sum(value);\n" +
                "    }";
        Block<Document> documentBlock = document -> {
            if (document != null) {
                total[0] = document.getDouble("value").intValue();
            }
        };
        collection.mapReduce(mapFunc, reduceFunc).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家的每种角色游戏次数统计
     */
    public List<Map> groupCountByRoleForPlayer(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");

        List<Map> list = new ArrayList();
        Block<Document> documentBlock = document -> {
            Map map = new HashMap<>();
            map.put("roleName", document.getString("_id"));
            map.put("roleCount", document.getInteger("total"));
            list.add(map);
        };

        collection.aggregate(
                Arrays.asList(
                        Aggregates.project(Projections.fields(Projections.include("player"))),
                        Aggregates.unwind("$player"),
                        Aggregates.match(Filters.eq("player.name", player)),
                        Aggregates.group("$player.role", Accumulators.sum("total", 1))
                )
        ).forEach(documentBlock);
        return list;
    }

    /**
     * 玩家的每种角色游戏胜利次数统计
     */
    public List<Map> groupWinCountByRoleForPlayer(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");

        List<Map> list = new ArrayList();
        Block<Document> documentBlock = document -> {
            Map map = new HashMap<>();
            map.put("roleName", document.getString("_id"));
            map.put("roleCount", document.getInteger("total"));
            list.add(map);
        };

        collection.aggregate(
                Arrays.asList(
                        Aggregates.project(Projections.fields(Projections.include("player"),
                                Projections.include("settlement"))),
                        Aggregates.unwind("$player"),
                        Aggregates.match(Filters.eq("player.name", player)),
                        Aggregates.unwind("$settlement.winner"),
                        Aggregates.match(Filters.eq("settlement.winner", player)),
                        Aggregates.group("$player.role", Accumulators.sum("total", 1))
                )
        ).forEach(documentBlock);
        return list;
    }

    /**
     * 玩家作为女巫使用解药次数统计
     */
    public Integer countByRevive(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "            $project: {\n" +
                        "                'player': {\n" +
                        "                    $filter: {\n" +
                        "                        input: '$player',\n" +
                        "                        as: 'p',\n" +
                        "                        cond: {\n" +
                        "                            $and: [\n" +
                        "                                {$eq: ['$$p.name', '" + player + "']}," +
                        "\n" +
                        "                                {$eq: ['$$p.role', 'witch']}\n" +
                        "                            ]\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "\n" +
                        "                },\n" +
                        "                'round': 1\n" +
                        "            }\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $project: {\n" +
                        "                'player': 1,\n" +
                        "                'round': 1,\n" +
                        "                'size': {$size: '$player'}\n" +
                        "            }\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $match: {'size': {$gt: 0}}\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $unwind: '$round'\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $unwind: '$round'\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $unwind: '$round.step'\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $match: {$and: [{'round.stage': 'night'}, {'round" +
                        ".step.action': 'revive'}]}\n" +
                        "        }")
                )
        ).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家作为女巫有效的使用解药次数统计
     */
    public Integer countByReviveIsValid(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };
        collection.aggregate(
                Arrays.asList(
                        Document.parse("{\n" +
                                "        $project: {\n" +
                                "            'player': 1,\n" +
                                "            'round': 1,\n" +
                                "            'filter_player': {\n" +
                                "                $filter: {\n" +
                                "                    input: '$player',\n" +
                                "                    as: 'p',\n" +
                                "                    cond: {\n" +
                                "                        $and: [\n" +
                                "                            {$eq: ['$$p.name', '" + player +
                                "']},\n" +
                                "                            {$eq: ['$$p.role', 'witch']},\n" +
                                "                            {$eq: ['$$p.lovers', 'no']}\n" +
                                "                        ]\n" +
                                "                    }\n" +
                                "                }\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $project: {\n" +
                                "            'player': 1,\n" +
                                "            'round': 1,\n" +
                                "            'size': {$size: '$filter_player'}\n" +
                                "        }\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $match: {'size': {$gt: 0}}\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $unwind: '$round'\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $unwind: '$round'\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $unwind: '$round.step'\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $match: {$and: [{'round.stage': 'night'}, {'round.step" +
                                ".action': 'revive'}]}\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $unwind: '$player'\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $unwind: '$round.step.passive'\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $redact: {\n" +
                                "            $cond: {\n" +
                                "                if: {$eq: ['$player.name', '$round.step" +
                                ".passive']},\n" +
                                "                then: '$$DESCEND',\n" +
                                "                else: '$$PRUNE'\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }"),
                        Document.parse("{\n" +
                                "        $match: {\n" +
                                "            $and: [\n" +
                                "                {'player.role': {$not: {$eq: 'wolf'}}},\n" +
                                "                {'player.role': {$not: {$eq: 'whiteWolf'}}}\n" +
                                "            ]\n" +
                                "        }\n" +
                                "    }")
                )
        ).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家作为女巫使用毒药次数统计
     */
    public Integer countByPoison(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };

        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "            $project: {\n" +
                        "                'player': {\n" +
                        "                    $filter: {\n" +
                        "                        input: '$player',\n" +
                        "                        as: 'p',\n" +
                        "                        cond: {\n" +
                        "                            $and: [\n" +
                        "                                {$eq: ['$$p.name', '" + player + "']}," +
                        "\n" +
                        "                                {$eq: ['$$p.role', 'witch']}\n" +
                        "                            ]\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "\n" +
                        "                },\n" +
                        "                'round': 1\n" +
                        "            }\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $project: {\n" +
                        "                'player': 1,\n" +
                        "                'round': 1,\n" +
                        "                'size': {$size: '$player'}\n" +
                        "            }\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $match: {'size': {$gt: 0}}\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $unwind: '$round'\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $unwind: '$round'\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $unwind: '$round.step'\n" +
                        "        }"),
                Document.parse("{\n" +
                        "            $match: {$and: [{'round.stage': 'night'}, {'round.step" +
                        ".action': 'poison'}]}\n" +
                        "        }")
        )).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家作为女巫有效的使用毒药次数统计
     */
    public Integer countByPoisonIsValid(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': 1,\n" +
                        "            'round': 1,\n" +
                        "            'filter_player': {\n" +
                        "                $filter: {\n" +
                        "                    input: '$player',\n" +
                        "                    as: 'p',\n" +
                        "                    cond: {\n" +
                        "                        $and: [\n" +
                        "                            {$eq: ['$$p.name', '" + player + "']},\n" +
                        "                            {$eq: ['$$p.role', 'witch']},\n" +
                        "                            {$eq: ['$$p.lovers', 'no']}\n" +
                        "                        ]\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': 1,\n" +
                        "            'round': 1,\n" +
                        "            'size': {$size: '$filter_player'}\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'size': {$gt: 0}}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round.step'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {$and: [{'round.stage': 'night'}, {'round.step.action':" +
                        " 'poison'}]}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$player'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round.step.passive'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $redact: {\n" +
                        "            $cond: {\n" +
                        "                if: {$eq: ['$player.name', '$round.step.passive']},\n" +
                        "                then: '$$DESCEND',\n" +
                        "                else: '$$PRUNE'\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {\n" +
                        "            $or: [\n" +
                        "                {'player.role': {$eq: 'wolf'}},\n" +
                        "                {'player.role': {$eq: 'whiteWolf'}}\n" +
                        "            ]\n" +
                        "\n" +
                        "        }\n" +
                        "    }")
        )).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家作为预言家活到游戏结束并且好人胜利的次数统计
     */
    public Integer countBySeerIsWinner(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': 1,\n" +
                        "            'settlement': 1,\n" +
                        "            'filter_player': {\n" +
                        "                $filter: {\n" +
                        "                    input: '$player',\n" +
                        "                    as: 'p',\n" +
                        "                    cond: {\n" +
                        "                        $and: [\n" +
                        "                            {$eq: ['$$p.name', '" + player + "']},\n" +
                        "                            {$eq: ['$$p.role', 'seer']},\n" +
                        "                            {$eq: ['$$p.lovers', 'no']}\n" +
                        "                        ]\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': 1,\n" +
                        "            'settlement': 1,\n" +
                        "            'size': {$size: '$filter_player'}\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'size': {$gt: 0}}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'settlement.ending': 'safety'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$settlement.survival'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'settlement.survival': '" + player + "'}\n" +
                        "    }")
        )).forEach(documentBlock);
        return total[0];
    }

    public void createPlayerStats() {
        List<Player> players = playerRepository.getPlayers();
        this.operations.dropCollection("PlayerStats");
        for (Player player : players) {
            PlayerStats playerStats = new PlayerStats();
            playerStats.setPlayerId(player.getPlayerId());
            playerStats.setPlayCount(this.groupCountByPlayForPlayer(player.getPlayerId()));
            playerStats.setPlayWinCount(this.groupCountWinForPlayer(player.getPlayerId()));
            playerStats.setSeerWinCount(this.countBySeerIsWinner(player.getPlayerId()));
            playerStats.setWitchPoisonCount(this.countByPoison(player.getPlayerId()));
            playerStats.setWitchVaildPoisonCount(this.countByPoisonIsValid(player.getPlayerId()));
            playerStats.setWitchReviveCount(this.countByRevive(player.getPlayerId()));
            playerStats.setWitchVaildReviveCount(this.countByReviveIsValid(player.getPlayerId()));
            playerStats.setWolfPlayCount(this.groupCountByWolfPlayForPlayer(player.getPlayerId()));
            playerStats.setWolfWinCount(this.groupCountByWolfWinForPlayer(player.getPlayerId()));
            playerStats.setRolePlayGroupCount(this.groupCountByRoleForPlayer(player.getPlayerId()));
            playerStats.setRoleWinPlayGroupCount(this.groupWinCountByRoleForPlayer(player.getPlayerId()));
            playerStats.setVillagerKilledCount(this.countByVillagerKilled(player.getPlayerId()));
            playerStats.setSeerFisrNightKilledCount(this.countBySeerFisrNightKilled(player.getPlayerId()));
            playerStats.setHuntedCount(this.countByHunted(player.getPlayerId()));
            playerStats.setHurtedGroupByPlayerAndPatternCount(this.countByHurtedGroupByPlayerAndPattern(player.getPlayerId()));
            playerStats.setTopHurtPlayerCount(this.countByTopHurtPlayer(player.getPlayerId()));
            this.operations.insert(playerStats);
        }
    }

    /**
     * 总游戏局数
     */
    public Integer getGameCount() {
        return Long.valueOf(operations.getCollection("Game").count()).intValue();
    }

    public GameTale getGameTale() {
        return operations.findOne(new Query(), GameTale.class);
    }

    /**
     * 玩家作为平民被杀的次数统计
     */
    public Integer countByVillagerKilled(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': {\n" +
                        "                $filter: {\n" +
                        "                    input: '$player',\n" +
                        "                    as: 'p',\n" +
                        "                    cond: {\n" +
                        "                        $and: [\n" +
                        "                            {$eq: ['$$p.name', '" + player + "']},\n" +
                        "                            {$eq: ['$$p.role', 'villager']}\n" +
                        "                        ]\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            },\n" +
                        "            'settlement': 1\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$player'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$settlement.survival'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'settlement.winner': {$not: {$eq: '" + player + "'}}}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': 1\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $group: {\n" +
                        "            _id: '$_id', total: {\n" +
                        "                $sum: 1\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }")
        )).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家作为预言家第一晚被杀的次数统计
     */
    public Integer countBySeerFisrNightKilled(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': {\n" +
                        "                $filter: {\n" +
                        "                    input: '$player',\n" +
                        "                    as: 'p',\n" +
                        "                    cond: {\n" +
                        "                        $and: [\n" +
                        "                            {$eq: ['$$p.name', '" + player + "']},\n" +
                        "                            {$eq: ['$$p.role', 'seer']}\n" +
                        "                        ]\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            },\n" +
                        "            'round': 1\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$player'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: {path: \"$round\", includeArrayIndex: \"arrayIndex\"}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'arrayIndex': 0}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'round.stage': 'night'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round.step'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'round.step.action': 'murder'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round.step.passive'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'round.step.passive': '" + player + "'}\n" +
                        "    }")
        )).forEach(documentBlock);
        return total[0];
    }

    /**
     * 玩家被猎人带走的次数统计
     */
    public Integer countByHunted(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        int[] total = {0};
        Block<Document> documentBlock = document -> {
            total[0]++;
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'player': {\n" +
                        "                $filter: {\n" +
                        "                    input: '$player',\n" +
                        "                    as: 'p',\n" +
                        "                    cond: {\n" +
                        "                        $and: [\n" +
                        "                            {$eq: ['$$p.name', '" + player + "']}\n" +
                        "                        ]\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            },\n" +
                        "            'round': 1\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$player'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'round.stage': 'day'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round.step'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'round.step.action': 'hunt'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round.step.passive'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'round.step.passive': '" + player + "'}\n" +
                        "    }")
        )).forEach(documentBlock);
        return total[0];
    }

    /**
     * 作为情侣最多次数的两位玩家
     */
    public Object[] getLoverPlayerTop() {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        Object[] result = new Object[2];
        Block<Document> documentBlock = document -> {
            result[0] = document.get("_id", ArrayList.class);
            result[1] = document.getInteger("total");
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $project: {'player': 1}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$player'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'player.lovers': 'yes'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $sort: {'player.name': -1}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $group: {\n" +
                        "            _id: '$_id', 'player': {$push: \"$player.name\"}\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $group: {\n" +
                        "            _id: '$player', total: {\n" +
                        "                $sum: 1\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $sort: {'total': -1}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $limit: 1\n" +
                        "    }")
        )).forEach(documentBlock);
        return result;
    }

    /**
     * 玩家被其他人以某方式伤害的次数统计
     */
    public List<Map> countByHurtedGroupByPlayerAndPattern(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        List<Map> arrayList = new ArrayList();
        Block<Document> documentBlock = document -> {
            Map _id = ((Map) document.get("_id"));
            _id.put("total", document.getInteger("total"));
            arrayList.add(_id);
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $match: {'player.name': '" + player + "'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'round': 1\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'step': '$round.step'\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$step'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {\n" +
                        "            $or: [\n" +
                        "                {'step.action': 'murder'},\n" +
                        "                {'step.action': 'poison'},\n" +
                        "                {'step.action': 'hunt'},\n" +
                        "                {'step.action': 'implicate'}\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$step.passive'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'step.passive': '" + player + "'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$step.initiative'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'action': '$step.action',\n" +
                        "            'initiative': '$step.initiative',\n" +
                        "            'passive': '$step.passive'\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $group: {\n" +
                        "            _id: {passive:'$passive', initiative: '$initiative', action: '$action'},total: {\n" +
                        "                $sum: 1\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $sort: {'total': -1}\n" +
                        "    }")
        )).forEach(documentBlock);
        return arrayList;
    }

    /**
     * 玩家被某人伤害的次数最多
     */
    public Map countByTopHurtPlayer(String player) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection("Game");
        Map result = new HashMap();
        Block<Document> documentBlock = document -> {
            Map<String, String> map = (Map<String, String>) document.get("_id");
            String initiativePlayer = map.get("initiative");
            Integer total = document.getInteger("total");
            result.put("player", initiativePlayer);
            result.put("total", total);
        };
        collection.aggregate(Arrays.asList(
                Document.parse("{\n" +
                        "        $match: {'player.name': '" + player + "'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'round': 1\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$round'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'step': '$round.step'\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$step'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {\n" +
                        "            $or: [\n" +
                        "                {'step.action': 'murder'},\n" +
                        "                {'step.action': 'poison'},\n" +
                        "                {'step.action': 'hunt'},\n" +
                        "                {'step.action': 'implicate'}\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$step.passive'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $match: {'step.passive': '" + player + "'}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $unwind: '$step.initiative'\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $project: {\n" +
                        "            'action': '$step.action',\n" +
                        "            'initiative': '$step.initiative',\n" +
                        "            'passive': '$step.passive'\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $group: {\n" +
                        "            _id: {passive:'$passive', initiative: '$initiative'},total: {\n" +
                        "                $sum: 1\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $sort: {'total': -1}\n" +
                        "    }"),
                Document.parse("{\n" +
                        "        $limit: 1\n" +
                        "    }")
        )).forEach(documentBlock);
        return result;
    }
}
