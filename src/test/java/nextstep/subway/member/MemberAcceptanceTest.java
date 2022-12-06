package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
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

    @DisplayName("나의 정보를 관리한다. (내 정보를 요청하여 존재한다면 삭제한다)")
    @Test
    void manageMyInfo_findAndDelete() {
        // given 나의 정보가 등록되어 있다.
        회원_생성됨(회원_생성을_요청(EMAIL, PASSWORD, AGE));

        // given 로그인 토큰을 발급받는다.
        String accessToken = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        // when 나의 정보를 요청한다.
        ExtractableResponse<Response> fineResponse = 내정보_요청(accessToken);

        // then 정상 요청 받는다.
        내정보_요청_성공함(fineResponse, EMAIL);

        // when 나의 정보를 삭제한다.
        ExtractableResponse<Response> removeResponse = 내정보_삭제_요청(accessToken);

        // then 정상 삭제가 된다.
        내정보_삭제_성공함(removeResponse);

    }

    @DisplayName("나의 정보를 관리한다. (내 정보를 수정하여 조회하면 수정된것이 반영되어 있다.)")
    @Test
    void manageMyInfo_updateAndFind() {
        // given 나의 정보가 등록되어 있다.
        회원_생성됨(회원_생성을_요청(EMAIL, PASSWORD, AGE));

        // given 로그인 토큰을 발급받는다.
        String accessToken = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        // when 나의 정보를 수정한다.
        ExtractableResponse<Response> updateResponse = 내정보_수정_요청(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then 정상 수정이 된다.
        내정보_수정_성공함(updateResponse);

        // given 로그인 토큰을 재 발급받는다.
        accessToken = AuthAcceptanceTest.로그인_요청(NEW_EMAIL, NEW_PASSWORD).as(TokenResponse.class).getAccessToken();

        // when 나의 정보를 요청한다.
        ExtractableResponse<Response> fineResponse = 내정보_요청(accessToken);

        // then 정상 요청 받는다.
        내정보_요청_성공함(fineResponse, NEW_EMAIL);
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

    public static ExtractableResponse<Response> 내정보_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내정보_수정_요청(String accessToken, String newEmail, String newPassword, int newAge) {
        MemberRequest memberRequest = new MemberRequest(newEmail, newPassword, newAge);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .auth().oauth2(accessToken)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내정보_삭제_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 내정보_요청_성공함(ExtractableResponse<Response> response, String email) {
        MemberResponse memberResponse = response.as(MemberResponse.class);

        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 내정보_요청_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 내정보_수정_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 내정보_삭제_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
