package com.asiainfo.werewolfweb.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.werewolfweb.assembler.MessageBoardResourceAssembler;
import com.asiainfo.werewolfweb.assembler.TopOneBoardResourceAssembler;
import com.asiainfo.werewolfweb.bean.*;
import com.asiainfo.werewolfweb.resource.*;
import com.asiainfo.werewolfweb.serivce.WereWolfService;
import com.asiainfo.werewolfweb.utils.IConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author : huangchen
 * @description :
 * @date : 2016/12/5
 */
@RestController
@Slf4j
public class WereWolfController {

    @Autowired
    WereWolfService wereWolfService;

    /**
     * 登陆
     *
     * @return
     */
    @RequestMapping(value = "/login/passport/{passport}/", method = RequestMethod.GET)
    public PlayerResource login(@PathVariable("passport") String passport) {
        Player player = wereWolfService.login(passport);
        PlayerResource playerResource = new PlayerResource(player);
        return playerResource;
    }

    /**
     * 获取游戏描述
     *
     * @return
     */
    @RequestMapping(value = "/game/tale/", method = RequestMethod.GET)
    public GameTaleResource getAllGameTale() {
        GameTale gameTale = wereWolfService.getAllGameTale();
        GameTaleResource gameTaleResource = new GameTaleResource(gameTale);
        return gameTaleResource;
    }

    /**
     * 获取狼人topone
     *
     * @return
     */
    @RequestMapping(value = "/wolf/topone/", method = RequestMethod.GET)
    public WolfTopOneResource getWolfTopOne() {
        WolfTopOne wolfTopOne = wereWolfService.computeWolfTopOne();
        WolfTopOneResource wolfTopOneResource = new WolfTopOneResource(wolfTopOne);
        return wolfTopOneResource;
    }

    /**
     * 获取女巫topone
     *
     * @return
     */
    @RequestMapping(value = "/witch/topone/", method = RequestMethod.GET)
    public WitchTopOneResource getWitchTopOne() {
        WitchTopOne witchTopOne = wereWolfService.computeWitchTopOne();
        WitchTopOneResource witchTopOneResource = new WitchTopOneResource(witchTopOne);
        return witchTopOneResource;
    }

    /**
     * 获取预言家topone
     *
     * @return
     */
    @RequestMapping(value = "/seer/topone/", method = RequestMethod.GET)
    public SeerTopOneResource getSeerTopOne() {
        SeerTopOne seerTopOne = wereWolfService.computeSeerTopOne();
        SeerTopOneResource seerTopOneResource = new SeerTopOneResource(seerTopOne);
        return seerTopOneResource;
    }

    /**
     * 获取留言板信息
     *
     * @return
     */
    @RequestMapping(value = "/board/message/", method = RequestMethod.GET)
    public Resources<MessageBoardResource> queryMessageBoards() {
        List<MessageBoard> messageBoards = wereWolfService.queryMessageBoards(10);
        Resources<MessageBoardResource> resources = new Resources<>(new MessageBoardResourceAssembler
                (this.getClass(), MessageBoardResource.class).toResources(messageBoards));
        resources.add(linkTo(methodOn(WereWolfController.class).queryMessageBoards()).withSelfRel());
        return resources;
    }

    /**
     * 增加留言板信息
     *
     * @return
     */
    @RequestMapping(value = "/board/message/", method = RequestMethod.POST)
    public String addMessageBoard(@RequestBody String jsonString) {
        JSONObject requestJson = JSON.parseObject(jsonString);
        String playerId = requestJson.getString("playerId");
        String message = requestJson.getString("message");
        wereWolfService.addMessageBoard(playerId, message);
        return "";
    }

