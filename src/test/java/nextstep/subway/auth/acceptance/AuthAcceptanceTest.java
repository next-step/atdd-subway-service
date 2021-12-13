package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.RestAssuredApi;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    private String email = "abc@mia.com";
    private String password = "pa**@@rd";
    private int age = 10;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원_등록됨(회원_생성_요청(email, password, age));
        로그인_성공(로그인_요청(email, password));
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        로그인_실패(로그인_요청(email, password));

        회원_등록됨(회원_생성_요청(email, password, age));
        로그인_실패(로그인_요청(email, "invalidPassword"));
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        회원_등록됨(회원_생성_요청(email, password, age));
        로그인_성공(로그인_요청(email, password));

        TokenResponse token = new TokenResponse("invalidToken");
        정보_조회_실패됨(내_정보_조회_요청(token));
    }

    private void 정보_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);
        return RestAssuredApi.post("/login/token", request);
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class)).isNotNull();
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
