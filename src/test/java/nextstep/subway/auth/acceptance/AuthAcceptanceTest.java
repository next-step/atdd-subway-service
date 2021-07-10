package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
		// Scenario : 로그인을 시도 시나리오
		// Given : 회원 등록되어 있음
		// When : 로그인 요청
		// Then : 로그인 됨
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
		// Scenario : 로그인 실패 시나리오
		// Given : 회원 등록되어있지 않음
		// When : 로그인 요청
		// Then : 로그인 실패
		// When : 회원 가입 후 로그인 요청(ID 틀림)
		// Then : 로그인 실패
		// When : 회원 가입 후 로그인 요청(비밀번호 틀림)
		// Then : 로그인 실패
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
		// Scenario : 유효하지 않은 토큰으로 /members/me 요청할 경우 에러 시나리오
		// Given : 회원등록되어 있지만, 유효하지 않은 토큰으로 요청 보냄
		// When : 로그인 요청
		// Then : 에러 발생
    }

}
