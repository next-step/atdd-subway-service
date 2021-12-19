package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.dto.TokenRequest;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static String email = "changchang743@gmail.com";
    public static String password = "1234";
    public static int age = 29;

    @DisplayName("로그인을 통한 토큰 발급 (Bearer Auth)")
    @Test
    void myInfoWithBearerAuth() {
        // given
        ExtractableResponse<Response> 회원_등록_응답 = 회원_등록되어_있음(email, password, age);

        // when
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(email, password);

        // then
        로그인_됨(로그인_응답);
    }


    @DisplayName("등록되어 있지 않은 이메일 로그인 시도 (Bearer Auth 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuthToWrongEmail() {
        assertThatExceptionOfType(AuthorizationException.class)
            .isThrownBy(() -> {
                // given
                ExtractableResponse<Response> 회원_등록_응답 = 회원_등록되어_있음(email, password, age);
                String wrongEmail = "wrongEmail@gmail.com";

                // when
                ExtractableResponse<Response> 로그인_응답 = 로그인_요청(wrongEmail, password);
            });
    }

    @DisplayName("비밀번호가 일치 않은 로그인 시도 (Bearer Auth 로그인 실패)")
    @Test
    void myInfoWithBadBearerAuthToWrongPassword() {
        assertThatExceptionOfType(AuthorizationException.class)
            .isThrownBy(() -> {
                // given
                ExtractableResponse<Response> 회원_등록_응답 = 회원_등록되어_있음(email, password, age);
                String wrongPassword = "wrongPassword";

                // when
                ExtractableResponse<Response> 로그인_응답 = 로그인_요청(email, wrongPassword);
            });
    }

    @DisplayName("유효하지 않은 토큰으로 로그인 시도 (Bearer Auth 유효하지 않은 토큰)")
    @Test
    void myInfoWithWrongBearerAuth() {
        assertThatExceptionOfType(AuthorizationException.class)
            .isThrownBy(() -> {
                // given
                ExtractableResponse<Response> 회원_등록_응답 = 회원_등록되어_있음(email, password, age);

                // when
            });
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        String token = response.jsonPath().getObject("accessToken", String.class);
        assertThat(token).isNotNull();
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest token = new TokenRequest(email, password);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(token)
            .when().post("login/token")
            .then().log().all().extract();
    }

}
