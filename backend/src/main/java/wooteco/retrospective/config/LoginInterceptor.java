package wooteco.retrospective.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.retrospective.exception.AuthorizationException;
import wooteco.retrospective.utils.auth.AuthorizationExtractor;
import wooteco.retrospective.utils.auth.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LoginInterceptor implements HandlerInterceptor {

    private JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        Optional.ofNullable( request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 토큰입니다."));

        String extractor = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(extractor)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }

        return true;
    }
}
