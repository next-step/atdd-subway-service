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
import static nextstep.subway.member.MemberAcceptanceTest.회원_정보를_토큰으로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private TokenRequest 로그인_유저;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        로그인_유저 = new TokenRequest("sgkim94@github.com", "123456");
        회원_생성을_요청(로그인_유저, 28);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 회원_로그인을_요청(로그인_유저);

        // then
        TokenResponse token = response.body().as(TokenResponse.class);

        로그인_성공됨(response);
        토큰이_포함됨(token);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 잘못된_회원_인증_요청("sgkim94@github.com", "234567");

        // then
        회원_인증_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        String wrongToken = "Bearer sadfjowejfoawjefoqwo";

        // when
        ExtractableResponse<Response> response = 회원_정보를_토큰으로_조회_요청(wrongToken);

        // then
        회원_인증_실패됨(response);
    }

    private ExtractableResponse<Response> 잘못된_회원_인증_요청(String email, String wrongPassword) {
        TokenRequest request = new TokenRequest(email, wrongPassword);
        return 회원_로그인을_요청(request);
    }


    private static void 로그인_성공됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 회원_인증_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static void 토큰이_포함됨(TokenResponse token) {
        assertThat(token.getAccessToken()).isNotBlank();
    }

    public static String 회원_로그인_됨(TokenRequest request) {
        ExtractableResponse<Response> response = 회원_로그인을_요청(request);
        로그인_성공됨(response);

        TokenResponse token = response.body().as(TokenResponse.class);
        토큰이_포함됨(token);

        return token.getAccessToken();
    }

    private static ExtractableResponse<Response> 회원_로그인을_요청(TokenRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }
}
