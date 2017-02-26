package com.asiainfo.werewolfweb.resource;

import com.asiainfo.werewolfweb.bean.HonoraryTopOne;
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
public class HonoraryTopOneResource extends ResourceSupport {
    @Getter
    @Setter
    private Map villagerKilledTopOne;
    @Getter
    @Setter
    private Map witchKillTopOne;
    @Getter
    @Setter
    private Map seerFisrNightKilledTopOne;
    @Getter
    @Setter
    private Map huntedTopOne;
    @Getter
    @Setter
    private Map loverPlayerTopOne;
    @Getter
    @Setter
    private Map hurtPlayerTopOne;

    public HonoraryTopOneResource(HonoraryTopOne honoraryTopOne) {
        villagerKilledTopOne = honoraryTopOne.getVillagerKilledTopOne();
        witchKillTopOne = honoraryTopOne.getWitchKillTopOne();
        seerFisrNightKilledTopOne = honoraryTopOne.getSeerFisrNightKilledTopOne();
        huntedTopOne = honoraryTopOne.getHuntedTopOne();
        loverPlayerTopOne = honoraryTopOne.getLoverPlayerTopOne();
        hurtPlayerTopOne = honoraryTopOne.getHurtPlayerTopOne();

        this.add(linkTo(methodOn(WereWolfController.class).getHonoraryPlayer()).withSelfRel());
    }

}
