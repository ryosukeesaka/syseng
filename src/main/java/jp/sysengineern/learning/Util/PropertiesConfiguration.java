package jp.sysengineern.learning.Util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {
    @Bean
    public BaseURL url() {
        return new BaseURL();
    }
}
