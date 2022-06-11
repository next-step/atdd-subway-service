package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_얻기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;
    public static final String MEMBER_ME_URI = "/members/me";

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
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        //given
        String token = 로그인_토큰_얻기(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> getResponse = 내_정보_조회_요청(token);
        //then
        내_정보_조회됨(getResponse, EMAIL, AGE);

        //given
        String updateEmail = "updateEmail";
        String updatePassword = "updatePassword";
        int updateAge = 32;
        MemberRequest updateRequest = new MemberRequest(updateEmail, updatePassword, updateAge);
        //when
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(token, updateRequest);
        //then
        내_정보_수정됨(updateResponse);

        //when
        ExtractableResponse<Response> response = 내_정보_삭제_요청(token);
        //then
        내_정보_삭제됨(response);
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

    public static ExtractableResponse<Response> 내_정보_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get(MEMBER_ME_URI)
                .then().log().all()
                .extract();
    }

    private void 내_정보_조회됨(ExtractableResponse<Response> getResponse, String expectedEmail, int expectedAge) {
        MemberResponse memberResponse = getResponse.as(MemberResponse.class);
        assertAll(
                () -> assertEquals(expectedEmail, memberResponse.getEmail()),
                () -> assertEquals(expectedAge, memberResponse.getAge())
        );
    }

    private ExtractableResponse<Response> 내_정보_수정_요청(String token, MemberRequest request) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(MEMBER_ME_URI)
                .then().log().all()
                .extract();
    }

    private void 내_정보_수정됨(ExtractableResponse<Response> response) {
        assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

    private ExtractableResponse<Response> 내_정보_삭제_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete(MEMBER_ME_URI)
                .then().log().all()
                .extract();
    }

    private void 내_정보_삭제됨(ExtractableResponse<Response> response) {
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }
}
