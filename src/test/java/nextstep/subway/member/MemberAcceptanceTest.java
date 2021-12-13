package nextstep.subway.member;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestTestApi;
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
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL,
            NEW_PASSWORD, NEW_AGE);
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
        TokenResponse tokenResponse = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD)
            .as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();

        // When 내 정보 조회
        ExtractableResponse<Response> myInfoResponse = 내_정보_조회(accessToken);

        // Then 내 정보 조회됨
        내_정보_조회됨(myInfoResponse);

        // When 내 정보 수정
        내_정보_수정(accessToken);

        // Then 내 정보 수정됨
        내_정보_수정됨(accessToken);

        // When 내 정보 삭제
        ExtractableResponse<Response> deleteResponse = RestTestApi.delete("/members/me",
            accessToken);

        // Then 내 정보 삭제됨
        내_정보_삭제됨(deleteResponse);
    }

    private void 내_정보_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 내_정보_수정됨(String accessToken) {
        ExtractableResponse<Response> response = RestTestApi.get("/members/me", accessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 내_정보_수정(String accessToken) {
        MemberRequest memberRequest = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        RestTestApi.put("/members/me", accessToken, memberRequest);
    }

    private void 내_정보_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
    }

    private ExtractableResponse<Response> 내_정보_조회(String accessToken) {
        return RestTestApi.get("/members/me", accessToken);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password,
        Integer age) {
        return RestTestApi.post("/members", new MemberRequest(email, password, age));
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(
        ExtractableResponse<Response> response) {
        return RestTestApi.get(response.header("Location"));
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response,
        String email, String password, Integer age) {
        return RestTestApi.put(response.header("Location"),
            new MemberRequest(email, password, age));
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        return RestTestApi.delete(response.header("Location"));
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

    public static ExtractableResponse<Response> 회원_등록되어_있음(final String email,
        final String password, final int age) {
        return 회원_생성을_요청(email, password, age);
    }
}
