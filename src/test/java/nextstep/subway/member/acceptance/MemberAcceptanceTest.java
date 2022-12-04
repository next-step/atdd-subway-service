package nextstep.subway.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.정상_로그인_토큰_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

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
     * Feature: 나의 회원정보 관련 기능
     *
     * Background
     * *  Given 회원이 생성 되어 있고
     * *  And 로그인 되어있음
     * *  And 로그인 하고 받은 토큰을 보유
     *
     * Scenario: 나의 회원정보 관리
     * [1] 토큰으로 나의 회원정보를 조회할 수 있다.
     * *  When 로그인하고 받은 토큰으로 나의 정보를 조회하면
     * *  Then 나의 정보를 응답받을 수 있다.
     * [2] 토큰으로 나의 회원정보를 수정할 수 있다.
     * *  When 로그인하고 받은 토큰으로 나의 정보를 수정하면
     * *  Then 나의 정보를 수정할 수 있다.
     * [3] 토큰으로 나의 회원정보를 삭제할 수 있다.
     * *  When 로그인하고 받은 토큰으로 나의 정보를 삭제하면
     * *  Then 나의 정보를 삭제할 수 있다.
     */
    @DisplayName("나의 회원정보 관리 통합 테스트")
    @TestFactory
    Stream<DynamicTest> dynamicTestsFromCollection() {
        // given
        회원_생성됨(회원_생성을_요청(EMAIL, PASSWORD, AGE));
        String 토큰 = 정상_로그인_토큰_반환(EMAIL, PASSWORD);
        MemberRequest 수정_회원_정보 = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        return Stream.of(
                dynamicTest("토큰으로 나의 회원정보를 조회할 수 있다.", () -> 나의_정보를_조회한다(토큰)),
                dynamicTest("토큰으로 나의 회원정보를 수정할 수 있다.", () -> 나의_정보를_수정한다(토큰, 수정_회원_정보)),
                dynamicTest("토큰으로 나의 회원정보를 삭제할 수 있다..", () -> 나의_정보를_삭제한다())
        );}



    void 나의_정보를_조회한다(String 토큰) {
        // when
        ExtractableResponse<Response> 조회_결과 = 나의_회원_정보_조회_요청(토큰);
        // then
        회원_정보_조회됨(조회_결과, EMAIL, AGE);
    }
    void 나의_정보를_수정한다(String 토큰, MemberRequest 수정_정보) {
        // when
        ExtractableResponse<Response> 수정_결과 = 나의_회원_정보_수정_요청(토큰, 수정_정보);
        // then
        회원_정보_수정됨(수정_결과);
        assertThat(정상_로그인_토큰_반환(NEW_EMAIL, NEW_PASSWORD)).isNotNull();
    }

    void 나의_정보를_삭제한다() {
        // given
        String 토큰 = 정상_로그인_토큰_반환(NEW_EMAIL, NEW_PASSWORD);
        // when
        ExtractableResponse<Response> 삭제_결과 = 나의_회원_정보_삭제_요청(토큰);
        // then
        회원_삭제됨(삭제_결과);
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

    public static ExtractableResponse<Response> 나의_회원_정보_조회_요청(String token) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 나의_회원_정보_수정_요청(String token, MemberRequest memberRequest) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 나의_회원_정보_삭제_요청(String token) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
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
}
