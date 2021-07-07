package wooteco.retrospective.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.retrospective.presentation.auth.TokenToNameArgumentResolver;
import wooteco.retrospective.utils.auth.JwtTokenProvider;

import java.util.List;

@Configuration
public class TokenToNameConfig implements WebMvcConfigurer {

    @Value("${server.front.origin}")
    private String allowOrigin;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenToNameConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createTokenToNameArgumentResolver());
    }

    @Bean
    public TokenToNameArgumentResolver createTokenToNameArgumentResolver() {
        return new TokenToNameArgumentResolver(jwtTokenProvider);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider))
                .excludePathPatterns("/api/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns(allowOrigin);
    }
}
