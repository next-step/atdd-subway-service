package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final String NEW_EMAIL = "newemail@email.com";
	public static final String NEW_PASSWORD = "newpassword";
	public static final int AGE = 20;
	public static final int NEW_AGE = 21;

	private static ExtractableResponse<Response> createResponse;
	private static TokenResponse tokenResponse;

	@BeforeEach
	public void setUp() {
		super.setUp();
		createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		tokenResponse = 로그인_되어있음(EMAIL, PASSWORD);
	}

	@DisplayName("회원 정보를 관리한다.")
	@Test
	void manageMember() {
		// then
		회원_생성됨(createResponse);

		// when
		ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
		// then
		회원_정보_조회됨(findResponse, EMAIL, AGE);

		// when
		ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
		// then
		회원_정보_수정됨(updateResponse);

		// when
		ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
		// then
		회원_삭제됨(deleteResponse);
	}

	@DisplayName("나의 정보를 관리한다.(성공)")
	@Test
	void manageMyInfoSuccess() {
		//when 토큰으로 내 정보 조회 요청
		ExtractableResponse<Response> findResponse = 내_정보_조회_요청(tokenResponse);

		//then 조회 성공
		내_정보_조회_성공(findResponse);

		//when 토큰으로 내 정보 수정 요청
		MemberRequest memberRequest = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
		ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(tokenResponse, memberRequest);

		//then 수정 성공
		내_정보_수정_성공(updateResponse);

		//when 토큰으로 내 정보 수정 요청
		ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(tokenResponse);

		//then 삭제 성공
		내_정보_삭제_성공(deleteResponse);
	}

	@DisplayName("나의 정보를 조회한다.(실패)")
	@Test
	void findMyInfoFail() {
		//given
		회원_삭제_요청(createResponse);

		//when 토큰으로 내 정보 수정 요청
		ExtractableResponse<Response> findResponse = 내_정보_조회_요청(tokenResponse);

		//then 조회 실패
		내_정보_인증_실패(findResponse);
	}

	@DisplayName("나의 정보를 수정한다.(실패)")
	@Test
	void updateMyInfoFail() {
		//given
		회원_삭제_요청(createResponse);

		//when 토큰으로 내 정보 수정 요청
		MemberRequest memberRequest = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
		ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(tokenResponse, memberRequest);

		//then 수정 실패
		내_정보_인증_실패(updateResponse);
	}

	@DisplayName("나의 정보를 삭제한다.(실패)")
	@Test
	void deleteMyInfoFail() {
		//given
		회원_삭제_요청(createResponse);

		//when 토큰으로 내 정보 수정 요청
		ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(tokenResponse);

		//then 조회 실패
		내_정보_인증_실패(deleteResponse);
	}

	private void 내_정보_인증_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	private void 내_정보_삭제_성공(ExtractableResponse<Response> deleteResponse) {
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> 내_정보_삭제_요청(TokenResponse tokenResponse) {
		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.when().delete("/members/me")
			.then().log().all()
			.extract();
	}

	private void 내_정보_수정_성공(ExtractableResponse<Response> updateResponse) {
		assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 내_정보_수정_요청(TokenResponse tokenResponse, MemberRequest memberRequest) {
		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(memberRequest)
			.when().put("/members/me")
			.then().log().all()
			.extract();
	}

	private void 내_정보_조회_성공(ExtractableResponse<Response> response) {
		MemberResponse memberResponse = response.as(MemberResponse.class);
		assertThat(memberResponse).isNotNull();
		assertThat(memberResponse.getId()).isEqualTo(1L);
		assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
		assertThat(memberResponse.getAge()).isEqualTo(AGE);
	}

	private ExtractableResponse<Response> 내_정보_조회_요청(TokenResponse tokenResponse) {
		return RestAssured.given().log().all()
			.auth().oauth2(tokenResponse.getAccessToken())
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
	}

	private TokenResponse 로그인_되어있음(String email, String password) {
		return AuthAcceptanceTest.로그인_요청(new TokenRequest(email, password))
			.as(TokenResponse.class);
	}

	public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
		MemberRequest memberRequest = new MemberRequest(email, password, age);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(memberRequest)
			.when().post("/members")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");

		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email,
		String password, Integer age) {
		String uri = response.header("Location");
		MemberRequest memberRequest = new MemberRequest(email, password, age);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(memberRequest)
			.when().put(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return RestAssured
			.given().log().all()
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	public static void 회원_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
		MemberResponse memberResponse = response.as(MemberResponse.class);
		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(email);
		assertThat(memberResponse.getAge()).isEqualTo(age);
	}

	public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 회원_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
