package com.kunal.twitterlite;

import com.kunal.twitterlite.filter.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TwitterLiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterLiteApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        AuthFilter authFilter = new AuthFilter();
        registrationBean.setFilter(authFilter);
        final String[] URL_PATTERNS = new String[] {"/api/following/*", "/api/admin/*", "/api/follower/*", "/api/post/*" };
        registrationBean.addUrlPatterns(URL_PATTERNS);
        return registrationBean;
    }

}
