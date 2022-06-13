package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    /**
     *  Given 멤버가 주어지고
     *  When 해당 멤버 id / password로 로그인을 하면
     *  Then Bearer 토큰을 받아온다
     */
    @Test
    @DisplayName("멤버의 정보로 로그인을 하면 정상적으로 Bearer 토큰을 받아온다")
    void myInfoWithBearerAuth() {
        // given

        // when

        // then
    }

    /**
     *  When 올바르지 않은 id / password로 로그인을 하면
     *  Then 로그인이 실패한다
     */
    @Test
    @DisplayName("올바르지 않은 정보로 로그인을 하면 로그인이 실패한다")
    void myInfoWithBadBearerAuth() {
        // when

        // then
    }

    /**
     *  When 올바르지 않은 Bearer 토큰으로 호출하면
     *  Then 조회할 수 없다
     */
    @Test
    @DisplayName("올바르지 않는 토큰으로 조회 할 수 없다")
    void myInfoWithWrongBearerAuth() {
        // when

        // then
    }

}
