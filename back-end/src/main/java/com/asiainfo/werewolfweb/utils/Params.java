package com.asiainfo.werewolfweb.utils;

import com.asiainfo.werewolfweb.bean.Player;
import com.asiainfo.werewolfweb.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/15
 */
@Component
public class Params {
//    @Autowired
//    public PlayerRepository playerRepository;
    private static List<Player> players = null;

    @Autowired
    public Params(PlayerRepository playerRepository) {
        if (players == null) {
            players = playerRepository.getPlayers();
        }
    }

    public static String transPlayerId(String playerId) {
        for (Player player : players) {
            if (player.getPlayerId().equals(playerId)) {
                return player.getName();
            }
        }
        return "匿名";
    }

}
