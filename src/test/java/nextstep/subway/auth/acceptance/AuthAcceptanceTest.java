package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberTestMethod;

public class AuthAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
		// Scenario : 로그인을 시도 시나리오
		// Given : 회원 등록되어 있음
		ExtractableResponse<Response> createResponse = MemberTestMethod.회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When : 로그인 요청
		ExtractableResponse<Response> tokenResponse = AuthTestMethod.login(EMAIL, PASSWORD);
		// Then : 로그인 됨
		assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
		// Scenario : 로그인 실패 시나리오
		// Given : 회원 등록되어있지 않음
		// When : 로그인 요청
		ExtractableResponse<Response> tokenResponse1 = AuthTestMethod.login(EMAIL, PASSWORD);
		// Then : 로그인 실패
		assertThat(tokenResponse1.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		// Given : 회원가입
		ExtractableResponse<Response> createResponse = MemberTestMethod.회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When : 로그인 요청(ID 틀림)
		ExtractableResponse<Response> tokenResponse2 = AuthTestMethod.login("abc@email.com", PASSWORD);
		// Then : 로그인 실패
		assertThat(tokenResponse2.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		// When : 회원 가입 후 로그인 요청(비밀번호 틀림)
		ExtractableResponse<Response> tokenResponse3 = AuthTestMethod.login(EMAIL, "12345");
		// Then : 로그인 실패
		assertThat(tokenResponse3.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
		// Scenario : 유효하지 않은 토큰으로 /members/me 요청할 경우 에러 시나리오
		// Given : 회원등록 됨
		ExtractableResponse<Response> createResponse = MemberTestMethod.회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// When : 로그인 요청
		ExtractableResponse<Response> response = AuthTestMethod.login(EMAIL, PASSWORD);
		// Then : 로그인 됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		// Given : 토큰 수령
		TokenResponse tokenResponse = response.as(TokenResponse.class);
		String token = tokenResponse.getAccessToken();
		String wrongToken = "abc";
		// When : 유효하지 않은 토큰으로 /members/me 요청
		ExtractableResponse<Response> errorResponse = AuthTestMethod.requestWithToken(wrongToken);
		// Then : 에러 발생
		assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
