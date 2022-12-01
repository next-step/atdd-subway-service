package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {


    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청("chunhodong@github.com", "test1023", 123);
    }

    @DisplayName("계정정보와 저장된 회원정보가 일치하면 로그인 성공")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest("chunhodong@github.com", "test1023"));

        로그인_성공(response);
    }

    @DisplayName("등록되지 않는 회원정보를 로그인하면 로그인 실패")
    @Test
    void myInfoWithNotMatchEmail() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest("awefwa", "test1023"));

        로그인_실패(response);
    }

    @DisplayName("잘못된 비밀번호를 가지고 로그인하면 로그인 실패")
    @Test
    void myInfoWithNotMatchPassword() {
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest("chunhodong@github.com", "1123"));

        로그인_실패(response);
    }

    @DisplayName("유효하지 않은 토큰으로 유저인증을하면 실패")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = 회원_정보_조회_요청("invalid_token");

        회원_정보_인증_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
