package pl.selfcloud.chat.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "pl.selfcloud.chat.web",
    "pl.selfcloud.chat.domain",
    "pl.selfcloud.chat.infrastructure",
    "pl.selfcloud.chat.saga",
    "pl.selfcloud.chat.api"
})
@EnableJpaRepositories(basePackages = "pl.selfcloud.chat.domain.repository")
@EntityScan(basePackages = "pl.selfcloud.chat.domain.model")
public class SelfcloudChatApplication 
{
    public static void main(String[] args) {
        SpringApplication.run(SelfcloudChatApplication.class, args);
    }
}
