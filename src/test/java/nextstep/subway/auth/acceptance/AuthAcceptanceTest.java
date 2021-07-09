package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        회원_생성을_요청("sgkim94@github.com", "123456", 28);
        TokenRequest request = new TokenRequest("sgkim94@github.com", "123456");

        // when
        ExtractableResponse<Response> response = 회원_로그인을_요청(request);

        // then
        TokenResponse token = response.body().as(TokenResponse.class);

        로그인_성공됨(response);
        토큰이_포함됨(token);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        회원_생성을_요청("sgkim94@github.com", "123456", 28);
        TokenRequest request = new TokenRequest("sgkim94@github.com", "234567");

        // when
        ExtractableResponse<Response> response = 회원_로그인을_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private void 로그인_성공됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 토큰이_포함됨(TokenResponse token) {
        assertThat(token.getAccessToken()).isNotBlank();
    }

    private ExtractableResponse<Response> 회원_로그인을_요청(TokenRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }
}
