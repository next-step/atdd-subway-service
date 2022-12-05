package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberSteps.*;

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
        ExtractableResponse<Response> findResponse = MemberSteps.회원_정보_조회_요청(createResponse);
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

    /*
        Feature: 나의 정보를 관리한다.

          Background
            Given 회원 등록되어 있음
            And 로그인 되어있음

          Scenario: 나의 정보를 관리
            When 내 정보 조회 요청
            Then 내 정보 조회됨
            When 내 정보 수정 요청
            Then 내 정보 수정됨
            When 내 정보 삭제 요청
            Then 내 정보 삭제됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(accessToken);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(accessToken);
        회원_삭제됨(deleteResponse);
    }
}
