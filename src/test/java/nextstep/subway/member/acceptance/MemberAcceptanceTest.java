package nextstep.subway.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인되어있음;
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
        //given
        회원_생성_되어있음(EMAIL, PASSWORD, AGE);
        TokenResponse 토큰 = 로그인되어있음(EMAIL, PASSWORD);
        
        //when
        ExtractableResponse<Response> myInfoResponse = 나의정보_조회_요청(토큰);

        // then
        나의정보_조회됨(myInfoResponse);

        // when
        ExtractableResponse<Response> updateResponse = 나의정보_수정_요청(토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        나의정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 나의정보_삭제_요청(토큰);

        // then
        나의정보_삭제됨(deleteResponse);
    }

    private void 나의정보_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 나의정보_삭제_요청(TokenResponse 토큰) {
        ExtractableResponse<Response> deleteResponse =
                given()
                        .log().all()
                        .auth().oauth2(토큰.getAccessToken())
                .when()
                        .delete("/members/me")
                .then()
                        .log().all()
                        .extract();
        return deleteResponse;
    }

    private void 나의정보_수정됨(ExtractableResponse<Response> updateResponse) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 나의정보_수정_요청(TokenResponse 토큰, String newEmail, String newPassword, int newAge) {
        Map<String, String> params = new HashMap<>();
        params.put("email", newEmail);
        params.put("password", newPassword);
        params.put("AGE", String.valueOf(newAge));
        ExtractableResponse<Response> updateResponse =
                given()
                        .log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .auth().oauth2(토큰.getAccessToken())
                .when()
                        .put("/members/me")
                .then()
                        .log().all()
                        .extract();
        return updateResponse;
    }

    public static void 나의정보_조회됨(ExtractableResponse<Response> myInfoResponse) {
        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 나의정보_조회_요청(TokenResponse 토큰) {
        ExtractableResponse<Response> myInfoResponse =
                given()
                        .log().all()
                        .auth().oauth2(토큰.getAccessToken())
                .when()
                        .get("/members/me")
                .then()
                        .log().all()
                        .extract();
        return myInfoResponse;
    }

    public static ExtractableResponse<Response> 회원_생성_되어있음(String email, String password, Integer age) {
        ExtractableResponse<Response> response = 회원_생성을_요청(email, password, age);
        회원_생성됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return given().log().all()
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
