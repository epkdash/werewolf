package com.asiainfo.werewolfweb.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
@Document(collection = "Player")
@Data
public class Player
{
    private String playerId = "";
    private String name;
    private String gender;
    private String vocable;
    private String passport;

}
