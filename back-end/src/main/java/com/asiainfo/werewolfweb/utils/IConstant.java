package com.asiainfo.werewolfweb.utils;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/13
 */
public class IConstant {
    public interface Role {
        public static final String WOLF = "wolf";
        public static final String VILLAGER = "villager";
        public static final String WITCH = "witch";
        public static final String SEER = "seer";
        public static final String HUNTER = "hunter";
        public static final String WILD_CHILD = "wildChild";
        public static final String IDIOT = "idiot";
        public static final String CUPID = "cupid";
        public static final String WHITE_WOLF = "whiteWolf";
        public static final String MAID = "maid";
        public static final String LIGHT_KNIGHT = "lightKnight";
    }

    public interface WinType {
        public static final String SAFETY = "safety";
        public static final String BLOODY = "bloody";
        public static final String FOREVER = "forever";
        public static final String ALONE = "alone";
    }

//    public interface StatsCollation
//    {
//        public static final String countByRevive = "countByRevive";
//        public static final String countByReviveIsValid = "countByReviveIsValid";
//        public static final String countByPoison = "countByPoison";
//        public static final String countByPoisonIsValid = "countByPoisonIsValid";
//        public static final String countBySeerIsWinner = "countBySeerIsWinner";
//        public static final String groupCountByWolfWinForPlayer = "groupCountByWolfWinForPlayer";
//        public static final String groupCountByWolfPlayForPlayer = "groupCountByWolfPlayForPlayer";
//        public static final String groupCountByPlayForPlayer = "groupCountByPlayForPlayer";
//        public static final String groupCountByRoleForPlayer = "groupCountByRoleForPlayer";
//    }

    public interface ValidTag {
        public static final String VALID = "Y";
        public static final String IN_VAILD = "N";
    }

    public interface boardType {
        public static final String WOLF_TOP_ONE = "wolf";
        public static final String WITCH_TOP_ONE = "witch";
        public static final String SEER_TOP_ONE = "seer";
    }

    public interface action {
        public static final String MURDER = "murder";
        public static final String REVIVE = "revive";
        public static final String POISON = "poison";
        public static final String VOTE = "vote";
        public static final String HUNT = "hunt";
        public static final String IMPLICATE = "implicate";
    }
}
