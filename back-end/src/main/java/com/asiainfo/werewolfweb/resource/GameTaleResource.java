package com.asiainfo.werewolfweb.resource;

import com.asiainfo.werewolfweb.bean.GameTale;
import com.asiainfo.werewolfweb.controller.WereWolfController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/15
 */
public class GameTaleResource extends ResourceSupport {
    @Getter
    @Setter
    private Map honorary;
    @Getter
    @Setter
    private Map mainTale;
    @Getter
    @Setter
    private Map topBroad;

    public GameTaleResource(GameTale gameTale) {
        this.honorary = gameTale.getHonorary();
        this.mainTale = gameTale.getMainTale();
        this.topBroad = gameTale.getTopBroad();

        this.add(linkTo(methodOn(WereWolfController.class).getAllGameTale()).withSelfRel());
    }

}
