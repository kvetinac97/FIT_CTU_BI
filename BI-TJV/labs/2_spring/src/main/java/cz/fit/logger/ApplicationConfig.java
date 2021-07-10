package cz.fit.logger;

import cz.fit.logger.service.SlackService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class ApplicationConfig {

    @Bean
    public SlackService slackService() {
        return new SlackService();
    }

    @Bean
    public Logger logger() {
        return new Logger(slackService());
    }


}
