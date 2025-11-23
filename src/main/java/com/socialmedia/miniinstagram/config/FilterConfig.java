package com.socialmedia.miniinstagram.config;

import com.socialmedia.miniinstagram.security.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final TokenFilter tokenFilter;

    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilterRegistration() {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenFilter);
        registrationBean.addUrlPatterns("/api/*"); // tüm API için geçerli
        registrationBean.setOrder(1); // önce bu filter çalışsın
        return registrationBean;
    }
}
