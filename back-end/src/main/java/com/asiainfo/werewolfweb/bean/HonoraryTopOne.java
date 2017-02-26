package com.asiainfo.werewolfweb.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * @author : huangchen
 * @description :
 * @date : 2017/2/14
 */
@Document(collection = "PlayerStats")
@Data
public class HonoraryTopOne {
    private Map villagerKilledTopOne;
    private Map witchKillTopOne;
    private Map seerFisrNightKilledTopOne;
    private Map huntedTopOne;
    private Map loverPlayerTopOne;
    private Map hurtPlayerTopOne;
}

