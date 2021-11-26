package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_됨;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_사용자_정보_요청;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_사용자_정보_조회_실패;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_실패;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_요청;
import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_요청_실퍠;
import static nextstep.subway.member.step.MemberAcceptanceStep.회원_등록_되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("Bearer Auth")
    void myInfoWithBearerAuth() {
        // given
        String email = "email@email.com";
        String password = "password";
        회원_등록_되어_있음(email, password, 1);

        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_됨(response);
    }

    @Test
    @DisplayName("Bearer Auth 로그인 실패")
    void myInfoWithBadBearerAuth_invalidEmailPassword_401() {
        // given, when
        ExtractableResponse<Response> response = 로그인_요청("email@email.com", "password");

        // then
        로그인_실패(response);
    }

    @Test
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    void myInfoWithWrongBearerAuth_invalidToken_401() {
        // given, when
        ExtractableResponse<Response> response = 로그인_사용자_정보_요청("wrongToken");

        // then
        로그인_사용자_정보_조회_실패(response);
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 로그인 요청 불가능")
    @CsvSource({"email@email.com,", ",password"})
    @DisplayName("로그인시 이메일과 패스워드는 필수")
    void myInfoWithBadBearerAuth_nullEmailOrPassword_400(String email, String password) {
        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_요청_실퍠(response);
    }
}
