package nextstep.subway.auth.acceptance;

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

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 인증 기능 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "diqksrk123@naver.com";
    public static final String PASSWORD = "1234";
    private String LONG_TOKEN_INFO = "1234";
    private String LONG_MEMBER_URI = "/members/1";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD , AGE);
    }

    /**
     * Given 회원이 등록되어 있으면
     * When 로그인 요청하고
     * Then 로그인이 시행된다
     */
    @DisplayName("로그인 테스트")
    @Test
    void loginTest() {
        TokenResponse tokenResponse = 로그인_요청됨(EMAIL, PASSWORD);

        로그인_확인됨(tokenResponse);
    }

    public static TokenResponse 로그인_요청됨(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(TokenResponse.class);
    }

    public static void 로그인_확인됨(TokenResponse tokenResponse) {
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    /**
     * Given 회원 생성, 로그인 요청하면
     * When 전달받은 토큰으로 회원정보를 요청하면
     * Then 회원정보가 반환된다.
     */
    @DisplayName("Bearer Auth을 통한 정보 조회")
    @Test
    void myInfoWithBearerAuth() {
        TokenResponse tokenResponse = 로그인_요청됨(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 토큰_회원정보_조회_요청(tokenResponse);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    private ExtractableResponse<Response> 토큰_회원정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    /**
     * Given 잘못된 유저정보와 토큰을 생성한다.
     * When 잘못된 정보로 유저정보를 요청하면
     * Then Bad Reqeust를 돌려받는다.
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuthAndWrongBearerAuth() {
        TokenResponse tokenResponse = 잘못된_토큰정보_생성();

        ExtractableResponse<Response> response = 토큰_회원정보_조회_요청(tokenResponse);

        회원_정보_조회_안됨(response);
    }

    private void 회원_정보_조회_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private TokenResponse 잘못된_토큰정보_생성() {
        return new TokenResponse(LONG_TOKEN_INFO);
    }

}
