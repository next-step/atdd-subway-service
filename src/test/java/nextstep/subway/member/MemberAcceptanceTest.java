package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    private ExtractableResponse<Response> 회원_생성_요청_응답;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성_요청_응답 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @TestFactory
    Stream<DynamicTest> 회원_정보_관리_시나리오() {
        return Stream.of(
                dynamicTest("회원 생성을 확인한다.", this::회원_생성을_확인한다),
                dynamicTest("회원 정보 조회를 요청한다.", this::회원_정보_조회를_요청한다),
                dynamicTest("회원 정보 수정을 요청한다.", this::회원_정보_수정을_요청한다),
                dynamicTest("회원 삭제를 요청한다.", this::회원_삭제를_요청한다)
        );
    }

    @TestFactory
    Stream<DynamicTest> 나의_정보_관리_시나리오() {
        return Stream.of(
                dynamicTest("나의 정보를 조회한다.", this::나의_정보를_조회한다),
                dynamicTest("나의 정보를 수정한다.", this::나의_정보를_수정한다),
                dynamicTest("나의 정보를 삭제한다.", this::나의_정보를_삭제한다)
        );
    }

    private void 회원_생성을_확인한다() {
        // then
        회원_생성됨(회원_생성_요청_응답);
    }

    private void 회원_정보_조회를_요청한다() {
        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(회원_생성_요청_응답);

        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);
    }

    private void 회원_정보_수정을_요청한다() {
        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(회원_생성_요청_응답, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        회원_정보_수정됨(updateResponse);
    }

    private void 회원_삭제를_요청한다() {
        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(회원_생성_요청_응답);

        // then
        회원_삭제됨(deleteResponse);
    }

    private void 나의_정보를_조회한다() {
        // given
        ExtractableResponse<Response> 로그인_정보 = 로그인_요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> 나의_정보_응답 = 나의_정보_조회_요청(로그인_정보);

        // then
        나의_정보_조회됨(나의_정보_응답, EMAIL);
    }

    private void 나의_정보를_수정한다() {

    }

    private void 나의_정보를_삭제한다() {

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

    public static void 나의_정보_조회됨(ExtractableResponse<Response> response, String email) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
    }

    public static ExtractableResponse<Response> 나의_정보_조회_요청(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}
