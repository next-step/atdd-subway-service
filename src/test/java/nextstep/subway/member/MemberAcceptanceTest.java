package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.request.AcceptanceTestRequest;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_성공됨;
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

    @TestFactory
    @DisplayName("나의 정보를 관리한다.")
    Stream<DynamicTest> manageMyInfo() {
        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("회원을 등록한다", 회원_생성_요청_및_성공함(EMAIL, PASSWORD, AGE)),
                dynamicTest("로그인을 요청한다", 로그인_요청_성공됨(new TokenRequest(EMAIL, PASSWORD), authToken)),
                dynamicTest("회원 정보를 조회한다", 나의_정보_조회_요청_및_성공함(authToken, EMAIL, AGE)),
                dynamicTest("회원 정보를 수정한다", 나의_정보_수정_요청_및_성공함(authToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE)),
                dynamicTest("이메일이 바뀐 기존의 토큰으로는 수정된 회원 정보를 조회할 수 없다", 나의_정보_조회_요청_및_실패함(authToken)),
                dynamicTest("로그인을 요청한다", 로그인_요청_성공됨(new TokenRequest(NEW_EMAIL, NEW_PASSWORD), authToken)),
                dynamicTest("바뀐 정보로 회원 정보를 조회한다", 나의_정보_조회_요청_및_성공함(authToken, NEW_EMAIL, NEW_AGE)),
                dynamicTest("회원 정보를 삭제한다", 나의_정보_삭제_요청_및_성공함(authToken)),
                dynamicTest("삭제된 회원 정보를 조회한다", 나의_정보_조회_요청_및_실패함(authToken))
        );
    }

    public static Executable 나의_정보_조회_요청_및_성공함(AuthToken token, String email, Integer age) {
        return () -> {
            ExtractableResponse<Response> response = AcceptanceTestRequest.get(
                    Given.builder().bearer(token.getToken()).build(),
                    When.builder().uri("/members/me").build()
            );

            회원_정보_조회됨(response, email, age);
        };
    }

    public static Executable 나의_정보_수정_요청_및_성공함(AuthToken token, String email, String password, Integer age) {
        return () -> {
            ExtractableResponse<Response> response = AcceptanceTestRequest.put(
                    Given.builder()
                            .bearer(token.getToken())
                            .body(new MemberRequest(email, password, age))
                            .contentType(ContentType.JSON)
                            .build(),
                    When.builder().uri("/members/me").build()
            );

            회원_정보_수정됨(response);
        };
    }

    public static Executable 나의_정보_삭제_요청_및_성공함(AuthToken token) {
        return () -> {
            ExtractableResponse<Response> response = AcceptanceTestRequest.delete(
                    Given.builder().bearer(token.getToken()).build(),
                    When.builder().uri("/members/me").build()
            );

            회원_삭제됨(response);
        };
    }

    public static Executable 나의_정보_조회_요청_및_실패함(AuthToken token) {
        return () -> {
            ExtractableResponse<Response> response = AcceptanceTestRequest.get(
                    Given.builder().bearer(token.getToken()).build(),
                    When.builder().uri("/members/me").build()
            );

            회원_정보_조회_실패함(response);
        };
    }

    public static Executable 회원_생성_요청_및_성공함(String email, String password, Integer age) {
        return () -> {
            ExtractableResponse<Response> response = 회원_생성을_요청(email, password, age);

            회원_생성됨(response);
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

    public static void 회원_정보_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
