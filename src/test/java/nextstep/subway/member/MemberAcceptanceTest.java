package nextstep.subway.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RequestUtil;
import nextstep.subway.utils.StatusCodeCheckUtil;

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
        // given
        final String uri = 회원_생성을_요청(EMAIL, PASSWORD, AGE).header("Location");
        final String accessToken = AuthAcceptanceTest.로그인_토큰_발급(EMAIL, PASSWORD);

        // when
        final ExtractableResponse<Response> findResponse = 자신의_회원_정보_조회_요청(accessToken);

        // then
        자신의_회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        final ExtractableResponse<Response> updateResponse =
            자신의_회원_정보_수정_요청(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        자신의_회원_정보_수정됨(updateResponse, uri, NEW_EMAIL, NEW_AGE);

        // given
        final String newAccessToken = AuthAcceptanceTest.로그인_토큰_발급(NEW_EMAIL, NEW_PASSWORD);

        // when
        final ExtractableResponse<Response> deleteResponse = 자신의_회원_정보_삭제_요청(newAccessToken);

        // then
        자신의_회원_정보_삭제됨(deleteResponse, uri);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RequestUtil.post("/members", memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RequestUtil.get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(
        ExtractableResponse<Response> response, String email, String password, Integer age
    ) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RequestUtil.put(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RequestUtil.delete(uri);
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        StatusCodeCheckUtil.ok(response);
        MemberResponse memberResponse = response.as(MemberResponse.class);
        validateMember(memberResponse, email, age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.noContent(response);
    }

    public static void 회원_등록되어_있음(final String email, final String password, final int age) {
        회원_생성을_요청(email, password, age);
    }

    public static ExtractableResponse<Response> 자신의_회원_정보_조회_요청(final String accessToken) {
        return RequestUtil.getWithAccessToken("/members/me", accessToken);
    }

    public static ExtractableResponse<Response> 자신의_회원_정보_수정_요청(
        final String accessToken, final String email, final String password, final int age
    ) {
        return RequestUtil.putWithAccessToken("/members/me", accessToken, new MemberRequest(email, password, age));
    }

    public static ExtractableResponse<Response> 자신의_회원_정보_삭제_요청(final String accessToken) {
        return RequestUtil.deleteWithAccessToken("/members/me", accessToken);
    }

    public static void 자신의_회원_정보_조회됨(
        final ExtractableResponse<Response> response, final String email, final int age
    ) {
        StatusCodeCheckUtil.ok(response);
        MemberResponse memberResponse = response.as(MemberResponse.class);
        validateMember(memberResponse, email, age);
    }

    public static void 자신의_회원_정보_수정됨(
        final ExtractableResponse<Response> response, final String uri, final String email, final int age
    ) {
        StatusCodeCheckUtil.ok(response);
        final MemberResponse memberResponse = RequestUtil.get(uri).as(MemberResponse.class);
        validateMember(memberResponse, email, age);
    }

    public static void 자신의_회원_정보_삭제됨(final ExtractableResponse<Response> response, final String uri) {
        StatusCodeCheckUtil.noContent(response);
        final ExtractableResponse<Response> findResponse = RequestUtil.get(uri);
        StatusCodeCheckUtil.notFound(findResponse);
    }

    private static void validateMember(final MemberResponse memberResponse, final String email, final int age) {
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }
}
