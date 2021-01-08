package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
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
        ExtractableResponse<Response> createResponse = MemberAcceptanceSupport.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        MemberAcceptanceSupport.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = MemberAcceptanceSupport.회원_정보_조회_요청(createResponse);
        // then
        MemberAcceptanceSupport.회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberAcceptanceSupport.회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        MemberAcceptanceSupport.회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberAcceptanceSupport.회원_삭제_요청(createResponse);
        // then
        MemberAcceptanceSupport.회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // 로그인 절차
        MemberAcceptanceSupport.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String accessToken = MemberAcceptanceSupport.회원_로그인_요청(EMAIL, PASSWORD);

        // 조회
        ExtractableResponse<Response> 내_정보_조회_결과 = MemberAcceptanceSupport.내_정보_조회_요청(accessToken);
        MemberAcceptanceSupport.내_정보_조회_성공함(내_정보_조회_결과, EMAIL, AGE);

        // 업데이트
        ExtractableResponse<Response> 내_정보_업데이트_결과 = MemberAcceptanceSupport.내_정보_업데이트(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        MemberAcceptanceSupport.내_정보_업데이트_성공함(내_정보_업데이트_결과);
        accessToken = MemberAcceptanceSupport.회원_로그인_요청(NEW_EMAIL, NEW_PASSWORD);
        MemberAcceptanceSupport.내_정보_조회_성공함(MemberAcceptanceSupport.내_정보_조회_요청(accessToken), NEW_EMAIL, NEW_AGE);

        // 삭제
        ExtractableResponse<Response> 내_정보_삭제_결과 = MemberAcceptanceSupport.내_정보_삭제(accessToken);
        MemberAcceptanceSupport.냬_정보_삭제_성공함(내_정보_삭제_결과);
        MemberAcceptanceSupport.내_정보_조회_실패함(MemberAcceptanceSupport.내_정보_조회_요청(accessToken));
    }

}
