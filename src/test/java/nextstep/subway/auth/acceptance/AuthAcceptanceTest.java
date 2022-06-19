package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
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

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private final String MEMBER_EMAIL = "email1@email.com";
    private final String MEMBER_PASSWORD = "password1";
    private final Integer MEMBER_AGE = 30;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //given
        회원_생성을_요청(MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_AGE);
    }

    /**
     * Scenario: 로그인을 시도한다.
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_요청(MEMBER_EMAIL, MEMBER_PASSWORD);

        //then
        응답결과_확인(response, HttpStatus.OK);
        토큰_생성됨(response);
    }

    /**
     * Scenario: 로그인에 실패한다.
     * Given 회원 등록되어 있음.
     * When 잘못된 패스워드로 로그인 요청
     * Then UNAUTHORIZED 발생
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_요청(MEMBER_EMAIL, "잘못된_패스워드");

        //then
        응답결과_확인(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Scenario: 토큰을 검증한다.
     * Given 회원 등록되어 있음.
     * When 잘못된 토큰 확인 요청
     * Then INTERNAL_SERVER_ERROR 발생
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> response = 토큰_확인_요청("잘못된_토큰");

        //then
        응답결과_확인(response, HttpStatus.UNAUTHORIZED);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
                .given().log().all()
                .body(new TokenRequest(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 토큰_생성됨(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    private ExtractableResponse<Response> 토큰_확인_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

}
