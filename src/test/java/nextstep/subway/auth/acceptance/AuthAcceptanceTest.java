package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

	@BeforeEach
	public void setUp() {
		회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		//given 회원 등록되어있음
		//when 로그인 요청
		ExtractableResponse<Response> response = 로그인_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
		TokenResponse tokenResponse = response.as(TokenResponse.class);
		//then 로그인됨
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(tokenResponse.getAccessToken()).isNotNull();
		assertThat(tokenResponse.getAccessToken()).isNotEmpty();
	}

	@DisplayName("Bearer Auth 로그인 실패")
	@Test
	void myInfoWithBadBearerAuth() {
		//given 회원 등록되어있음
		//when 잘못된 정보로 로그인 요청
		ExtractableResponse<Response> response = 로그인_요청(MemberAcceptanceTest.EMAIL, "abcd");
		//then 401 에러
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

	}

	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		//given 회원 등록되어있음
		//given 로그인됨
		ExtractableResponse<Response> loginResponse = 로그인_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);

		//when 유효하지 않은  토큰으로 /members/me 요청
		TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
		String wrongToken = tokenResponse.getAccessToken() + "test";
		ExtractableResponse<Response> response = 내_정보_조회_요청(wrongToken);

		//then 에러
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

	}

}
