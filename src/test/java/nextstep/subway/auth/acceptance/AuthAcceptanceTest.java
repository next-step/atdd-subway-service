package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.utils.AcceptanceTestUtil.get;
import static nextstep.subway.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인에 성공하면 토큰을 발급 받는다")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_생성을_요청("email@google.com", "password", 20);

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청("email@google.com", "password");

        // then
        로그인_됨(로그인_응답);
        응답에_토큰_포함(로그인_응답);
    }

    @DisplayName("로그인에 실패하면 토큰을 발급 받지 못한다")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        회원_생성을_요청("email@google.com", "password", 20);

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청("email@google.com", "잘못된 비밀번호");

        // then
        로그인_실패(로그인_응답);
    }

    @DisplayName("유효하지 않은 토큰으로 API 호출")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> 내_정보_응답 = 내_정보_요청("invalid_token");

        // then
        토큰이_유효하지_않아_요청_실패(내_정보_응답);
    }

    @DisplayName("토큰없이 API 호출")
    @Test
    void myInfoWithoutBearerAuth() {
        // when
        ExtractableResponse<Response> 내_정보_응답 = 내_정보_요청();

        // then
        토큰이_유효하지_않아_요청_실패(내_정보_응답);
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return post("/login/token", new TokenRequest(email, password));
    }

    private ExtractableResponse<Response> 내_정보_요청(String token) {
        return get("/members/me", token);
    }

    private ExtractableResponse<Response> 내_정보_요청() {
        return get("/members/me");
    }

    private void 로그인_됨(ExtractableResponse<Response> 로그인_응답) {
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 응답에_토큰_포함(ExtractableResponse<Response> 로그인_응답) {
        assertThat(로그인_응답.as(TokenResponse.class).getAccessToken()).isNotBlank();
    }

    private void 로그인_실패(ExtractableResponse<Response> 로그인_응답) {
        assertThat(로그인_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 토큰이_유효하지_않아_요청_실패(ExtractableResponse<Response> 내_정보_응답) {
        assertThat(내_정보_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
