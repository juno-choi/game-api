package com.juno.gameapi.webconfig;

import com.juno.gameapi.interceptor.LogInterceptor;
import com.juno.gameapi.interceptor.TokenInterceptor;
import com.juno.gameapi.webclient_config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final WebClientConfig webClientConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //log interceptor
        registry.addInterceptor(new LogInterceptor())   //interceptor 등록
                .order(1)   //우선도
                .addPathPatterns("/**")  //사용될 url
                ;

        //login token check interceptor
        registry.addInterceptor(new TokenInterceptor(webClientConfig))
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/api/member/login", "/api/member/join", "/api/google/places", "/error")
                ;
    }
}
