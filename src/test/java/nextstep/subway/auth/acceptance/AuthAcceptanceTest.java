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

    @DisplayName("올바른 로그인 기능 사용 (해피패스)")
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

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
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
}
