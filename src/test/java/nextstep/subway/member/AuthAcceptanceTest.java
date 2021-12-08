package nextstep.subway.member;


import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.AuthRequest;
import nextstep.subway.auth.dto.AuthResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("인증을 통한 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> 회원;
    ExtractableResponse<Response> loginResponse;
    @BeforeEach
    public void setUp() {
        회원 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        AuthResponse authResponse = 회원.as(AuthResponse.class);
        loginResponse = 로그인_요청(authResponse);
    }

    @Test
    void 회원정보가_있는경우_토큰_발급() {
        // then
        응답_정상(loginResponse);
        로그인_됨(loginResponse);
    }

    @Test
    void 회원정보가_없는경우_토큰_생성_실패() {
        // then
        토큰_생성_실패(loginResponse);
    }

    private static void 토큰_생성_실패(ExtractableResponse<Response> loginResponse) {
        AuthResponse authResponse = loginResponse.as(AuthResponse.class);
        Assertions.assertThat(loginResponse.statusCode()).isEqualTo(BAD_REQUEST.value());
        Assertions.assertThat(authResponse).isNull();
    }


    public static ExtractableResponse<Response> 로그인_요청(AuthResponse memberResponse) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(AuthRequest.of(EMAIL, PASSWORD))
            .when().post("/members/me")
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 내_정보_조회하기(AuthResponse memberResponse) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(AuthRequest.of(EMAIL, PASSWORD))
            .when().post("/members/me")
            .then().log().all()
            .extract();
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        AuthResponse authResponse = response.as(AuthResponse.class);
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getAccessToken()).isNotNull();
    }

    public static void 응답_정상(ExtractableResponse<Response> response) {
        assertThat(response).isEqualTo(OK.value());
    }

}
