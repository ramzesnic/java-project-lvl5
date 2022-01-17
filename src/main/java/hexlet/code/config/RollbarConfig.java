package hexlet.code.config;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration()
@EnableWebMvc
@ComponentScan({
        "hexlet.code",
        "com.rollbar.spring"

})
public class RollbarConfig {
    @Bean
    public Rollbar rollbar() {
        return new Rollbar(getRollbarConfigs("${ROLLBAR_TOKEN}"));
    }

    private Config getRollbarConfigs(String accessToken) {
        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                .environment("${APP_ENV}")
                .build();
    }
}
