package wooteco.retrospective.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.retrospective.utils.auth.JwtTokenProvider;
import wooteco.retrospective.presentation.auth.TokenToNameArgumentResolver;

import java.util.List;

@Configuration
public class TokenToNamelConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenToNamelConfig(JwtTokenProvider jwtTokenProvider) {
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
}