    /**
     * 获取topone留言信息
     *
     * @return
     */
    @RequestMapping(value = "/board/topone/", method = RequestMethod.GET)
    public Resources<TopOneBoardResource> queryTopOneBoards() {
        List<TopOneBoard> topOneBoards = wereWolfService.queryTopOneBoards();
        Resources<TopOneBoardResource> resources = new Resources<>(new TopOneBoardResourceAssembler
                (this.getClass(), TopOneBoardResource.class).toResources(topOneBoards));
        resources.add(linkTo(methodOn(WereWolfController.class).queryTopOneBoards()).withSelfRel());
        return resources;
    }

    /**
     * 获取topone单个留言信息
     *
     * @return
     */
    @RequestMapping(value = "/board/topone/player/{player}/type/{type}/", method = RequestMethod.GET)
    public TopOneBoardResource queryTopOneBoard(
            @PathVariable("player") String player,
            @PathVariable("type") String type) {
        TopOneBoard topOneBoard = wereWolfService.queryTopOneBoard(player, type);
        TopOneBoardResource topOneBoardResource = new TopOneBoardResource(topOneBoard);
        return topOneBoardResource;
    }

    /**
     * 增加topone wolf留言板信息
     *
     * @return
     */
    @RequestMapping(value = "/board/topone/wolf/", method = RequestMethod.POST)
    public String addWolfTopOneBoard(@RequestBody String jsonString) {
        JSONObject requestJson = JSON.parseObject(jsonString);
        String playerId = requestJson.getString("playerId");
        String message = requestJson.getString("message");
        String type = IConstant.boardType.WOLF_TOP_ONE;
        wereWolfService.removeTopOneBoard(playerId, type);
        wereWolfService.addTopOneBoard(playerId, message, type);
        return "";
    }

    /**
     * 增加topone witch留言板信息
     *
     * @return
     */
    @RequestMapping(value = "/board/topone/witch/", method = RequestMethod.POST)
    public String addWitchTopOneBoard(@RequestBody String jsonString) {
        JSONObject requestJson = JSON.parseObject(jsonString);
        String playerId = requestJson.getString("playerId");
        String message = requestJson.getString("message");
        String type = IConstant.boardType.WITCH_TOP_ONE;
        wereWolfService.removeTopOneBoard(playerId, type);
        wereWolfService.addTopOneBoard(playerId, message, type);
        return "";
    }

    /**
     * 增加topone seer留言板信息
     *
     * @return
     */
    @RequestMapping(value = "/board/topone/seer/", method = RequestMethod.POST)
    public String addSeerTopOneBoard(@RequestBody String jsonString) {
        JSONObject requestJson = JSON.parseObject(jsonString);
        String playerId = requestJson.getString("playerId");
        String message = requestJson.getString("message");
        String type = IConstant.boardType.SEER_TOP_ONE;
        wereWolfService.removeTopOneBoard(playerId, type);
        wereWolfService.addTopOneBoard(playerId, message, type);
        return "";
    }

    /**
     * 获取单个玩家统计
     *
     * @return
     */
    @RequestMapping(value = "/player/{playerId}/stats/", method = RequestMethod.GET)
    public PlayerStatsResource queryTopOneBoard(
            @PathVariable("playerId") String playerId) {
        PlayerStats playerStats = wereWolfService.getPlayerStats(playerId);
        PlayerStatsResource playerStatsResource = new PlayerStatsResource(playerStats);
        return playerStatsResource;
    }

    /**
     * 获取荣耀榜
     *
     * @return
     */
    @RequestMapping(value = "/honorary/player/", method = RequestMethod.GET)
    public HonoraryTopOneResource getHonoraryPlayer() {
        HonoraryTopOne honoraryTopOne = wereWolfService.getHonoraryPlayer();
        HonoraryTopOneResource honoraryTopOneResource = new HonoraryTopOneResource(honoraryTopOne);
        return honoraryTopOneResource;
    }

    /**
     * 计算统计数据
     *
     * @return
     */
    @RequestMapping(value = "/compute/stats/", method = RequestMethod.GET)
    public String computeStats() {
        wereWolfService.computeStats();
        return "ok";
    }

}
