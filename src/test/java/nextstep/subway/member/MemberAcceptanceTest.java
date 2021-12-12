package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청함;
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
        // given
        Long id = 내_정보_등록되어_있음(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청함(EMAIL, PASSWORD);
        String 사용자 = 로그인_됨(로그인_응답);

        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청함(사용자);

        // then
        내_정보_조회됨(내_정보_조회_응답, id, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 내_정보_수정_응답 = 내_정보_수정_요청함(사용자, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        내_정보_수정됨(내_정보_수정_응답);

        // given
        ExtractableResponse<Response> 수정_후_로그인_응답 = 로그인_요청함(NEW_EMAIL, NEW_PASSWORD);
        String 수정된_사용자 = 로그인_됨(수정_후_로그인_응답);

        // when
        ExtractableResponse<Response> 수정_후_내_정보_조회_응답 = 내_정보_조회_요청함(수정된_사용자);

        // then
        내_정보_조회됨(수정_후_내_정보_조회_응답, id, NEW_EMAIL, NEW_AGE);

        // when
        ExtractableResponse<Response> 내_정보_삭제_응답 = 내_정보_삭제_요청함(수정된_사용자);

        // then
        내_정보_삭제됨(내_정보_삭제_응답);
    }

    private void 내_정보_수정됨(ExtractableResponse<Response> response) {
        요청_성공함(response, HttpStatus.OK);
    }

    private void 내_정보_조회됨(ExtractableResponse<Response> response, Long id, String email, int age) {
        요청_성공함(response, HttpStatus.OK);

        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getId()).isEqualTo(id);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
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
        요청_성공함(response, HttpStatus.CREATED);
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        요청_성공함(response, HttpStatus.OK);
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        요청_성공함(response, HttpStatus.NO_CONTENT);
    }

    public static Long 내_정보_등록되어_있음(String email, String password, int age) {
        ExtractableResponse<Response> response = 회원_생성을_요청(email, password, age);
        MemberResponse memberResponse = response.as(MemberResponse.class);
        return memberResponse.getId();
    }

    public static String createBearerToken(String accessToken) {
        return "bearer " + accessToken;
    }

    private ExtractableResponse<Response> 내_정보_조회_요청함(String accessToken) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
        return response;
    }

    private ExtractableResponse<Response> 내_정보_수정_요청함(String accessToken, String newEmail, String newPassword, int newAge) {
        Map<String, Object> param = new HashMap<>();
        param.put("email", newEmail);
        param.put("password", newPassword);
        param.put("age", newAge);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(accessToken))
                .body(param)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    private void 내_정보_삭제됨(ExtractableResponse<Response> response) {
        요청_성공함(response, HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> 내_정보_삭제_요청함(String accessToken) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, createBearerToken(accessToken))
                .when().delete("/members/me")
                .then().log().all()
                .extract();
        return response;
    }

    private static void 요청_성공함(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
