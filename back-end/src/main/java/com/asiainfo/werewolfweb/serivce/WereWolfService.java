package com.asiainfo.werewolfweb.serivce;

import com.asiainfo.werewolfweb.bean.*;
import com.asiainfo.werewolfweb.repository.BoardRepository;
import com.asiainfo.werewolfweb.repository.PlayerRepository;
import com.asiainfo.werewolfweb.repository.WereWolfRepository;
import com.asiainfo.werewolfweb.utils.IConstant;
import com.asiainfo.werewolfweb.utils.Params;
import com.asiainfo.werewolfweb.utils.Utils;
import com.mongodb.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
@Service
@Slf4j
public class WereWolfService {

    @Autowired
    public WereWolfRepository wereWolfRepository;

    @Autowired
    public PlayerRepository playerRepository;

    @Autowired
    private BoardRepository boardRepository;

    public WolfTopOne computeWolfTopOne() {
        WolfTopOne wolfTopOne = new WolfTopOne();
        Integer gameCount = wereWolfRepository.getGameCount();
        List<PlayerStats> playerStatses = playerRepository.getPlayerStatses();
        double maxSocre = 0;
        for (PlayerStats playerStats : playerStatses) {
            if (gameCount == 0 || playerStats.getWolfPlayCount() == 0) {
                continue;
            }
            log.info("playerStats.getPlayerId():" + playerStats.getPlayerId());
            log.info("playerStats.getPlayCount()/gameCount:" + playerStats.getPlayCount() + "/" + gameCount);
            Double joinRate = Double.valueOf(playerStats.getPlayCount()) / Double.valueOf(gameCount);
            log.info("joinRate:" + joinRate);
            log.info("playerStats.getWolfWinCount()/playerStats.getWolfPlayCount():" + playerStats.getWolfWinCount() + "/" + playerStats.getWolfPlayCount());
            Double wolfWinRate = Double.valueOf(playerStats.getWolfWinCount()) / Double.valueOf(playerStats.getWolfPlayCount());
            log.info("wolfWinRate:" + wolfWinRate);
            Double socre = wolfWinRate * joinRate;
            log.info("socre:" + socre);
            if (socre > maxSocre) {
                maxSocre = socre;
                wolfTopOne.setMaxSocre(maxSocre);
                wolfTopOne.setPlayerId(playerStats.getPlayerId());
                Player player = playerRepository.getPlayer(playerStats.getPlayerId());
                wolfTopOne.setPlayerName(player.getName());
            }
        }
        return wolfTopOne;
    }

    public WitchTopOne computeWitchTopOne() {
        WitchTopOne witchTopOne = new WitchTopOne();
        Integer gameCount = wereWolfRepository.getGameCount();
        List<PlayerStats> playerStatses = playerRepository.getPlayerStatses();
        double maxSocre = 0;
        for (PlayerStats playerStats : playerStatses) {
            int useVaildWitchSkillCount = playerStats.getWitchVaildPoisonCount() + playerStats.getWitchVaildReviveCount();
            int useWitchSkillCount = playerStats.getWitchPoisonCount() + playerStats.getWitchReviveCount();
            if (gameCount == 0 || useWitchSkillCount == 0) {
                continue;
            }
            Double useValidRate = Double.valueOf(useVaildWitchSkillCount) / Double.valueOf(useWitchSkillCount);
            Double joinRate = Double.valueOf(playerStats.getPlayCount()) / Double.valueOf(gameCount);
            Double socre = useValidRate * joinRate;
            log.info("playerStats.getPlayerId():" + playerStats.getPlayerId());
            log.info("socre:" + socre);
            if (socre > maxSocre) {
                maxSocre = socre;
                witchTopOne.setMaxSocre(maxSocre);
                witchTopOne.setPlayerId(playerStats.getPlayerId());
                Player player = playerRepository.getPlayer(playerStats.getPlayerId());
                witchTopOne.setPlayerName(player.getName());
            }

        }
        return witchTopOne;
    }

    public SeerTopOne computeSeerTopOne() {
        SeerTopOne seerTopOne = new SeerTopOne();
        Integer gameCount = wereWolfRepository.getGameCount();
        List<PlayerStats> playerStatses = playerRepository.getPlayerStatses();
        double maxSocre = 0;
        for (PlayerStats playerStats : playerStatses) {
            int seerPlayCount = 0;
            List<Map> roleCounts = playerStats.getRolePlayGroupCount();
            for (Map roleCount : roleCounts) {
                if (roleCount.get("roleName").equals(IConstant.Role.SEER)) {
                    seerPlayCount = (int) roleCount.get("roleCount");
                }
            }
            if (gameCount == 0 || seerPlayCount == 0) {
                continue;
            }
            Double seerWinRate = Double.valueOf(playerStats.getSeerWinCount()) / Double.valueOf(seerPlayCount);
            Double joinRate = Double.valueOf(playerStats.getPlayCount()) / Double.valueOf(gameCount);
            Double socre = seerWinRate * joinRate;
            log.info("playerStats.getPlayerId():" + playerStats.getPlayerId());
            log.info("socre:" + socre);
            if (socre > maxSocre) {
                maxSocre = socre;
                seerTopOne.setMaxSocre(maxSocre);
                seerTopOne.setPlayerId(playerStats.getPlayerId());
                Player player = playerRepository.getPlayer(playerStats.getPlayerId());
                seerTopOne.setPlayerName(player.getName());
            }
        }
        return seerTopOne;
    }

