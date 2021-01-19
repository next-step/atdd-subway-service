package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.utils.AuthRestAssuredTestUtils.*;
import static nextstep.subway.utils.MemberRestAssuredUtils.*;

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
        회원_등록됨(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        로그인_됨(loginResponse);
        String 토큰 = loginResponse.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> myInfoResponse = 나의_정보_조회를_요청(토큰);
        // then
        나의_정보가_조회됨(myInfoResponse);

        // when
        ExtractableResponse<Response> changeMyInfoResponse = 나의_정보_수정을_요청(토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        나의_정보가_수정됨(changeMyInfoResponse);
        String 새로운_토큰 = 나의_수정된_정보가_조회됨(NEW_EMAIL, NEW_PASSWORD);

        // when
        나의_정보_삭제를_요청(새로운_토큰);
        // then
        나의_정보가_삭제됨(새로운_토큰);
    }
}
