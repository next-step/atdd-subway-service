package nextstep.subway.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

public class MemberAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final String NEW_EMAIL = "newemail@email.com";
	public static final String NEW_PASSWORD = "newpassword";
	public static final int AGE = 20;
	public static final int NEW_AGE = 21;
	AuthAcceptanceTest authAcceptanceTest;
	TokenResponse 토큰;

	@BeforeEach
	void memberSetUp() {
		authAcceptanceTest = new AuthAcceptanceTest();
		회원_생성을_요청("taminging@kakao.com", "taminging", 20);
		토큰 = authAcceptanceTest.로그인("taminging@kakao.com", "taminging").as(TokenResponse.class);
	}

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
		// 조회
		ExtractableResponse<Response> selectResponse = 로그인된_회원_정보_조회_요청();
		로그인된_회원_정보_조회_확인(selectResponse);

		// 수정
		ExtractableResponse<Response> updateResponse = 로그인된_회원_정보_수정_요청("taminging2@kakao.com", "password2", 21);
		로그인된_회원_정보_수정_확인(updateResponse);

		// 삭제
		ExtractableResponse deleteResponse = 로그인된_회원_정보_삭제_요청();
		로그인된_회원_정보_삭제_확인(deleteResponse);

	}

	@DisplayName("로그인된 회원 정보 조회하기")
	@Test
	void 로그인된_회원_정보_조회하기() {
		ExtractableResponse<Response> selectResponse = 로그인된_회원_정보_조회_요청();
		로그인된_회원_정보_조회_확인(selectResponse);
	}

	@DisplayName("로그인된 회원 정보 수정하기")
	@Test
	void 로그인된_회원_정보_수정하기() {
		ExtractableResponse<Response> updateResponse = 로그인된_회원_정보_수정_요청("taminging2@kakao.com", "password2", 21);
		로그인된_회원_정보_수정_확인(updateResponse);
	}

	@DisplayName("로그인된 회원 정보 삭제하기")
	@Test
	void 로그인된_회원_정보_삭제하기() {
		ExtractableResponse deleteResponse = 로그인된_회원_정보_삭제_요청();
		로그인된_회원_정보_삭제_확인(deleteResponse);
	}

	ExtractableResponse<Response> 로그인된_회원_정보_조회_요청() {
		return get("/members/me", 토큰.getAccessToken());
	}

	void 로그인된_회원_정보_조회_확인(ExtractableResponse<Response> response) {
		MemberResponse memberResponse = response.as(MemberResponse.class);
		assertThat(memberResponse.getEmail()).isEqualTo("taminging@kakao.com");
		assertThat(memberResponse.getAge()).isEqualTo(20);
	}

	ExtractableResponse<Response> 로그인된_회원_정보_수정_요청(String email, String password, int age) {
		MemberRequest memberRequest = new MemberRequest(email, password, age);
		return put(memberRequest, "/members/me", 토큰.getAccessToken());
	}

	void 로그인된_회원_정보_수정_확인(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	ExtractableResponse<Response> 로그인된_회원_정보_삭제_요청() {
		return delete("/members/me", 토큰.getAccessToken());
	}

	void 로그인된_회원_정보_삭제_확인(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
		MemberRequest memberRequest = new MemberRequest(email, password, age);

		return post(memberRequest, "members");
	}

	public ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return get(uri);
	}

	public ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email,
		String password, Integer age) {
		String uri = response.header("Location");
		MemberRequest memberRequest = new MemberRequest(email, password, age);

		return put(memberRequest, uri);
	}

	public ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return delete(uri);
	}

	public void 회원_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
		MemberResponse memberResponse = response.as(MemberResponse.class);
		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(email);
		assertThat(memberResponse.getAge()).isEqualTo(age);
	}

	public void 회원_정보_수정됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public void 회원_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

}
