package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "apple2021@email.com";
    private static final String PASSWORD = "mango";
    private static final int AGE = 20;
    private static final String INVALID_TOKEN = "invalidToken";

    @BeforeEach
    void beforeEach() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("올바른 이메일과 비밀번호로 로그인한다")
    @Test
    void 올바른_이메일_비밀번호로_로그인() {
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(EMAIL, PASSWORD);
        로그인_성공(로그인응답);
    }

    @DisplayName("등록되지 않은 이메일로 로그인한다")
    @Test
    void 등록되지_않은_이메일로_로그인_시도() {
        String 미등록_이메일 = "invalid@email.com";
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(미등록_이메일, PASSWORD);

        로그인_실패(로그인응답);
    }

    @DisplayName("잘못된 비밀번호로 로그인한다")
    @Test
    void 잘못된_비밀번호로_로그인_시도() {
        //로그인_요청
        String 틀린_비밀번호 = "wrong password";
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(EMAIL, 틀린_비밀번호);

        로그인_실패(로그인응답);
    }

    @DisplayName("유효하지 않은 토큰으로 내 정보를 조회한다")
    @Test
    void 유효하지_않은_토큰으로_내정보_조회() {
        //내정보_조회_요청
        ExtractableResponse<Response> 조회응답 = 내정보_조회_요청(INVALID_TOKEN);
        조회_실패(조회응답);

    }

    @DisplayName("유효하지 않은 토큰으로 즐겨찾기를 조회한다")
    @Test
    void 유효하지_않은_토큰으로_즐겨찾기_조회() {
        //즐겨찾기_조회_요청
        //조회_실패
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotEmpty();
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when()
                .post("/login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내정보_조회_요청(String token) {

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();
    }
}
