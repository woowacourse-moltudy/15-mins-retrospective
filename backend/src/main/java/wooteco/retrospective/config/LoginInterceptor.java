package wooteco.retrospective.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.retrospective.exception.AuthorizationException;
import wooteco.retrospective.utils.auth.AuthorizationExtractor;
import wooteco.retrospective.utils.auth.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println(new Date().toGMTString());
        System.out.println(new Date().getTimezoneOffset());
        java.util.TimeZone tz = java.util.TimeZone.getDefault();
        System.out.println("Timezone offset from UTC reported as " +
                (tz.getRawOffset() / 1000 / 60) + " minutes");
        if (tz.getRawOffset() % (15 * 60 * 1000) != 0) {
            System.out.println("Warning: not a multiple of quarter-hours");
        }
        System.out.println(new java.util.Date());
        System.out.println(tz);
        TimeZone tz2;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)");
        tz2 = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(tz2);
        System.out.format("%s%n%s%n%n", tz2.getDisplayName(), df.format(date));
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 토큰입니다."));

        String token = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }

        return true;
    }
}
