package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.토큰_회원정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_정보_조회됨;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "tester@naver.com";
    private static final String PASSWORD = "1234";
    private static final int AGE = 36;
    private static final String WRONG_TOKEN = "WrongToken";
    private static final String WRONG_PASSWORD = "111";
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD , AGE);
    }


    /**
     * Feature: 로그인 기능
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 로그인 됨
     */
    @DisplayName("로그인을 시도한다")
    @Test
    void authLoginTest() {
        // given: 회원 등록되어 있음
        // when: 로그인 요청
        TokenResponse loginResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
        // then: 로그인 됨
        로그인_성공(loginResponse);
    }

    /**
     *  Scenario: Bearer Auth 로 회원정보 조회
     *      Given 회원 생성, 로그인 요청하면
     *      When 전달받은 토큰으로 회원정보를 요청했을 때
     *      Then 회원정보가 조회된다.
     */
    @DisplayName("Bearer Auth 로 회원정보 조회")
    @Test
    void myInfoWithBearerAuth() {
        // given: 회원 생성, 로그인 요청하면
        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
        // when: 전달받은 토큰으로 회원정보를 요청했을 때
        ExtractableResponse<Response> response = 토큰_회원정보_조회_요청(tokenResponse);
        // Then 회원정보가 조회된다.
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     *  Scenario: 잘못된 비밀번호로 회원정보 조회
     *      Given 회원 생성
     *      When 잘못된 비밀번호로 로그인을 요청했을 때
     *      Unauthorized 를 돌려받는다.
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given: BeforeEach 에서 회원 생성
        // when: 잘못된 토큰으로 회원정보를 요청했을 때
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, WRONG_PASSWORD);
        // Then Then Bad Request 를 돌려받는다.
        로그인_실패(response);
    }


    /**
     *  Scenario: Bearer Auth 유효하지 않은 토큰으로 조회
     *      Given 잘못된 토큰을 생성한다
     *      When 잘못된 토큰으로 회원정보를 요청했을 때
     *      Then Bad Request 를 돌려받는다.
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given: 잘못된 토큰을 생성한다
        TokenResponse tokenResponse = 잘못된_토큰정보_생성();
        // when: 잘못된 토큰으로 회원정보를 요청했을 때
        ExtractableResponse<Response> response = 토큰_회원정보_조회_요청(tokenResponse);
        // Then Then Bad Request 를 돌려받는다.
        회원_정보_조회_안됨(response);
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

    public static void 로그인_성공(TokenResponse tokenResponse) {
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    public static TokenResponse 잘못된_토큰정보_생성() {
        return new TokenResponse(WRONG_TOKEN);
    }


    private void 회원_정보_조회_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



}
