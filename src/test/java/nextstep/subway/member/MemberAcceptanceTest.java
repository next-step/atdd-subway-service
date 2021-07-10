package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
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
		// Scenario : 나의 정보 관리 시나리오
		// Given : 회원 등록되어 있음
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// And : token 정보 가지고 있음
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		ExtractableResponse<Response> tokenResponseCandidate1 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		TokenResponse tokenResponse = tokenResponseCandidate1.as(TokenResponse.class);
		String token = tokenResponse.getAccessToken();
		// When : 나의 정보 조회 요청
		ExtractableResponse<Response> MyInfoResponse1 = RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
		// Then : 나의 정보 조회
		assertThat(MyInfoResponse1.statusCode()).isEqualTo(HttpStatus.OK.value());
		// Given : token 정보 가지고 있음
		token = tokenResponse.getAccessToken();
		// When : 나의 정보 업데이트 요청
		ExtractableResponse<Response> MyInfoResponse2 = RestAssured
			.given().log().all()
			.auth().oauth2(token)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE))
			.when().put("/members/me")
			.then().log().all()
			.extract();
		// Then : 업데이트 된 정보 조회
		TokenRequest tokenRequest2 = new TokenRequest(NEW_EMAIL, NEW_PASSWORD);
		ExtractableResponse<Response> tokenResponseCandidate2 = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest2)
			.when().post("/login/token")
			.then().log().all()
			.extract();
		TokenResponse tokenResponse2 = tokenResponseCandidate2.as(TokenResponse.class);
		String newToken = tokenResponse2.getAccessToken();

		assertThat(MyInfoResponse2.statusCode()).isEqualTo(HttpStatus.OK.value());
		ExtractableResponse<Response> MyInfoResponse3 = RestAssured
			.given().log().all()
			.auth().oauth2(newToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
		assertThat(MyInfoResponse3.statusCode()).isEqualTo(HttpStatus.OK.value());
		// Given : token 존재함
		newToken = tokenResponse2.getAccessToken();
		// When : 나의 정보 삭제 요청
		ExtractableResponse<Response> MyInfoResponse4 = RestAssured
			.given().log().all()
			.auth().oauth2(newToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE))
			.when().delete("/members/me")
			.then().log().all()
			.extract();
		// Then : 나의 정보 삭제 확인
		assertThat(MyInfoResponse4.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
}
