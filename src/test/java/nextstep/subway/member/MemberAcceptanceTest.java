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

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.잘못된_토큰정보_생성;
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

    /**
     * Feature: 나의 정보 관련
     *   Scenario: 로그인을 성공하고 나의 정보를 조회합니다
     *     Given 새로운 회원을 등록한다
     *     When 로그인하지 않음
     *     Then 나의 정보를 조회할 수 없음
     *     When 로그인을 통해 생성된 토큰을 이용하여 나의 정보 조회 요청
     *     Then 나의 정보가 조회됨
     *     When 로그인을 통해 생성된 토큰을 이용하여 비밀번호 수정 요청
     *     Then 패스워드가 수정됨
     *     When 변경된 패스워드로 로그인 요청
     *     Then 로그인 성공
     *     When 로그인을 통해 생성된 토큰을 이용하여 회원 탈퇴 요청
     *     Then 회원 정보가 삭제됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given: 새로운 회원을 등록한다
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // when: 로그인하지 않으면
        // then: 나의 정보를 조회할 수 없음
        ExtractableResponse<Response> 실패결과 = 토큰_회원정보_조회_요청(잘못된_토큰정보_생성());
        내_정보_조회에_실패(실패결과);
        // when: 로그인을 통해 생성된 토큰을 이용하여 나의 정보 조회 요청
        ExtractableResponse<Response> 로그인결과 = 로그인_요청(EMAIL, PASSWORD);
        TokenResponse 로그인토큰 = 로그인결과.as(TokenResponse.class);
        // then: 나의 정보가 조회됨
        ExtractableResponse<Response> 조회결과 = 토큰_회원정보_조회_요청(로그인토큰);
        회원_정보_조회됨(조회결과, EMAIL, AGE);
        // when: 로그인을 통해 생성된 토큰을 이용하여 비밀번호 수정 요청
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(로그인토큰, EMAIL, NEW_PASSWORD, AGE);
        // then: 패스워드가 수정됨
        회원_정보_수정됨(updateResponse);
        // when: 변경된 패스워드로 로그인 요청
        ExtractableResponse<Response> 재로그인결과 = 로그인_요청(EMAIL, NEW_PASSWORD);
        TokenResponse 변경된_로그인토큰 = 재로그인결과.as(TokenResponse.class);
        // then: 로그인 성공
        로그인_성공(변경된_로그인토큰);
        // when: 로그인을 통해 생성된 토큰을 이용하여 회원 탈퇴 요청
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(변경된_로그인토큰);
        // then: 회원 정보가 삭제됨
        회원_삭제됨(deleteResponse);
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

    public static ExtractableResponse<Response> 내_정보_수정_요청(TokenResponse tokenResponse, String email, String password, Integer age) {
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


    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_삭제_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/members/me")
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

    public static ExtractableResponse<Response> 토큰_회원정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }


    public static void 내_정보_조회에_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
