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

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        //given 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        TokenResponse tokenResponse = 로그인_요청_성공(EMAIL, PASSWORD);
        //when
        ExtractableResponse<Response> response = 토큰을_이용한_내정보_조회(tokenResponse);
        //then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when && then
        로그인_요청_실패(EMAIL, BAD_PASSWORD);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        로그인_요청_성공(EMAIL, PASSWORD);
        //when
        ExtractableResponse<Response> response = 토큰을_이용한_내정보_조회(new TokenResponse("badtoken"));
        //then
        회원_정보_조회_실패됨(response);
    }

    public static TokenResponse 로그인_요청_성공(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(TokenResponse.class);
    }

    public static void 로그인_요청_실패(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }
}
