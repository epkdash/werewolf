package app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : huangchen
 * @description :
 * @date : 2016/12/5
 */
@SpringBootApplication
@ComponentScan(basePackages =
        {
                "com.asiainfo.werewolfweb"
        })
@EnableAutoConfiguration
@Slf4j
public class WebApplication
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        SpringApplication.run(WebApplication.class, args);
    }
}
