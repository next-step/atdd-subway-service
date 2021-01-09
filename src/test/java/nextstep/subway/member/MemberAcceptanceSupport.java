package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MemberAcceptanceSupport {
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

	public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
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

	public static String 회원_로그인_요청(String email, String password) {
		ExtractableResponse<Response> response = RestAssured
				.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new TokenRequest(email, password))
				.when().post("/login/token")
				.then().log().all().
						extract();

		return response.body().jsonPath().get("accessToken");
	}


	public static ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
		return RestAssured
				.given().log().all().auth().oauth2(accessToken)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when().get("/members/me")
				.then().log().all()
				.extract();
	}

	public static void 내_정보_조회_성공함(ExtractableResponse<Response> 내_정보_조회_결과, String email, int age) {
		MemberResponse memberResponse = 내_정보_조회_결과.as(MemberResponse.class);
		assertThat(memberResponse.getEmail()).isEqualTo(email);
		assertThat(memberResponse.getAge()).isEqualTo(age);
	}

	public static ExtractableResponse<Response> 내_정보_업데이트(String accessToken, String newEmail, String newPassword, int newAge) {
		MemberRequest memberRequest = new MemberRequest(newEmail, newPassword, newAge);
		return RestAssured
				.given().log().all().auth().oauth2(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(memberRequest)
				.when().put("/members/me")
				.then().log().all()
				.extract();
	}

	public static void 내_정보_업데이트_성공함(ExtractableResponse<Response> 내_정보_업데이트_결과) {
		assertThat(내_정보_업데이트_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static ExtractableResponse<Response> 내_정보_삭제(String accessToken) {
		return RestAssured
				.given().log().all().auth().oauth2(accessToken)
				.when().delete("/members/me")
				.then().log().all()
				.extract();
	}

	public static void 냬_정보_삭제_성공함(ExtractableResponse<Response> 내_정보_삭제_결과) {
		assertThat(내_정보_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static void 내_정보_조회_실패함(ExtractableResponse<Response> 내_정보_조회_요청) {
		assertThat(내_정보_조회_요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
