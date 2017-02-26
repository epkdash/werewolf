package com.asiainfo.werewolfweb.utils;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
public class Utils {
    public static String genPlayerStatsCollectionName(String collection, String player) {
        return collection + "-" + player;
    }

    public static String getWinTypeByRole(String role) {
        switch (role) {
            case IConstant.Role.WOLF:
                return IConstant.WinType.BLOODY;
            case IConstant.Role.WHITE_WOLF:
                return IConstant.WinType.ALONE;
            default:
                return IConstant.WinType.SAFETY;
        }
    }

    public static String transAction(String action){
        switch (action){
            case IConstant.action.HUNT:
                return "猎杀";
            case IConstant.action.IMPLICATE:
                return "牵连";
            case IConstant.action.MURDER:
                return "谋杀";
            case IConstant.action.POISON:
                return "女巫毒";
            case IConstant.action.REVIVE:
                return "女巫救";
            case IConstant.action.VOTE:
                return "投票";
            default: return  "未知";
        }
    }
}
