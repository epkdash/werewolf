package com.asiainfo.werewolfweb.resource;

import com.asiainfo.werewolfweb.bean.MessageBoard;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;


/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/15
 */
public class MessageBoardResource extends ResourceSupport {
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
    private String isValid;
    @Getter
    @Setter
    private Date inDate;

    public MessageBoardResource(MessageBoard messageBoard) {
        this.boardId = messageBoard.getBoardId();
        this.playerId = messageBoard.getPlayerId();
        this.name = messageBoard.getName();
        this.message = messageBoard.getMessage();
        this.isValid = messageBoard.getIsValid();
        this.inDate = messageBoard.getInDate();

        //这里获取单条
//        this.add(linkTo(methodOn(WereWolfController.class).queryMessageBoard(boardId)).withSelfRel());
    }

}
