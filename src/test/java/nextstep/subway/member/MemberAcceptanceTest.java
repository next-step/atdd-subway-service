package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.유효하지_않은_인증_토큰;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.인증_실패됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_발급_요청;
import static nextstep.subway.utils.RestAssuredUtils.delete;
import static nextstep.subway.utils.RestAssuredUtils.get;
import static nextstep.subway.utils.RestAssuredUtils.post;
import static nextstep.subway.utils.RestAssuredUtils.put;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String MEMBER_API_BASE_URL = "/members";
    public static final String ME_API_BASE_URL = MEMBER_API_BASE_URL.concat("/me");
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    public static String 인증_토큰;


    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        인증_토큰 = 토큰_발급_요청(EMAIL, PASSWORD);
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

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(인증_토큰);
        // then
        내_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(인증_토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        내_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(인증_토큰);
        // then
        내_정보_삭제됨(deleteResponse);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 인한 나의 정보 관리 예외 발생 검증")
    public void throwException_WhenAccessTokenIsInvalid() {
        // when
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(유효하지_않은_인증_토큰);
        // then
        인증_실패됨(findResponse);

        // when
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(유효하지_않은_인증_토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        인증_실패됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(유효하지_않은_인증_토큰);
        // then
        인증_실패됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(
        final String email,
        final String password,
        final Integer age
    ) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return post(MEMBER_API_BASE_URL, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(
        ExtractableResponse<Response> response,
        final String email,
        final String password,
        final Integer age
    ) {
        final String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return put(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        final String uri = response.header("Location");
        return delete(uri);
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(final String accessToken) {
        return get(accessToken, ME_API_BASE_URL);
    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(
        final String accessToken,
        final String newEmail,
        final String newPassword,
        final int newAge
    ) {
        MemberRequest memberRequest = new MemberRequest(newEmail, newPassword, newAge);
        return put(accessToken, ME_API_BASE_URL, memberRequest);
    }

    public static ExtractableResponse<Response> 내_정보_삭제_요청(final String accessToken) {
        return delete(accessToken, ME_API_BASE_URL);
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

    public static void 내_정보_조회됨(
        ExtractableResponse<Response> response,
        final String email,
        final int age
    ) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 내_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 내_정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
