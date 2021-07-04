package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private final String EMAIL = "mwkwon@test.com";
    private final String PASSWORD = "1234";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given: 아이디: mwkwon@test.com, 비밀번호: 1234, 나이: 37 회원 가입 되어 있음
        회원_가입_되어_있음(EMAIL, PASSWORD, 37);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when: 아이디: mwkwon@test.com, 비밀번호: 1234로 로그인 요청
        ExtractableResponse<Response> 로그인 = 로그인_요청(EMAIL, PASSWORD);
        // then: 로그인 됨
        로그인_성공(로그인);
        // then: 인증 토근 값이 존재함.
        assertThat(인증_토큰_반환(로그인)).isNotBlank();
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when: 아이디: mwkwon1@test.com, 비밀번호: 1234로 로그인 요청
        ExtractableResponse<Response> 아이디_다른_로그인_요청 = 로그인_요청("mwkwon1@test.com", PASSWORD);
        // then: 로그인 실패함
        로그인_및_인증_실패(아이디_다른_로그인_요청);

        // when: 아이디: mwkwon@test.com, 비밀번호: 4321로 로그인 요청
        ExtractableResponse<Response> 비밀번호_다른_로그인_요청 = 로그인_요청(EMAIL, "4321");
        // then: 로그인 실패함
        로그인_및_인증_실패(비밀번호_다른_로그인_요청);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given: 아이디: mwkwon@test.com, 비밀번호: 1234로 로그인되어 있음
        ExtractableResponse<Response> 로그인 = 로그인_요청(EMAIL, PASSWORD);
        String accessToken = 인증_토큰_반환(로그인);

        // when: accessToken 값을 null 값으로 내정보를 조회함
        ExtractableResponse<Response> 인증_토큰_없이_내정보_조회 = 내정보_조회(null);
        // then: 유효하지 않은 토큰으로 조회 실패
        로그인_및_인증_실패(인증_토큰_없이_내정보_조회);

        // when: accessToken 값을 null 값으로 내정보를 조회함
        ExtractableResponse<Response> 임의의_인증_토큰으로_내정보_조회 = 내정보_조회(accessToken + "test");
        // then: 유효하지 않은 토큰으로 조회 실패
        로그인_및_인증_실패(임의의_인증_토큰으로_내정보_조회);
    }


    private ExtractableResponse<Response> 회원_가입_되어_있음(String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured.given().log().all()
                .body(memberRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내정보_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private String 인증_토큰_반환(ExtractableResponse<Response> response) {
        return response.as(TokenResponse.class).getAccessToken();
    }

    private void 로그인_및_인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
