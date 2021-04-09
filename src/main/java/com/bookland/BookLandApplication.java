package com.bookland;

import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.bookland.dao")
public class BookLandApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookLandApplication.class, args);
    }

    // Tomcat Cookie 處理，Tomcat 8 版本後面為遵循新的規範，不允許某些符號出現在 Cookie 裡面
    // 因此手動設定
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return (factory) -> factory.addContextCustomizers(
                (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}
