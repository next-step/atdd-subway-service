package nextstep.subway.domain.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.domain.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.domain.auth.dto.TokenResponse;
import nextstep.subway.domain.member.dto.MemberRequest;
import nextstep.subway.domain.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("내 정보 조회 기능")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;
    private String accessToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰_발급(EMAIL, PASSWORD);
    }

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

        accessToken = 토큰_발급(NEW_EMAIL, NEW_PASSWORD);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        final ExtractableResponse<Response> 나의_정보_조회_요청_응답 = 나의_정보_조회_요청();
        // then
        나의_정보_조회_성공(나의_정보_조회_요청_응답);

        // when
        final ExtractableResponse<Response> 나의_정보_수정_요청_응답 = 나의_정보_수정_요청();
        // then
        나의_정보_수정_성공(나의_정보_수정_요청_응답);

        accessToken = 토큰_발급(NEW_EMAIL, NEW_PASSWORD);

        // when
        final ExtractableResponse<Response> 나의_정보_삭제_요청_응답 = 나의_정보_삭제_요청();
        // then
        회원_삭제됨(나의_정보_삭제_요청_응답);

    }

    private ExtractableResponse<Response> 나의_정보_삭제_요청() {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("members/me")
                .then().log().all()
                .extract();
    }

    private void 나의_정보_수정_성공(final ExtractableResponse<Response> response) {
        final MemberResponse memberResponse = response.as(MemberResponse.class);
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(memberResponse.getEmail()).isEqualTo(NEW_EMAIL);
            assertThat(memberResponse.getAge()).isEqualTo(NEW_AGE);
        });
    }

    private ExtractableResponse<Response> 나의_정보_수정_요청() {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE))
                .when().put("members/me")
                .then().log().all()
                .extract();
    }

    private void 나의_정보_조회_성공(final ExtractableResponse<Response> response) {
        final MemberResponse memberResponse = response.as(MemberResponse.class);
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
            assertThat(memberResponse.getAge()).isEqualTo(AGE);
        });
    }

    private ExtractableResponse<Response> 나의_정보_조회_요청() {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("members/me")
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

    public static String 토큰_발급(String email, String password) {
        AuthAcceptanceTest authAcceptanceTest = new AuthAcceptanceTest();
        final ExtractableResponse<Response> response = authAcceptanceTest.로그인_요청(email, password);

        return response.as(TokenResponse.class).getAccessToken();
    }
}
