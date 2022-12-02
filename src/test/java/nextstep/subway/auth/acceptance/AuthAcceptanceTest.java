package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //  Given 회원 등록되어 있음
        //  When 로그인 요청
        //  Then 로그인 됨
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //  Given 회원 등록되어 있음
        //  When 로그인 요청
        //  Then 로그인 실패
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //  Given 회원 등록되어 있음
        // Given 회원 등록되어 있음
        //  given 로그인 요청
        //  When /members/me에 유효하지 않는 요청
        // then 요청 실패함.
    }

}
