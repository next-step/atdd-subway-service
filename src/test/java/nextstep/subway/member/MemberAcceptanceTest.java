package nextstep.subway.member;

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

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final String MY_EMAIL = "myemail@email.com";
    public static final String MY_PASSWORD = "mypassword";
    public static final String MY_NEW_EMAIL = "mynewemail@email.com";
    public static final String MY_NEW_PASSWORD = "mynewpassword";

    public static final int AGE = 20;
    public static final int NEW_AGE = 21;
    public static final int MY_AGE = 22;
    public static final int MY_NEW_AGE = 23;

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
        // 생성
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(MY_EMAIL, MY_PASSWORD, MY_AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(MY_EMAIL, MY_PASSWORD);
        로그인_됨(loginResponse);

        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);

        // 조회
        ExtractableResponse<Response> findResponse = 토큰_기반_조회_요청(tokenResponse);
        회원_정보_조회됨(findResponse, MY_EMAIL, MY_AGE);

        // 수정
        ExtractableResponse<Response> updateResponse = 토큰_기반_수정_요청(tokenResponse, MY_NEW_EMAIL, MY_NEW_PASSWORD, MY_NEW_AGE);
        회원_정보_수정됨(updateResponse);

        // 재로그인
        ExtractableResponse<Response> reLoginResponse = 로그인_요청(MY_NEW_EMAIL, MY_NEW_PASSWORD);
        TokenResponse newTokenResponse = reLoginResponse.as(TokenResponse.class);
        ExtractableResponse<Response> newFindResponse = 토큰_기반_조회_요청(newTokenResponse);
        회원_정보_조회됨(newFindResponse, MY_NEW_EMAIL, MY_NEW_AGE);

        // 삭제
        ExtractableResponse<Response> deleteResponse = 토큰_기반_삭제_요청(newTokenResponse);
        회원_삭제됨(deleteResponse);
    }


    private static ExtractableResponse<Response> 토큰_기반_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 토큰_기반_수정_요청(TokenResponse tokenResponse, String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().put("/members/me")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 토큰_기반_삭제_요청(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/members/me")
            .then().log().all()
            .extract();
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
