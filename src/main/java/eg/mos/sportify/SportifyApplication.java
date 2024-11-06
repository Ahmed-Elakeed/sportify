package eg.mos.sportify;

import eg.mos.sportify.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SportifyApplication {

    public static void main(String[] args) {
        LogUtil.getInstance(SportifyApplication.class).info("SportifyApplication started V1");
        LogUtil.getInstance(SportifyApplication.class).info("New logs for testing");
        SpringApplication.run(SportifyApplication.class, args);
    }

}
