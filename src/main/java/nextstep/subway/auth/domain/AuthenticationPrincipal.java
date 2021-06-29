package nextstep.subway.auth.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/* https://velog.io/@kingcjy/Spring-HandlerMethodArgumentResolver%EC%9D%98-%EC%82%AC%EC%9A%A9%EB%B2%95%EA%B3%BC-%EB%8F%99%EC%9E%91%EC%9B%90%EB%A6%AC
*  다시 읽어볼 것!
* */
public @interface AuthenticationPrincipal {
}
