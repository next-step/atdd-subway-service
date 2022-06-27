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

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void init() {
        //when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //then
        회원_생성됨(createResponse);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        //then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //given
        String unmatchedPassword = "password1";

        //when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, unmatchedPassword);

        //then
        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰으로 내 정보 가져오기")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        String wrongAccessToken = "wrongToken";
        //when
        ExtractableResponse<Response> response = 내_정보_조회_요청(wrongAccessToken);
        //then
        내_정보_조회_실패됨(response);
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

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