    public void addTopOneBoard(String playerId, String message, String type) {
        TopOneBoard topOneBoard = new TopOneBoard();
        topOneBoard.setBoardId(System.currentTimeMillis() + "");
        topOneBoard.setPlayerId(playerId);
        topOneBoard.setMessage(message);
        topOneBoard.setType(type);
        topOneBoard.setName(Params.transPlayerId(playerId));
        topOneBoard.setIsValid(IConstant.ValidTag.VALID);
        topOneBoard.setInDate(new Date());
        boardRepository.insertTopOneBoard(topOneBoard);
    }

    public void removeTopOneBoard(String playerId, String type) {
        TopOneBoard topOneBoardOld = this.queryTopOneBoard(playerId, type);
        if (topOneBoardOld != null) {
            this.removeTopOneBoard(topOneBoardOld.getBoardId());
        }
    }

    public void removeTopOneBoard(String boardId) {
        boardRepository.inValidTopOneBoard(boardId);
    }

    public TopOneBoard queryTopOneBoard(String playerId, String type) {
        return boardRepository.queryTopOneBoard(playerId, type);
    }

    public List<TopOneBoard> queryTopOneBoards() {
        return boardRepository.queryTopOneBoards();
    }

    public void addMessageBoard(String playerId, String message) {
        MessageBoard messageBoard = new MessageBoard();
        messageBoard.setBoardId(System.currentTimeMillis() + "");
        messageBoard.setPlayerId(playerId);
        messageBoard.setMessage(message);
        messageBoard.setName(Params.transPlayerId(playerId));
        messageBoard.setIsValid(IConstant.ValidTag.VALID);
        messageBoard.setInDate(new Date());
        boardRepository.insertMessageBoard(messageBoard);
    }

    public List<MessageBoard> queryMessageBoards(Integer limit) {
        return boardRepository.queryMessageBoards(limit);
    }

    public void inValidMessageBoard(String boardId) {
        boardRepository.inValidMessageBoard(boardId);
    }

    public Player login(String passport) {
        Player player = playerRepository.getPlayerByPassport(passport);
        if (player == null) {
            Player noPlayer = new Player();
            noPlayer.setPassport(passport);
            return noPlayer;
        } else {
            return player;
        }
    }

    public GameTale getAllGameTale() {
        return wereWolfRepository.getGameTale();
    }

    public PlayerStats getPlayerStats(String playerId) {
        PlayerStats playerStats = playerRepository.getPlayerStats(playerId);
        Map topHurtPlayerCount = playerStats.getTopHurtPlayerCount();
        if (topHurtPlayerCount != null && topHurtPlayerCount.get("player") != null) {
            Player hurtPlayer = playerRepository.getPlayer((String) topHurtPlayerCount.get("player"));
            topHurtPlayerCount.put("playerName", hurtPlayer.getName());
        }
        List<Map> hurtedGroupByPlayerAndPatternCount = playerStats.getHurtedGroupByPlayerAndPatternCount();

        if (hurtedGroupByPlayerAndPatternCount != null) {
            for(Map map: hurtedGroupByPlayerAndPatternCount){
                Player initiative = playerRepository.getPlayer((String) map.get("initiative"));
                map.put("initiativeName", initiative.getName());
                map.put("actionName", Utils.transAction((String) map.get("action")));
            }
        }
        return playerStats;
    }

    public void computeStats() {
        wereWolfRepository.createPlayerStats();
    }

