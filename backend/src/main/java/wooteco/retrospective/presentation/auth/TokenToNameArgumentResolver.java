package wooteco.retrospective.presentation.auth;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.retrospective.domain.auth.TokenToName;
import wooteco.retrospective.utils.auth.AuthorizationExtractor;
import wooteco.retrospective.utils.auth.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

public class TokenToNameArgumentResolver implements HandlerMethodArgumentResolver {

    private JwtTokenProvider jwtTokenProvider;

    public TokenToNameArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToName.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String credentials = AuthorizationExtractor.extract(webRequest.getNativeRequest(HttpServletRequest.class));

        return jwtTokenProvider.getPayload(credentials);
    }

}
