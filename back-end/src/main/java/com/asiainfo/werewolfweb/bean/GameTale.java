package com.asiainfo.werewolfweb.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
@Document(collection = "GameTale")
@Data
public class GameTale {
    private Map honorary;
    private Map mainTale;
    private Map topBroad;

}
