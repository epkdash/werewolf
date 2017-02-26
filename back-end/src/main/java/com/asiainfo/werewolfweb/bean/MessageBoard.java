package com.asiainfo.werewolfweb.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/15
 */
@Data
@Document(collection = "MessageBoard")
public class MessageBoard {
    private String boardId;
    private String playerId;
    private String name;
    private String message;
    private String isValid;
    private Date inDate;
}
