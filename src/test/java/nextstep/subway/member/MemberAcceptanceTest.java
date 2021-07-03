package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

@SuppressWarnings("NonAsciiCharacters")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    private final User user = new User(EMAIL, PASSWORD, AGE);
    private final User newUser = new User(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        final ExtractableResponse<Response> createResponse = 회원_생성을_요청(given(), user);
        // then
        회원_생성됨(createResponse);

        // given
        final String location = createResponse.header("Location");
        // when
        final ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(given(), location);
        // then
        회원_정보_조회됨(findResponse, user);

        // when
        final ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(given(), location, newUser);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        final ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(given(), location);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        final ExtractableResponse<Response> createResponse = 회원_생성을_요청(given(), user);
        // then
        회원_생성됨(createResponse);

        // when
        final ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        // then
        로그인_됨(loginResponse);

        // given
        final TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        final Header authorization = new Header("Authorization", "Bearer" + tokenResponse.getAccessToken());
        final String meUri = "/members/me";

        // when
        final ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(given(authorization), meUri);
        // then
        회원_정보_조회됨(findResponse, user);

        // when
        final ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(given(authorization), meUri, newUser);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        final ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(given(authorization), meUri);
        // then
        회원_삭제됨(deleteResponse);
    }

    private static RequestSpecification given(final Header header) {
        final RequestSpecification given = given();

        if (header != null) {
            given.header(header);
        }

        return given;
    }

    public static RequestSpecification given() {
        return RestAssured.given();
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(final RequestSpecification given, final User user) {
        return given
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new MemberRequest(user.email, user.password, user.age))
            .when().post("/members")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(final RequestSpecification given, final String uri) {
        return given
            .log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(final RequestSpecification given,
        final String uri,
        final User user) {

        return given
            .log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new MemberRequest(user.email, user.password, user.age))
            .when().put(uri)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(final RequestSpecification given, final String uri) {
        return given
            .log().all()
            .when().delete(uri)
            .then().log().all()
            .extract();
    }

    public static void 회원_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(final ExtractableResponse<Response> response, final User user) {
        final MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(user.email);
        assertThat(memberResponse.getAge()).isEqualTo(user.age);
    }

    public static void 회원_정보_수정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static class User {
        private final String email;
        private final String password;
        private final int age;

        public User(final String email, final String password, final int age) {
            this.email = email;
            this.password = password;
            this.age = age;
        }
    }
}
