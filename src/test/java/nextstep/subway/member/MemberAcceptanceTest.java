package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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

    /**
     * Given: 회원 등록되어 있음
     * And: 토큰 발급(로그인)되어 있음
     * When: 내 정보 조회 요청
     * Then: 내 정보 조회 성공
     * When: 내 정보 수정 요청
     * Then: 내 정보 수정 성공
     * When: 변경된 이메일, 패스워드로 토큰 재발급
     * Then: 토큰 재발급 성공
     * When: 내 정보 조회 요청
     * Then: 내 정보 조회 성공
     * When: 내 정보 삭제 요청
     * Then: 내 정보 삭제 성공
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // given
        String 토큰값 = 토큰값_추출(토큰_발급_요청(EMAIL, PASSWORD));
        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회_요청(토큰값);
        // then
        내_정보_조회_성공(내_정보_조회_응답, EMAIL, AGE);
        // when
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(토큰값, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        내_정보_수정_성공(updateResponse);
        // when
        ExtractableResponse<Response> 토큰_재발급_요청 = 토큰_발급_요청(NEW_EMAIL, NEW_PASSWORD);
        // then
        토큰_발급_성공(토큰_재발급_요청);
        String 재발급_토큰값 = 토큰값_추출(토큰_재발급_요청);
        // when
        ExtractableResponse<Response> 변경된_내_정보_조회_응답 = 내_정보_조회_요청(재발급_토큰값);
        // then
        내_정보_조회_성공(변경된_내_정보_조회_응답, NEW_EMAIL, NEW_AGE);
        // when
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(재발급_토큰값);
        // then
        내_정보_삭제_성공(deleteResponse);
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

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String expectedEmail, int expectedAge) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(expectedEmail);
        assertThat(memberResponse.getAge()).isEqualTo(expectedAge);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    public static void 내_정보_조회_성공(ExtractableResponse<Response> response, String expectedEmail, int expectedAge) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("email")).isEqualTo(expectedEmail);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(expectedAge);
    }

    public static void 내_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(String accessToken, String email, String password,
        Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age.toString());

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/members/me")
            .then().log().all()
            .extract();
    }

    public static void 내_정보_수정_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 내_정보_삭제_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/members/me")
            .then().log().all()
            .extract();
    }

    public static void 내_정보_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
