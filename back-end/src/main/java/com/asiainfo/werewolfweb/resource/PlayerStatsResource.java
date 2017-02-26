package com.asiainfo.werewolfweb.resource;

import com.asiainfo.werewolfweb.bean.PlayerStats;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Map;


/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/15
 */
public class PlayerStatsResource extends ResourceSupport {
    @Getter
    @Setter
    private String playerId;
    @Getter
    @Setter
    private Integer playCount;
    @Getter
    @Setter
    private Integer playWinCount;
    @Getter
    @Setter
    private List<Map> rolePlayGroupCount;
    @Getter
    @Setter
    private List<Map> roleWinPlayGroupCount;
    @Getter
    @Setter
    private Integer seerWinCount;
    @Getter
    @Setter
    private Integer witchPoisonCount;
    @Getter
    @Setter
    private Integer witchReviveCount;
    @Getter
    @Setter
    private Integer witchVaildPoisonCount;
    @Getter
    @Setter
    private Integer witchVaildReviveCount;
    @Getter
    @Setter
    private Integer wolfPlayCount;
    @Getter
    @Setter
    private Integer wolfWinCount;
    @Getter
    @Setter
    private Integer villagerKilledCount;
    @Getter
    @Setter
    private Integer seerFisrNightKilledCount;
    @Getter
    @Setter
    private Integer huntedCount;
    @Getter
    @Setter
    private List<Map> hurtedGroupByPlayerAndPatternCount;
    @Getter
    @Setter
    private Map topHurtPlayerCount;

    public PlayerStatsResource(PlayerStats playerStats) {
        this.playerId = playerStats.getPlayerId();
        this.playCount = playerStats.getPlayCount();
        this.playWinCount = playerStats.getPlayWinCount();
        this.rolePlayGroupCount = playerStats.getRolePlayGroupCount();
        this.roleWinPlayGroupCount = playerStats.getRoleWinPlayGroupCount();
        this.seerWinCount = playerStats.getSeerWinCount();
        this.witchPoisonCount = playerStats.getWitchPoisonCount();
        this.witchReviveCount = playerStats.getWitchReviveCount();
        this.witchVaildPoisonCount = playerStats.getWitchVaildPoisonCount();
        this.witchVaildReviveCount = playerStats.getWitchVaildReviveCount();
        this.wolfPlayCount = playerStats.getWolfPlayCount();
        this.wolfWinCount = playerStats.getWolfWinCount();
        this.villagerKilledCount = playerStats.getVillagerKilledCount();
        this.seerFisrNightKilledCount = playerStats.getSeerFisrNightKilledCount();
        this.huntedCount = playerStats.getHuntedCount();
        this.hurtedGroupByPlayerAndPatternCount = playerStats.getHurtedGroupByPlayerAndPatternCount();
        this.topHurtPlayerCount = playerStats.getTopHurtPlayerCount();

        //这里获取单条
//        this.add(linkTo(methodOn(WereWolfController.class).queryMessageBoard(boardId)).withSelfRel());
    }

}
