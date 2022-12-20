package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTestFixture.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTestFixture extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int NEW_AGE = 21;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    public static ExtractableResponse<Response> 토큰_생성_요청(TokenRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_되어_있음(TokenRequest request) {
        return 토큰_생성_요청(request);
    }

    public static String 토큰_값(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("accessToken");
    }

    public static void 토큰_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 토큰_생성_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_인증_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
