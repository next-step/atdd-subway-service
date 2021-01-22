package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

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

    @BeforeEach
    public void setUp() {
        // Given 회원 등록되어 있음
        MemberAcceptanceTest.회원_생성을_요청("success@email.com", "success", 20);
    }

    @DisplayName("Bearer Auth : 로그인 정상 시도")
    @Test
    void myInfoWithBearerAuth() {
        // Given 회원 등록되어 있음 (setUp 메소드 참고)

        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청("success@email.com", "success");

        // then
        로그인_됨(로그인_요청_응답);
    }

    @DisplayName("Bearer Auth : 이메일 or 패스워드 틀림")
    @Test
    void myInfoWithBadBearerAuth() {
        // Given 회원 등록되어 있음 (setUp 메소드 참고)

        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청("fail@email.com", "fail");

        // then
        로그인_실패됨(로그인_요청_응답);
    }

    @DisplayName("Bearer Auth : 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // Given 회원 등록되어 있음 (setUp 메소드 참고)

        // when
        ExtractableResponse<Response> 회원_정보_조회_응답 = MemberAcceptanceTest.나의_정보_조회_요청("aaa");

        // then
        유효하지_않은_토큰임(회원_정보_조회_응답);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new TokenRequest(email, password))
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(로그인_토큰_추출함(response)).isNotBlank();
    }

    public static String 로그인_토큰_추출함(ExtractableResponse<Response> response) {
        return response.as(TokenResponse.class).getAccessToken();
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 유효하지_않은_토큰임(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
