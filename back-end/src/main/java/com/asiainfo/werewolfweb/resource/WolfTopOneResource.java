package com.asiainfo.werewolfweb.resource;

import com.asiainfo.werewolfweb.bean.WolfTopOne;
import com.asiainfo.werewolfweb.controller.WereWolfController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/15
 */
public class WolfTopOneResource extends ResourceSupport {

    @Getter
    @Setter
    private String playerId;
    @Getter
    @Setter
    private String playerName;
    @Getter
    @Setter
    private Double maxSocre;

    public WolfTopOneResource(WolfTopOne wolfTopOne) {
        this.playerId = wolfTopOne.getPlayerId();
        this.playerName = wolfTopOne.getPlayerName();
        this.maxSocre = wolfTopOne.getMaxSocre();

        this.add(linkTo(methodOn(WereWolfController.class).getWolfTopOne()).withSelfRel());
    }

}
