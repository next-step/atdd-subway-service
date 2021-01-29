package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_생성을_요청("justhis@gmail.com", "1234", 30);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        TokenRequest tokenRequest = new TokenRequest("justhis@gmail.com", "1234");

        // when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        // then
        응답_코드_확인(response, HttpStatus.OK);
        토큰_확인(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        TokenRequest tokenRequest = new TokenRequest("justhis@gmail.com", "123456");

        // when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        // then
        응답_코드_확인(response, HttpStatus.UNAUTHORIZED);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {

        // given when
        ExtractableResponse<Response> response = MemberAcceptanceTest.본인_정보_요청("expiredToken");

        // then
        응답_코드_확인(response, HttpStatus.UNAUTHORIZED);
    }

    private void 토큰_확인(ExtractableResponse<Response> response) {
        assertThat(response.body().jsonPath().getString("accessToken")).isNotNull();
    }

    private void 응답_코드_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/login/token")
                .then().log().all().extract();
        return response;
    }

}
