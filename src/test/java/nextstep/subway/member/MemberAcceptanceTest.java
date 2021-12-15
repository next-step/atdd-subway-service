package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestAssuredUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        final ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // given
        final ExtractableResponse<Response> tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        final String token = tokenResponse.as(TokenResponse.class).getAccessToken();
        final ExtractableResponse<Response> findResponse = 나의_회원_정보_조회_요청(token);
        // then
        나의_회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        final ExtractableResponse<Response> updateResponse = 나의_회원_정보_수정_요청(
            createResponse,
            NEW_EMAIL,
            NEW_PASSWORD, NEW_AGE
        );
        // then
        나의_회원_정보_수정됨(updateResponse);

        // when
        final ExtractableResponse<Response> deleteResponse = 나의_회원_정보_삭제_요청(createResponse);
        // then
        나의_회원_정보_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_등록되어_있음(
        final String email,
        final String password,
        final int age
    ) {
        return 회원_생성을_요청(email, password, age);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(
        final String email,
        final String password,
        final Integer age
    ) {
        final MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssuredUtil.jsonPost(memberRequest, "/members");
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(
        final ExtractableResponse<Response> response
    ) {
        final String uri = response.header("Location");
        return RestAssuredUtil.jsonGet(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(
        final ExtractableResponse<Response> response,
        final String email,
        final String password, final Integer age
    ) {
        final String uri = response.header("Location");
        final MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssuredUtil.jsonPut(memberRequest, uri);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(
        final ExtractableResponse<Response> response
    ) {
        final String uri = response.header("Location");
        return RestAssuredUtil.delete(uri);
    }

    public static ExtractableResponse<Response> 나의_회원_정보_조회_요청(final String token) {
        return RestAssuredUtil.auth(token, "/members/me");
    }

    public static ExtractableResponse<Response> 나의_회원_정보_수정_요청(
        final ExtractableResponse<Response> response,
        final String email,
        final String password, final Integer age
    ) {
        final String uri = response.header("Location");
        final MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssuredUtil.jsonPut(memberRequest, uri);
    }

    public static ExtractableResponse<Response> 나의_회원_정보_삭제_요청(
        final ExtractableResponse<Response> response
    ) {
        final String uri = response.header("Location");
        return RestAssuredUtil.delete(uri);
    }

    public static void 회원_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(
        final ExtractableResponse<Response> response,
        final String email,
        final int age
    ) {
        final MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 나의_회원_정보_조회됨(
        final ExtractableResponse<Response> response,
        final String email,
        final int age
    ) {
        final MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 나의_회원_정보_수정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 나의_회원_정보_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
