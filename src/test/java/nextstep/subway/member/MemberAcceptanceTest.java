package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_생성이_요청됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String MEMBERS_ME_PATH = "/members/me";

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
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String token = 토큰_생성이_요청됨(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 나의_정보_조회_결과 = 나의_정보_조회_요청(token);
        // then
        회원_정보_조회됨(나의_정보_조회_결과, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 나의_정보_수정_결과 = 나의_정보_수정_요청(token, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(나의_정보_수정_결과);

        // given
        token = 토큰_생성이_요청됨(NEW_EMAIL, NEW_PASSWORD).as(TokenResponse.class).getAccessToken();
        // when
        ExtractableResponse<Response> 나의_정보_삭제_결과 = 나의_정보_삭제_요청(token);
        // then
        회원_삭제됨(나의_정보_삭제_결과);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return post(memberRequest, "/members");
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return put(memberRequest, uri);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return delete(uri);
    }


    public static ExtractableResponse<Response> 나의_정보_조회_요청(String token) {
        return get(token, MEMBERS_ME_PATH);
    }

    public static ExtractableResponse<Response> 나의_정보_수정_요청(String token, String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return put(token, memberRequest, MEMBERS_ME_PATH);
    }

    public static ExtractableResponse<Response> 나의_정보_삭제_요청(String token) {
        return delete(token, MEMBERS_ME_PATH);
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
