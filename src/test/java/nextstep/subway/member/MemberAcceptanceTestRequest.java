package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.request.AcceptanceTestRequest;
import nextstep.subway.request.Given;
import nextstep.subway.request.When;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.request.AcceptanceTestRequest.get;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTestRequest {
    public static Executable 나의_정보_조회_요청_및_성공함(AuthToken token, String email, Integer age) {
        return () -> {
            ExtractableResponse<Response> response = get(
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
            ExtractableResponse<Response> response = get(
                    Given.builder().bearer(token.getToken()).build(),
                    When.builder().uri("/members/me").build()
            );

            회원_정보_조회_실패함(response);
        };
    }

    public static Executable 나의_정보_조회_요청_및_권한없음(AuthToken token) {
        return () -> {
            ExtractableResponse<Response> response = get(
                    Given.builder().bearer(token.getToken()).build(),
                    When.builder().uri("/members/me").build()
            );

            회원_정보_조회_권한없음(response);
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

    private static void 회원_정보_조회_권한없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
