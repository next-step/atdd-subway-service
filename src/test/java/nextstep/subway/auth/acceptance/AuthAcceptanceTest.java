package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given 회원 등록되어 있음
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("로그인을 시도한다.")
    @Test
    void loginScenario() {
        // when
        ExtractableResponse<Response> response = 회원_로그인_요청(EMAIL, PASSWORD);

        // then
        회원_로그인_됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> loginResponse = 회원_로그인_요청(EMAIL, PASSWORD);
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();

        // then
        회원_로그인_됨(loginResponse);
        assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();

        // when
        ExtractableResponse<Response> myInfoResponse = 내정보_조회(accessToken);
        MemberResponse memberResponse = myInfoResponse.as(MemberResponse.class);

        //then
        내정보_조회됨(myInfoResponse);
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 회원_로그인_요청(EMAIL, "failed password");

        // then
        회원_로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String failedToken = "failed token";

        // when
        ExtractableResponse<Response> myInfoResponse = 내정보_조회(failedToken);

        //then
        내정보_조회_실패됨(myInfoResponse);
    }


    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, int age) {
        return 회원_생성_요청(email, password, age);
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .body(memberRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public void 회원_로그인_됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());

        TokenResponse tokenResponse = response.as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();

        assertThat(accessToken).isNotBlank();
    }

    public void 회원_로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.UNAUTHORIZED.value()).isEqualTo(response.statusCode());
    }

    public ExtractableResponse<Response> 내정보_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();
    }

    public void 내정보_조회됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());
    }

    public void 내정보_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.UNAUTHORIZED.value()).isEqualTo(response.statusCode());
    }
}
