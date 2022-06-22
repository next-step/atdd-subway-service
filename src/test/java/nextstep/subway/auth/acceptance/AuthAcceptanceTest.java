package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성됨(
                MemberAcceptanceTest.회원_생성을_요청(
                        MemberAcceptanceTest.EMAIL,
                        MemberAcceptanceTest.PASSWORD,
                        MemberAcceptanceTest.AGE
                ));
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        final ExtractableResponse<Response> response = 로그인_요청(
                MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);

        // then
        로그인_성공(response);

        // then
        토큰_발급됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        final ExtractableResponse<Response> response = 로그인_요청(MemberAcceptanceTest.EMAIL, "wrong-password");

        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        final String wrongToken = "wrong-token";

        // when
        final ExtractableResponse<Response> response = MemberAcceptanceTest.내_정보_조회_요청(wrongToken);

        // then
        MemberAcceptanceTest.내_정보_조회_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public void 토큰_발급됨(ExtractableResponse<Response> response) {
        final TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }
}