    public HonoraryTopOne getHonoraryPlayer() {
        HonoraryTopOne honoraryTopOne = new HonoraryTopOne();
        List<PlayerStats> playerStatses = playerRepository.getPlayerStatses();

        Integer villagerKilledMaxScore = 0;
        Map villagerKilledTopOne = new HashMap();

        Integer witchKillMaxScore = 0;
        Map witchKillTopOne = new HashMap();

        Integer seerFisrNightKilledMaxScore = 0;
        Map seerFisrNightKilledTopOne = new HashMap();

        Integer huntedMaxScore = 0;
        Map huntedTopOne = new HashMap();

        Map topHurtPlayerMaxScore = new HashMap();

        for (PlayerStats playerStats : playerStatses) {
            Player player = playerRepository.getPlayer(playerStats.getPlayerId());

            Integer villagerKilledScore = playerStats.getVillagerKilledCount();
            if (villagerKilledScore > villagerKilledMaxScore) {
                villagerKilledMaxScore = villagerKilledScore;
                villagerKilledTopOne.put("playerId", playerStats.getPlayerId());
                villagerKilledTopOne.put("playerName", player.getName());
                villagerKilledTopOne.put("maxScore", villagerKilledMaxScore);
            }
            Integer witchKillScore = playerStats.getWitchPoisonCount() - playerStats.getWitchVaildPoisonCount();
            if (witchKillScore > witchKillMaxScore) {
                witchKillMaxScore = witchKillScore;
                witchKillTopOne.put("playerId", playerStats.getPlayerId());
                witchKillTopOne.put("playerName", player.getName());
                witchKillTopOne.put("maxScore", witchKillMaxScore);
            }
            Integer seerFisrNightKilledScore = playerStats.getSeerFisrNightKilledCount();
            if (seerFisrNightKilledScore > seerFisrNightKilledMaxScore) {
                seerFisrNightKilledMaxScore = seerFisrNightKilledScore;
                seerFisrNightKilledTopOne.put("playerId", playerStats.getPlayerId());
                seerFisrNightKilledTopOne.put("playerName", player.getName());
                seerFisrNightKilledTopOne.put("maxScore", seerFisrNightKilledMaxScore);
            }
            Integer huntedScore = playerStats.getHuntedCount();
            if (huntedScore > huntedMaxScore) {
                huntedMaxScore = huntedScore;
                huntedTopOne.put("playerId", playerStats.getPlayerId());
                huntedTopOne.put("playerName", player.getName());
                huntedTopOne.put("maxScore", huntedMaxScore);
            }
            Map topHurtPlayerScore = playerStats.getTopHurtPlayerCount();
            if (topHurtPlayerScore.get("total") != null) {
                if (topHurtPlayerMaxScore.get("maxScore") == null) {
                    Player hurtPlayer = playerRepository.getPlayer((String) topHurtPlayerScore.get("player"));
                    topHurtPlayerMaxScore.put("hurtPlayerId", topHurtPlayerScore.get("player"));
                    topHurtPlayerMaxScore.put("hurtPlayerName", hurtPlayer.getName());
                    topHurtPlayerMaxScore.put("hurtedPlayerId", player.getPlayerId());
                    topHurtPlayerMaxScore.put("hurtedPlayerName", player.getName());
                    topHurtPlayerMaxScore.put("maxScore", topHurtPlayerScore.get("total"));
                } else {
                    Integer score = (Integer) topHurtPlayerScore.get("total");
                    Integer maxScore = (Integer) topHurtPlayerMaxScore.get("maxScore");
                    if (score > maxScore) {
                        Player hurtPlayer = playerRepository.getPlayer((String) topHurtPlayerScore.get("player"));
                        topHurtPlayerMaxScore.put("hurtPlayerId", topHurtPlayerScore.get("player"));
                        topHurtPlayerMaxScore.put("hurtPlayerName", hurtPlayer.getName());
                        topHurtPlayerMaxScore.put("hurtedPlayerId", player.getPlayerId());
                        topHurtPlayerMaxScore.put("hurtedPlayerName", player.getName());
                        topHurtPlayerMaxScore.put("maxScore", score);
                    }
                }
            }
        }

        Object[] objects = wereWolfRepository.getLoverPlayerTop();
        String playerAId = (String) ((ArrayList) objects[0]).get(0);
        String playerBId = (String) ((ArrayList) objects[0]).get(1);
        Integer total = (Integer) objects[1];
        Player playerA = playerRepository.getPlayer(playerAId);
        Player playerB = playerRepository.getPlayer(playerBId);
        Map loverPlayerTopOne = new HashMap();
        loverPlayerTopOne.put("playerAId", playerAId);
        loverPlayerTopOne.put("playerAName", playerA.getName());
        loverPlayerTopOne.put("playerBId", playerBId);
        loverPlayerTopOne.put("playerBName", playerB.getName());
        loverPlayerTopOne.put("maxScore", total);

        honoraryTopOne.setVillagerKilledTopOne(villagerKilledTopOne);
        honoraryTopOne.setWitchKillTopOne(witchKillTopOne);
        honoraryTopOne.setSeerFisrNightKilledTopOne(seerFisrNightKilledTopOne);
        honoraryTopOne.setHuntedTopOne(huntedTopOne);
        honoraryTopOne.setHurtPlayerTopOne(topHurtPlayerMaxScore);
        honoraryTopOne.setLoverPlayerTopOne(loverPlayerTopOne);
        return honoraryTopOne;
    }

}
