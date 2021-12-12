package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String MY_EMAIL = "smpark911020@gmail.com";
    private static final String MY_PASSWORD = "smpark";
    private static final int MY_AGE = 31;
    private static final String NOT_FOUND_EMAIL = "notfound@email.com";
    private static final String WRONG_PASSWORD = "wrong_password";

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원이_등록되어_있음(MY_EMAIL, MY_PASSWORD, MY_AGE);
    }

    @DisplayName("로그인 기능")
    @Test
    void login_success() {
        // when
        ExtractableResponse<Response> 등록되지_않은_이메일_응답 = 등록되지_않은_이메일_로그인_요청(NOT_FOUND_EMAIL, MY_PASSWORD);

        // then
        로그인_실패함(등록되지_않은_이메일_응답);

        // when
        ExtractableResponse<Response> 잘못된_비밀번호_응답 = 잘못된_비밀번호로_로그인_요청(MY_EMAIL, WRONG_PASSWORD);

        // then
        assertThat(잘못된_비밀번호_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청함(MY_EMAIL, MY_PASSWORD);

        // then
        로그인_됨(로그인_응답);
    }

    @DisplayName("토큰 없이 내 정보 조회 시 실패한다.")
    @Test
    void myInfoWithBadBearerAuth() {

    }

    @DisplayName("유효하지 않은 토큰으로 내 정보 조회 시 실패한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private void 회원이_등록되어_있음(String email, String password, int age) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", email);
        param.put("password", password);
        param.put("age", age);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 등록되지_않은_이메일_로그인_요청(String email, String password) {
        return 로그인_요청함(email, password);
    }

    private ExtractableResponse<Response> 로그인_요청함(String email, String password) {
        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("password", password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 잘못된_비밀번호로_로그인_요청(String email, String password) {
        return 로그인_요청함(email, password);
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }
}
