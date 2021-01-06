package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("토큰 발급 성공")
    @Test
    void myInfoWithBearerAuth() {
        // given
        String email = "yohan@email.com";
        String password = "password";
        int age = 29;
        회원_등록되어_있음(email, password, age);

        TokenRequest tokenRequest = new TokenRequest(email, password);

        // when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        // then
        로그인_성공됨(response);
    }

    @DisplayName("토근 발급 실패: 아이디 또는 비밀번호가 틀린 경우")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String email = "yohan@email.com";
        String wrongEamil = "wrong@email.com";
        String password = "password";
        String wrongPassword = "wrong";
        int age = 29;
        회원_등록되어_있음(email, password, age);

        TokenRequest wrongEmailRequest = new TokenRequest(wrongEamil, password);
        TokenRequest wrongPasswordRequest = new TokenRequest(email, wrongPassword);

        // when
        ExtractableResponse<Response> response1 = 로그인_요청(wrongEmailRequest);
        ExtractableResponse<Response> response2 = 로그인_요청(wrongPasswordRequest);

        // then
        로그인_실패됨(response1);
        로그인_실패됨(response2);
    }

    private void 회원_등록되어_있음(final String email, final String password, final Integer age) {
        ExtractableResponse<Response> response = 회원_생성을_요청(email, password, age);
        회원_생성됨(response);
    }

    private ExtractableResponse<Response> 로그인_요청(final TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/login/token")
                .then()
                .log().all()
                .extract();
    }

    private void 로그인_성공됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String accessToken = response.jsonPath().get("accessToken");
        assertThat(accessToken).isNotNull();
    }

    private void 로그인_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
