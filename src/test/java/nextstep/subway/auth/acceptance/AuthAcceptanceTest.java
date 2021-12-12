package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.utils.RestTestApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @DisplayName("등록된 회원 정보와 이메일, 비밀번호가 일치하면 로그인이 성공한다.")
    @Test
    void myInfoWithBearerAuth() {
        // given 회원 등록되어 있음
        MemberAcceptanceTest.회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then 로그인 됨
        로그인_됨(response);
        유효한_액세스_토큰_반환됨(response);
    }

    @DisplayName("등록하지 않은 회원 정보로 로그인을 시도하면 로그인이 실패한다.")
    @Test
    void myInfoWithBadBearerAuth1() {
        // given 회원 등록되어 있음
        MemberAcceptanceTest.회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when 등록하지 않은 회원 정보로 로그인 요청
        String wrongEmail = "notmember@email.com";
        String wrongPassword = "notmember";
        ExtractableResponse<Response> response = 로그인_요청(wrongEmail, wrongPassword);

        // then 로그인 실패됨
        로그인_실패됨(response);
    }

    @DisplayName("등록 정보와 다른 이메일로 로그인을 시도하면 로그인이 실패한다.")
    @Test
    void myInfoWithBadBearerAuth2() {
        // given 회원 등록되어 있음
        MemberAcceptanceTest.회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when 잘못된 이메일로 로그인 요청
        String wrongEmail = "testemail@email.com";
        ExtractableResponse<Response> response2 = 로그인_요청(wrongEmail, PASSWORD);

        // then
        로그인_실패됨(response2);
    }

    @DisplayName("잘못된 비밀번호로 로그인을 시도하면 로그인이 실패한다.")
    @Test
    void myInfoWithBadBearerAuth3() {
        // given 회원 등록되어 있음
        MemberAcceptanceTest.회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when 잘못된 패스워드로 로그인 요청
        String wrongPassword = "secret";
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, wrongPassword);

        // then 로그인 실패됨
        로그인_실패됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 정보를 요청하면 실패한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when 유효하지 않은 토큰으로 정보 요청
        String wrongToken = "wrongToken";
        ExtractableResponse<Response> response = RestTestApi.get("/members/me", wrongToken);

        // then
        정보_요청_실패됨(response);
    }

    private void 정보_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 유효한_액세스_토큰_반환됨(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        String uri = "/login/token";
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestTestApi.post(uri, tokenRequest);
    }
}
