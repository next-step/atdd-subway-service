package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원정보_조회_요청(createResponse);
        // then
        MemberResponse memberResponse = findResponse.as(MemberResponse.class);
        회원_정보_조회됨(memberResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원정보_수정_요청(memberResponse.getId(), NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원정보_조회_요청(final ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return get(uri);
    }

    public static ExtractableResponse<Response> 회원정보_수정_요청(final Long id, final String email, final String password, final Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return put("/members/" + id, memberRequest);
    }

    @DisplayName("내정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        final ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // given
        final ExtractableResponse<Response> tokenResponse = AuthAcceptanceTest.로그인_요청(new TokenRequest("email@email.com", "password"));

        // when
        final String accessToken = tokenResponse.as(TokenResponse.class).getAccessToken();
        final ExtractableResponse<Response> findResponse = 내정보_조회_요청(accessToken);
        // then
        MemberResponse memberResponse = findResponse.as(MemberResponse.class);
        회원_정보_조회됨(memberResponse, EMAIL, AGE);

        // when
        final ExtractableResponse<Response> updateResponse = 내정보_수정_요청(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        final ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_생성_요청(final String email, final String password, final Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return post("/members", memberRequest);
    }

    public static ExtractableResponse<Response> 내정보_조회_요청(final String accessToken) {
        return get("/members/me", accessToken);
    }

    public static ExtractableResponse<Response> 내정보_수정_요청(final String accessToken, final String email, final String password, final Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return put("/members/me", memberRequest, accessToken);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(final ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return delete(uri);
    }

    public static void 회원_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(final MemberResponse memberResponse, final String email, final int age) {
        assertAll(
                () -> assertThat(memberResponse.getId()).isNotNull(),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(email),
                () -> assertThat(memberResponse.getAge()).isEqualTo(age)
        );
    }

    public static void 회원_정보_수정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
