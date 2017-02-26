package com.asiainfo.werewolfweb.resource;

import com.asiainfo.werewolfweb.bean.Player;
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
public class PlayerResource extends ResourceSupport {

    @Getter
    @Setter
    private String playerId = "";
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String gender;
    @Getter
    @Setter
    private String vocable;
    @Getter
    @Setter
    private String passport = "";

    public PlayerResource(Player player) {
        this.playerId = player.getPlayerId();
        this.name = player.getName();
        this.gender = player.getGender();
        this.vocable = player.getVocable();
        this.passport = player.getPassport();

        this.add(linkTo(methodOn(WereWolfController.class).login(this.passport)).withSelfRel());
    }

}
