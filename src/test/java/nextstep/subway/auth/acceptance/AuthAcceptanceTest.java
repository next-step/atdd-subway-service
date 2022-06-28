package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_내_정보_조회;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private String email = "test@naver.com";
    private String password = "1234";

    @BeforeEach
    public void setup() {
        super.setUp();

        회원_등록되어_있음(email, password, 20);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        TokenResponse tokenResponse = 로그인_요청(email, password).as(TokenResponse.class);

        // then
        로그인_됨(tokenResponse.getAccessToken(), email);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(email, "1212");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @ParameterizedTest(name = "이메일형식이 아닌경우 로그인 실패")
    @NullAndEmptySource
    @ValueSource(strings = {"test"})
    void myInfoWithBadEmailFormat(String email) {
        // when
        ExtractableResponse<Response> response = 로그인_요청(email, "1212");

        // then
        잘못된_로그인정보로_로그인_실패(response);
    }

    @ParameterizedTest(name = "비밀번호가 잘못입력된 경우 로그인 실패")
    @NullAndEmptySource
    void myInfoWithBadPassword(String password) {
        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        잘못된_로그인정보로_로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        로그인_요청(email, password);

        // then
        유효하지_않는_토큰_로그인_실패();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String pw) {
        TokenRequest tokenRequest = new TokenRequest(email, pw);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_됨(String accessToken, String email) {
        MemberResponse memberResponse = 회원_내_정보_조회(accessToken).as(MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
    }

    private void 잘못된_로그인정보로_로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 유효하지_않는_토큰_로그인_실패() {
        ExtractableResponse<Response> response = 회원_내_정보_조회("유효하지 않는 토큰");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
