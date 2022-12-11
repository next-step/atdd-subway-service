package nextstep.subway.utils;

import static nextstep.subway.utils.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

public class MemberAcceptanceUtils {
	private static final String MEMBER_API_URL = "/members";
	private static final String MY_INFO_API_URL = MEMBER_API_URL + "/me";

	public static void 회원_등록되어_있음(String email, String password, int age) {
		회원_생성을_요청(email, password, age);
	}

	public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
		MemberRequest memberRequest = new MemberRequest(email, password, age);
		return post(MEMBER_API_URL, memberRequest).extract();
	}

	public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return get(uri).extract();
	}

	public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email,
		String password, Integer age) {
		String uri = response.header("Location");
		MemberRequest memberRequest = new MemberRequest(email, password, age);

		return put(uri, memberRequest).extract();
	}

	public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return delete(uri).extract();
	}

	public static void 회원_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
		MemberResponse memberResponse = response.as(MemberResponse.class);

		assertAll(
			() -> assertThat(memberResponse.getId()).isNotNull(),
			() -> assertThat(memberResponse.getEmail()).isEqualTo(email),
			() -> assertThat(memberResponse.getAge()).isEqualTo(age)
		);
	}

	public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 회원_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
		return get(MY_INFO_API_URL, accessToken).extract();
	}

	public static ExtractableResponse<Response> 내_정보_수정_요청(String accessToken, String email, String password,
		Integer age) {
		MemberRequest memberRequest = new MemberRequest(email, password, age);
		return put(MY_INFO_API_URL, memberRequest, accessToken).extract();
	}

	public static ExtractableResponse<Response> 내_정보_삭제_요청(String accessToken) {
		return delete(MY_INFO_API_URL, accessToken).extract();
	}

	public static void 내_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
		회원_정보_조회됨(response, email, age);
	}

	public static void 내_정보_수정됨(ExtractableResponse<Response> response, String accessToken, String newEmail,
		int newAge) {
		ExtractableResponse<Response> updateResponse = 내_정보_조회_요청(accessToken);

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(updateResponse.as(MemberResponse.class).getEmail()).isEqualTo(newEmail),
			() -> assertThat(updateResponse.as(MemberResponse.class).getAge()).isEqualTo(newAge)
		);
	}

	public static void 내_정보_삭제됨(ExtractableResponse<Response> response) {
		회원_삭제됨(response);
	}

}
