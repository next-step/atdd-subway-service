package nextstep.subway.member;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTestHelper;
import nextstep.subway.auth.dto.TokenResponse;

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
        ExtractableResponse<Response> createResponse = MemberAcceptanceTestHelper.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        MemberAcceptanceTestHelper.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = MemberAcceptanceTestHelper.회원_정보_조회_요청(createResponse);
        // then
        MemberAcceptanceTestHelper.회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberAcceptanceTestHelper.회원_정보_수정_요청(createResponse,
            NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        MemberAcceptanceTestHelper.회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberAcceptanceTestHelper.회원_삭제_요청(createResponse);
        // then
        MemberAcceptanceTestHelper.회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> createResponse = MemberAcceptanceTestHelper.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        MemberAcceptanceTestHelper.회원_생성됨(createResponse);

        // given
        String accessToken = AuthAcceptanceTestHelper.로그인_되어_있음(EMAIL, PASSWORD)
            .as(TokenResponse.class)
            .getAccessToken();
        Map<String, String> params = new HashMap<>();
        params.put("email", NEW_EMAIL);
        params.put("password", NEW_PASSWORD);
        params.put("age", String.valueOf(NEW_AGE));

        // when
        ExtractableResponse<Response> response = MemberAcceptanceTestHelper.내_정보_수정_요청(params, accessToken);

        // then
        MemberAcceptanceTestHelper.내_정보_수정_성공(response);

        // given
        accessToken = AuthAcceptanceTestHelper.로그인_되어_있음(NEW_EMAIL, NEW_PASSWORD)
            .as(TokenResponse.class)
            .getAccessToken();

        // when
        ExtractableResponse<Response> findResponse = MemberAcceptanceTestHelper.내_정보_조회_요청(accessToken);

        // then
        MemberAcceptanceTestHelper.내_정보_조회_성공(findResponse);
        MemberAcceptanceTestHelper.회원_정보_조회됨(findResponse, NEW_EMAIL, NEW_AGE);
    }
}
