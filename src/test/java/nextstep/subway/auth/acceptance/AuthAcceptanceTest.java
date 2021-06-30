package nextstep.subway.auth.acceptance;

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

import static nextstep.subway.member.MemberAcceptanceTest.내_정보_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private String email;
    private String password;

    @BeforeEach
    void setup() {
        email = "yjs2952@gmail.com";
        password = "1234";
        MemberAcceptanceTest.회원_생성을_요청(email, password, 20);
    }

    @DisplayName("Bearer Auth 로그인을 한다")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(email, "4321");

        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse 토큰 = 로그인_요청(email, password).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청(토큰.getAccessToken() + "!23123123");

        // then
        내_정보_조회_실패됨(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
                .given().log().all()
                .body(new TokenRequest(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 내_정보_조회_실패됨(ExtractableResponse<Response> response) {
        로그인_실패됨(response);
    }

}
