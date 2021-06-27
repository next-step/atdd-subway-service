package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final int AGE = 20;

    @BeforeEach()
    void setup() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        //when
        ExtractableResponse<Response> 회원_로그인_요청_응답 = 회원_로그인_요청(EMAIL, PASSWORD);
        //then
        로그인_토큰_확인(회원_로그인_요청_응답);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 회원_로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private static void 로그인_토큰_확인(ExtractableResponse<Response> 회원_로그인_요청_응답) {
        assertThat(회원_로그인_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        TokenResponse tokenResponse = 회원_로그인_요청_응답.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }
}
