package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.utils.RestAssuredCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private String email = MemberAcceptanceTest.EMAIL;
    private String password = MemberAcceptanceTest.PASSWORD;
    private int age = AGE;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(email, password, age);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // Given
        ExtractableResponse<Response> tokenResponse = 로그인_요청(new TokenRequest(email, password));
        String token = tokenResponse.as(TokenResponse.class).getAccessToken();

        // When
        ExtractableResponse<Response> meResponse = 내_정보_조회(token);

        // Then
        요청_성공(meResponse);

        // And
        회원_정보_조회됨(meResponse, email, age);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // When
        ExtractableResponse<Response> meResponse = 내_정보_조회("이상한토큰");

        // Then
        요청_UNAUTHORIZED_실패(meResponse);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // When 유효하지 않은 토큰을 준비한다.
        String secretKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
        long validityInMilliseconds = 0;
        JwtTokenProvider tokenProvider = new JwtTokenProvider();
        String token = tokenProvider.createToken(email, secretKey, validityInMilliseconds);

        // Then 유효하지 않은 토큰으로 판별된다.
        assertThat(tokenProvider.validateToken(token)).isFalse();

        // When
        ExtractableResponse<Response> meResponse = 내_정보_조회(token);

        // Then
        요청_UNAUTHORIZED_실패(meResponse);
    }

    @Test
    void 로그인_시도_성공과_실패() {
        // When 정상 로그인
        ExtractableResponse<Response> responseTrue = 로그인_요청(new TokenRequest(email, password));
        // Then
        요청_성공(responseTrue);

        // When 틀린 비밀번호 로그인
        ExtractableResponse<Response> responseFalse = 로그인_요청(new TokenRequest(email, "false"));
        // Then
        요청_UNAUTHORIZED_실패(responseFalse);
    }

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssuredCRUD.postRequest("/login/token", tokenRequest);
    }

    private void 요청_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 요청_UNAUTHORIZED_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
    
}
