package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
    }

    @DisplayName("로그인을 시도한다.")
    @Test
    void login() {
        // given 회원_등록_되어_있음

        // when 로그인_요청
        ExtractableResponse<Response> response = 회원_토큰_요청(EMAIL, PASSWORD);

        // then 로그인_됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given 회원_로그인_되어_있음
        String token = 회원_토큰_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when 회원_정보_요청
        ExtractableResponse<Response> response = 내_정보_조회_요청(token);

        // then 회원_정보_조회됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when 잘못된_정보로_로그인_시도
        ExtractableResponse<Response> response = 회원_토큰_요청("invalidEmail@gmail.com", "invalidPassword");

        // then 회원_정보_조회_불가
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when 잘못된_토큰으로_회원_정보_요청
        ExtractableResponse<Response> response = 내_정보_조회_요청("invalidToken");

        // then 회원_정보_조회_불가
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 회원_토큰_요청(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }


}
