package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        ExtractableResponse<Response> response = MemberAcceptanceTest.회원_생성을_요청("eversong0723@gmail.com", "1234", 10);
        MemberAcceptanceTest.회원_생성됨(response);

        // when
        ExtractableResponse<Response> tokenResponse = 토큰_로그인_요청(new TokenRequest("eversong0723@gmail.com", "1234"));

        // then
        토큰_로그인됨(tokenResponse);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        ExtractableResponse<Response> response = MemberAcceptanceTest.회원_생성을_요청("eversong0723@gmail.com", "1234", 10);
        MemberAcceptanceTest.회원_생성됨(response);

        // when
        ExtractableResponse<Response> tokenResponse = 토큰_로그인_요청(new TokenRequest("eversong0723@gmail.com", "12312312312"));

        // then
        토큰_로그인실패됨(tokenResponse);

    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    public static ExtractableResponse<Response> 토큰_로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 토큰_로그인됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static void 토큰_로그인실패됨(ExtractableResponse response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


}
