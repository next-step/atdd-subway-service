package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.utils.AuthAcceptanceUtils.*;
import static nextstep.subway.utils.MemberAcceptanceUtils.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {


	@BeforeEach
	public void setUp() {
		super.setUp();
		회원_생성을_요청(EMAIL, PASSWORD, AGE);
	}

	/**
	 * Given 회원 등록되어 있음
	 * When 로그인 요청
	 * Then 로그인 완료
	 */
	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		// when
		ExtractableResponse<Response> 로그인_요청 = 로그인_요청(EMAIL, PASSWORD);

		// then
		로그인_성공(로그인_요청);
	}

	/**
	 * Given 존재하지 않은 회원정보
	 * When 로그인 요청
	 * Then 로그인 실패
	 */
	/**
	 * Given 회원 등록되어 있음
	 * And 비밀번호가 틀린 회원정보
	 * When 로그인 요청
	 * Then 로그인 실패
	 */
	@DisplayName("Bearer Auth 로그인 실패")
	@ParameterizedTest(name = "[{index}] ({0}, {1}) -> {2}")
	@MethodSource
	void myInfoWithBadBearerAuth(
		String email,
		String password,
		String message
	) {
		// when
		ExtractableResponse<Response> 로그인_요청 = 로그인_요청(email, password);

		// then
		로그인_실패(로그인_요청);
	}

	private static Stream<Arguments> myInfoWithBadBearerAuth() {
		return Stream.of(
			Arguments.of("Incorrect email", "Incorrect password", "존재하지 않는 회원의 로그인 요청"),
			Arguments.of(EMAIL, "Incorrect password", "잘못된 비밀번호를 통한 로그인 요청")
		);
	}

	/**
	 * Given 회원 등록 되어 있음
	 * And 유효하지 않은 토큰
	 * When 내 정보 조회 요청
	 * Then 내 정보 조회 실패
	 */
	@DisplayName("Bearer Auth 유효하지 않은 토큰")
	@Test
	void myInfoWithWrongBearerAuth() {
		// when
		ExtractableResponse<Response> 내_정보_조회_요청 = 내_정보_조회_요청("Bearer wrongToken");

		// then
		인증_실패(내_정보_조회_요청);
	}
}
