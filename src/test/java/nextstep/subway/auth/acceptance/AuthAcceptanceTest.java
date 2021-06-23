package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private String email = "joenggyu0@gmail.com";
    private String password = "password";
    private TokenRequest request;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(email, password, 30);
        request = new TokenRequest(email, password);
    }

    @DisplayName("로그인을 시도")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 토큰을_요청한다(request);
        정상적으로_동작됨(response);
        정상적으로_동작됨(토큰으로_로그인_요청함(response.as(TokenResponse.class)));

    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 토큰으로_로그인_요청함(new TokenResponse("token"));
        인증되지_않았음(response);
    }

    @DisplayName("토큰으로 로그인을 시도한다")
    @Test
    void tokenTest() {
        정상적으로_동작됨(토큰을_요청한다(request));
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        인증되지_않았음(토큰을_요청한다(new TokenRequest("ABC@naver.com", "passwd")));
    }

    private void 인증되지_않았음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 정상적으로_동작됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 토큰으로_로그인_요청함(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 토큰을_요청한다(TokenRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }
}
