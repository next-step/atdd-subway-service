package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.ApiRequest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

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
        회원_생성되어있음(EMAIL, PASSWORD, AGE);
        TokenResponse token = 로그인_요청되어_있음(EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> searchResponse = 나의_정보를_조회_한다(EMAIL, PASSWORD);

        // then
        회원_정보_조회됨(searchResponse, EMAIL, AGE);

        // when
        나의_정보를_수정_한다(token, "new@email.com", "dklfsaj", 2);

        // then
        AuthAcceptanceTest.로그인을_요청한다(EMAIL, PASSWORD); // TODO : 수정 후 반영 결과 확인 방법

        // when
        ExtractableResponse<Response> response = 나의_정보를_삭제_한다(token);

        // then
        회원_삭제됨(response);
    }

    public static ExtractableResponse<Response> 회원_생성되어있음(String email, String password, int age) {
        return 회원_생성을_요청(email, password, age);
    }

    private ExtractableResponse<Response> 나의_정보를_삭제_한다(TokenResponse token) {
        return ApiRequest.deleteWithAuth("/members/me", token.getAccessToken());
    }

    private ExtractableResponse<Response> 로그인_요청되어_있음(String email, String password) {
        return AuthAcceptanceTest.로그인을_요청한다(email, password);
    }

    private ExtractableResponse<Response> 나의_정보를_조회_한다(String email, String password) {
        TokenResponse tokenResponse = AuthAcceptanceTest.로그인을_요청한다(email, password).as(TokenResponse.class);
        return ApiRequest.getWithAuth("/members/me", tokenResponse.getAccessToken());
    }

    private ExtractableResponse<Response> 나의_정보를_수정_한다(TokenResponse token, String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return ApiRequest.putWithAuth("/members/me", token.getAccessToken(), memberRequest);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return ApiRequest.post("/members", memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return ApiRequest.get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return ApiRequest.put(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return ApiRequest.delete(uri);
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
