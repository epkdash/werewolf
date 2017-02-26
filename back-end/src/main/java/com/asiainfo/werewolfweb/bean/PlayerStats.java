package com.asiainfo.werewolfweb.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
@Document(collection = "PlayerStats")
@Data
public class PlayerStats {
    private String playerId;
    private Integer playCount;
    private Integer playWinCount;
    private List<Map> rolePlayGroupCount;
    private List<Map> roleWinPlayGroupCount;
    private Integer seerWinCount;
    private Integer witchPoisonCount;
    private Integer witchReviveCount;
    private Integer witchVaildPoisonCount;
    private Integer witchVaildReviveCount;
    private Integer wolfPlayCount;
    private Integer wolfWinCount;
    private Integer villagerKilledCount;
    private Integer seerFisrNightKilledCount;
    private Integer huntedCount;
    private List<Map> hurtedGroupByPlayerAndPatternCount;
    private Map topHurtPlayerCount;
}

