package com.asiainfo.werewolfweb.resource;

import com.asiainfo.werewolfweb.bean.TopOneBoard;
import com.asiainfo.werewolfweb.controller.WereWolfController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/15
 */
public class TopOneBoardResource extends ResourceSupport {
    @Getter
    @Setter
    private String boardId;
    @Getter
    @Setter
    private String playerId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private String isValid;
    @Getter
    @Setter
    private Date inDate;

    public TopOneBoardResource(TopOneBoard topOneBoard) {
        this.boardId = topOneBoard.getBoardId();
        this.playerId = topOneBoard.getPlayerId();
        this.name = topOneBoard.getName();
        this.message = topOneBoard.getMessage();
        this.type = topOneBoard.getType();
        this.isValid = topOneBoard.getIsValid();
        this.inDate = topOneBoard.getInDate();

        this.add(linkTo(methodOn(WereWolfController.class).queryTopOneBoard(this.playerId, this.type)).withSelfRel());

    }

}
