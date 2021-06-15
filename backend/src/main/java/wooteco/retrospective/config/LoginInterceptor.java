package wooteco.retrospective.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.retrospective.infrastructure.auth.AuthorizationExtractor;
import wooteco.retrospective.infrastructure.auth.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    private JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String accessToken = request.getHeader("Authorization");

        if (accessToken == null) {
            throw new IllegalArgumentException();
        }

        String extractor = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(extractor)) {
            throw new IllegalArgumentException();
        }

        return true;
    }
}
