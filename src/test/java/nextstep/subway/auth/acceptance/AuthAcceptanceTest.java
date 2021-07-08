package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.나의_정보_확인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private String email;
    private String password;

    @BeforeEach
    public void setup() {
        super.setUp();
        email = "lsecret@naver.com";
        password = "test";
        회원_생성을_요청(email, password, 34);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        TokenRequest tokenRequest = new TokenRequest(email, password);

        // when
        ExtractableResponse<Response> response = 로그인_토큰_요청(tokenRequest);

        // then
        로그인_토큰_생성_됨(response);

    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        TokenRequest tokenRequest = new TokenRequest(email, "abcd");

        // when
        ExtractableResponse<Response> response = 로그인_토큰_요청(tokenRequest);

        // then
        로그인_요청_실패_됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        LoginMember loginMember = new LoginMember(email);
        String accessToken = "invalidToken";

        // when
        ExtractableResponse<Response> response = 나의_정보_확인_요청(loginMember, accessToken);

        // then
        토큰_인증_실패_됨(response);

    }

    public static ExtractableResponse<Response> 로그인_토큰_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static String 로그인_되어_있음(TokenRequest tokenRequest) {
        return 로그인_토큰_요청(tokenRequest).as(TokenResponse.class).getAccessToken();
    }

    private void 로그인_토큰_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
    }

    private void 로그인_요청_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 토큰_인증_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
