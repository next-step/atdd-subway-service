package nextstep.subway.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

public class MemberAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final String NEW_EMAIL = "newemail@email.com";
	public static final String NEW_PASSWORD = "newpassword";
	public static final int AGE = 20;
	public static final int NEW_AGE = 21;

	@DisplayName("회원 정보를 관리한다.")
	@Test
	void manageMember() {
		// when
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
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

	@DisplayName("나의 정보를 관리한다.")
	@Test
	void manageMyInfo() {
		ExtractableResponse<Response> memberCreateResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		회원_생성됨(memberCreateResponse);

		ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_시도(EMAIL, PASSWORD);

		내_정보_조회_테스트(loginResponse);
		ExtractableResponse<Response> loginAfterUpdateResponse = 내_정보_수정_테스트(loginResponse, "newEmail@email.com",
			PASSWORD, AGE);
		내_정보_삭제_테스트(loginAfterUpdateResponse);
	}

	private void 내_정보_조회_테스트(ExtractableResponse<Response> loginResponse) {
		ExtractableResponse<Response> readMyInfoResponse = 내_정보_조회_요청함(loginResponse);
		내_정보_조회_요청_성공함(readMyInfoResponse);
	}

	private static ExtractableResponse<Response> 내_정보_수정_테스트(ExtractableResponse<Response> loginResponse,
		String newEmail, String newPassword,
		Integer newAge) {
		MemberRequest memberRequest = new MemberRequest(newEmail, newPassword, newAge);
		ExtractableResponse<Response> updateMyInfoResponse = 내_정보_수정_요청함(memberRequest, loginResponse);
		내_정보_수정_요청_성공함(updateMyInfoResponse);

		ExtractableResponse<Response> loginAfterUpdateResponse = AuthAcceptanceTest.로그인_시도(newEmail, newPassword);
		ExtractableResponse<Response> readAfterUpdateResponse = 내_정보_조회_요청함(loginAfterUpdateResponse);
		내_정보_수정_올바른지_확인(readAfterUpdateResponse, loginAfterUpdateResponse, newEmail, newPassword, newAge);
		return loginAfterUpdateResponse;
	}

	private static void 내_정보_수정_올바른지_확인(ExtractableResponse<Response> readAfterUpdateResponse,
		ExtractableResponse<Response> loginAfterUpdateResponse, String email, String password, Integer age) {
		assertThat(readAfterUpdateResponse.body().jsonPath().getString("email")).isEqualTo(email);
		assertThat(readAfterUpdateResponse.body().jsonPath().getInt("age")).isEqualTo(age);
		assertThat(loginAfterUpdateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 내_정보_삭제_테스트(ExtractableResponse<Response> loginResponse) {
		ExtractableResponse<Response> updateMyInfoResponse = 내_정보_삭제_요청함(loginResponse);
		내_정보_삭제_요청_성공함(updateMyInfoResponse);
		내_정보_삭제_올바른지_확인(loginResponse);
	}

	private void 내_정보_삭제_올바른지_확인(ExtractableResponse<Response> loginResponse) {
		ExtractableResponse<Response> response = 내_정보_조회_요청함(loginResponse);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	public static ExtractableResponse<Response> 회원_생성되어_있음(String email, String password, Integer age) {
		return 회원_생성을_요청(email, password, age);
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

	public static ExtractableResponse<Response> 내_정보_조회_요청함(ExtractableResponse<Response> accessTokenResponse) {
		return 내_정보_조회_요청함(accessTokenResponse.jsonPath().getString("accessToken"));
	}

	public static ExtractableResponse<Response> 내_정보_조회_요청함(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.when().log().all()
			.get("/members/me")
			.then().log().all()
			.extract();
	}

	private void 내_정보_조회_요청_성공함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private static ExtractableResponse<Response> 내_정보_수정_요청함(MemberRequest memberRequest,
		ExtractableResponse<Response> loginResponse) {
		return RestAssured.given().log().all()
			.auth().oauth2(loginResponse.jsonPath().getString("accessToken"))
			.when().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(memberRequest)
			.put("/members/me")
			.then().log().all()
			.extract();
	}

	private static void 내_정보_수정_요청_성공함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 내_정보_삭제_요청함(ExtractableResponse<Response> loginResponse) {
		return RestAssured.given().log().all()
			.auth().oauth2(loginResponse.jsonPath().getString("accessToken"))
			.when().log().all()
			.delete("/members/me")
			.then().log().all()
			.extract();
	}

	private void 내_정보_삭제_요청_성공함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
