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
        TokenResponse tokenResponse = 로그인.as(TokenResponse.class);
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when: 아이디: mwkwon1@test.com, 비밀번호: 1234로 로그인 요청
        ExtractableResponse<Response> 아이디_다른_로그인_요청 = 로그인_요청("mwkwon1@test.com", PASSWORD);
        // then: 로그인 실패함
        로그인_실패(아이디_다른_로그인_요청);

        // when: 아이디: mwkwon@test.com, 비밀번호: 4321로 로그인 요청
        ExtractableResponse<Response> 비밀번호_다른_로그인_요청 = 로그인_요청(EMAIL, "4321");
        // then: 로그인 실패함
        로그인_실패(비밀번호_다른_로그인_요청);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
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

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
