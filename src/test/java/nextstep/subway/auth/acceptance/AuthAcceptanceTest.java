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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입 한 상태의 로그인 성공 검증")
    @Test
    void myInfoWithBearerAuth() {
        MemberAcceptanceTest.회원_생성을_요청("email@email.com", "password", 20);
        ExtractableResponse<Response> response = 로그인_요청("email@email.com", "password");

        로그인_됨(response);
    }

    @DisplayName("회원정보가 없음 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청("email@email.com", "password");

        로그인_실패(response);
    }

     public static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest param = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(accessToken).isNull();
    }


}
