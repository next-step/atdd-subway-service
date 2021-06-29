package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;

@DisplayName("로그인, 인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private String 이메일;
    private String 비밀번호;
    private Integer 나이;

    @BeforeEach
    void signUpSetUp() {
        이메일 = "ehdgml3206@gmail.com";
        비밀번호 = "1234";
        나이 = 31;
        회원_등록되어_있음(이메일, 비밀번호, 나이);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(이메일, 비밀번호);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(이메일, "잘못된 비밀번호");

        // then
        권한없음_확인됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String token = "유효하지 않은 토큰";

        // when
        ExtractableResponse<Response> response = 개인_정보_조회_요청(token);

        // then
        권한없음_확인됨(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());
        String token = response.as(TokenResponse.class).getAccessToken();
        assertThat(token).isNotNull();
    }

    private void 권한없음_확인됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
