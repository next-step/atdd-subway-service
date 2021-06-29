package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

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
        //given
        회원_생성_되어있음(EMAIL, PASSWORD, AGE);
        TokenResponse 토큰 = 로그인되어있음(EMAIL, PASSWORD);
        
        //when
        ExtractableResponse<Response> myInfoResponse = 나의정보_조회_요청(토큰);

        // then
        나의정보_조회됨(myInfoResponse);

        // when
        ExtractableResponse<Response> updateResponse = 나의정보_수정_요청(토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        나의정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 나의정보_삭제_요청(토큰);

        // then
        나의정보_삭제됨(deleteResponse);
    }

    private void 나의정보_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertHttpStatus(deleteResponse, NO_CONTENT);
    }

    private ExtractableResponse<Response> 나의정보_삭제_요청(TokenResponse 토큰) {
        return delete("/members/me", 토큰.getAccessToken());
    }

    private void 나의정보_수정됨(ExtractableResponse<Response> updateResponse) {
        assertHttpStatus(updateResponse, OK);
    }

    private ExtractableResponse<Response> 나의정보_수정_요청(TokenResponse 토큰, String newEmail, String newPassword, int newAge) {
        return put("/members/me", new MemberRequest(newEmail, newPassword, newAge), 토큰.getAccessToken());
    }

    public static void 나의정보_조회됨(ExtractableResponse<Response> myInfoResponse) {
        assertHttpStatus(myInfoResponse, OK);
    }

    public static ExtractableResponse<Response> 나의정보_조회_요청(TokenResponse 토큰) {
        return get("/members/me", 토큰.getAccessToken());
    }

    public static ExtractableResponse<Response> 회원_생성_되어있음(String email, String password, Integer age) {
        ExtractableResponse<Response> response = 회원_생성을_요청(email, password, age);
        회원_생성됨(response);
        return response;
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        return post("/members", new MemberRequest(email, password, age));
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        return get(response.header("Location"));
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        return put(response.header("Location"), new MemberRequest(email, password, age));
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        return delete(response.header("Location"));
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertHttpStatus(response, CREATED);
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertHttpStatus(response, OK);
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertHttpStatus(response, NO_CONTENT);
    }
}
