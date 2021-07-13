package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthTestMethod.*;
import static nextstep.subway.member.MemberTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;

public class AuthAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
		// Scenario : 로그인을 시도 시나리오
		// Given
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When
		ExtractableResponse<Response> tokenResponse = login(EMAIL, PASSWORD);
		// Then
		assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
		// Scenario : 로그인 실패 시나리오
		// Given : 회원 등록되어있지 않음
		// When
		ExtractableResponse<Response> tokenResponse1 = login(EMAIL, PASSWORD);
		// Then
		assertThat(tokenResponse1.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		// Given
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When
		ExtractableResponse<Response> tokenResponse2 = login("abc@email.com", PASSWORD);
		// Then
		assertThat(tokenResponse2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		// When
		ExtractableResponse<Response> tokenResponse3 = login(EMAIL, "12345");
		// Then
		assertThat(tokenResponse3.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
		// Scenario : 유효하지 않은 토큰으로 /members/me 요청할 경우 에러 시나리오
		// Given
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When
		ExtractableResponse<Response> response = login(EMAIL, PASSWORD);
		// Then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		// Given
		TokenResponse tokenResponse = response.as(TokenResponse.class);
		String token = tokenResponse.getAccessToken();
		String wrongToken = "abc";
		// When
		ExtractableResponse<Response> errorResponse = requestWithToken(wrongToken);
		// Then
		assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
