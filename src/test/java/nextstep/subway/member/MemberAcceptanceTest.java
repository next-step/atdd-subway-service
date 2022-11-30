package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTestActions.내_정보_삭제_성공;
import static nextstep.subway.member.MemberAcceptanceTestActions.내_정보_수정_성공;
import static nextstep.subway.member.MemberAcceptanceTestActions.내_정보_조회_성공;
import static nextstep.subway.member.MemberAcceptanceTestActions.내_정보를_삭제한다;
import static nextstep.subway.member.MemberAcceptanceTestActions.내_정보를_수정한다;
import static nextstep.subway.member.MemberAcceptanceTestActions.내_정보를_조회한다;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_삭제됨;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_정보_수정됨;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTestActions.회원_정보_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    /**
     * <p>Feature: 내 정보 관리 기능
     * <p>  Background
     * <p>    Given 회원 등록되어 있음
     * <p>    AND 로그인 되어있음
     * <p>    AND 모든 요청에 토큰값이 포함됨
     *
     * <p>  Scenario: 내 정보 (조회, 수정, 삭제)
     * <p>    When 내 정보를 조회한다
     * <p>    Then 조회 성공
     *
     * <p>    When 내 정보를 수정한다
     * <p>    Then 수정 성공
     *
     * <p>    When 내 정보를 삭제한다
     * <p>    Then 삭제 성공
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // Given 회원 등록되어 있음
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        // And 로그인 되어있음
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String accessToken = loginResponse.jsonPath().getString("accessToken");

        // When 내 정보를 조회한다
        ExtractableResponse<Response> getResponse = 내_정보를_조회한다(accessToken);

        // Then 조회 성공
        내_정보_조회_성공(getResponse, EMAIL, AGE);

        // When 내 정보를 수정한다
        ExtractableResponse<Response> updateResponse = 내_정보를_수정한다(accessToken, "email@kakao.com", "password", 29);

        // Then 수정 성공
        내_정보_수정_성공(updateResponse);

        // When 내 정보를 삭제한다
        ExtractableResponse<Response> deleteResponse = 내_정보를_삭제한다(accessToken);

        // Then 삭제 성공
        내_정보_삭제_성공(deleteResponse);
    }


}
