package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private String email = "test@nextstep.com";
    private String password = "password";
    private Integer age = 5;

    private String incorrectPassword = "incorrectPassword";
    private String incorrectToken = "abc.def.efg";


    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(email, password, age);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(email, incorrectPassword);

        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = 내_정보_조회_요청(incorrectToken);

        내_정보_조회_실패됨(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 내_정보_조회_요청(String token) {
        return RestAssured
            .given().log().all()
            .auth()
            .oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 내_정보_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
