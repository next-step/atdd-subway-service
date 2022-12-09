package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공하고_토큰을_발급받는다;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
    @TestFactory
    Stream<DynamicTest> manageMyInfo() {
        return Stream.of(
                dynamicTest("새로운 회원을 등록한다", 회원_생성_요청_성공(EMAIL, PASSWORD, AGE)),
                dynamicTest("로그인하지 않으면 내 정보를 조회할 수 없다", 내_정보_조회_요청_실패("")),
                dynamicTest("로그인에 성공하고 내 정보를 조회한다", 내_정보_조회_요청_성공(EMAIL, PASSWORD)),
                dynamicTest("로그인에 성공하고 비밀번호를 수정한다", 내_정보_수정_요청_성공(EMAIL, PASSWORD)),
                dynamicTest("수정된 비밀번호로 로그인에 성공한다", 로그인_요청_성공(EMAIL, NEW_PASSWORD)),
                dynamicTest("로그인에 성공하고 탈퇴 요청하면 계정이 삭제된다", 회원_탈퇴_요청_성공(EMAIL, NEW_PASSWORD))
        );
    }

    public static Executable 회원_생성_요청_성공(String email, String password, Integer age) {
        return () -> {
            ExtractableResponse<Response> createResponse = 회원_생성을_요청(email, password, age);
            회원_생성됨(createResponse);
        };
    }

    public static Executable 로그인_요청_성공(String email, String password) {
        return () -> {
            로그인을_요청하고_토큰을_발급받는다(email, password);
        };
    }

    public static Executable 내_정보_조회_요청_성공(String email, String password) {
        return () -> {
            ExtractableResponse<Response> tokenResponse = 로그인을_요청하고_토큰을_발급받는다(email, password);
            ExtractableResponse<Response> memberResponse = 내_정보_조회_요청(tokenResponse.jsonPath().getString("accessToken"));
            내_정보_조회에_성공한다(memberResponse, EMAIL, AGE);
        };
    }

    public static Executable 내_정보_조회_요청_실패(String accessToken) {
        return () -> {
            ExtractableResponse<Response> memberResponse = 내_정보_조회_요청(accessToken);
            내_정보_조회에_실패한다(memberResponse);
        };
    }

    public static Executable 내_정보_수정_요청_성공(String email, String password) {
        return () -> {
            ExtractableResponse<Response> tokenResponse = 로그인을_요청하고_토큰을_발급받는다(email, password);
            ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(tokenResponse.jsonPath().getString("accessToken"));
            내_정보_수정에_성공한다(updateResponse);
            ExtractableResponse<Response> memberResponse = 내_정보_조회_요청(tokenResponse.jsonPath().getString("accessToken"));
            내_정보_조회에_성공한다(memberResponse, EMAIL, AGE);
        };
    }

    public static Executable 회원_탈퇴_요청_성공(String email, String password) {
        return () -> {
            ExtractableResponse<Response> tokenResponse = 로그인을_요청하고_토큰을_발급받는다(email, password);
            ExtractableResponse<Response> deleteResponse = 회원_탈퇴_요청(tokenResponse.jsonPath().getString("accessToken"));
            회원_삭제됨(deleteResponse);
            ExtractableResponse<Response> memberResponse = 내_정보_조회_요청(tokenResponse.jsonPath().getString("accessToken"));
            내_정보_조회에_실패한다(memberResponse);
        };
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

    public static ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_탈퇴_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(String accessToken) {
        MemberRequest memberRequest = new MemberRequest(EMAIL, NEW_PASSWORD, AGE);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인을_요청하고_토큰을_발급받는다(String email, String password) {
        ExtractableResponse<Response> tokenResponse = 로그인_요청(new TokenRequest(email, password));
        로그인_성공하고_토큰을_발급받는다(tokenResponse);
        return tokenResponse;
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

    public static void 내_정보_조회에_성공한다(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertAll(()->{
            assertThat(memberResponse.getId()).isNotNull();
            assertThat(memberResponse.getEmail()).isEqualTo(email);
            assertThat(memberResponse.getAge()).isEqualTo(age);
        });
    }

    public static void 내_정보_조회에_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 내_정보_수정에_성공한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
