package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethod.*;
import static nextstep.subway.member.MemberAcceptanceMethod.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    /**
     * Scenario: 회원 생성
     *  Given 이메일, 패스워드, 나이 정보로
     *  When 회원 생성을 요청하면
     *  Then 회원이 생성됨
     * Scenario: 회원 정보 조회
     *  When 회원 정보 조회를 요청하면
     *  Then 회원 정보가 조회됨
     * Scenario: 회원 정보 수정
     *  Given 새로운 이메일, 패스워드, 나이 정보로
     *  When 회원 정보 수정을 요청하면
     *  Then 회원 정보가 수정됨
     * Scenario: 회원 삭제
     *  When 회원 삭제를 요청하면
     *  Then 회원이 삭제됨
     */
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
     * Scenario: 내 정보 생성
     *  Given 이메일, 패스워드, 나이 정보로
     *  When 회원 생성을 요청하면
     *  Then 회원이 생성됨
     * Scenario: 토큰 발급 요청
     *  When 생성된 정보로 토큰 발급을 요청하면
     *  Then 토큰이 발급됨
     * Scenario: 내 정보 조회
     *  Given 발급된 토큰으로
     *  When 내 정보 조회를 요청하면
     *  Then 내 정보가 조회됨
     * Scenario: 내 정보 수정
     *  Given 발급퇸 토큰과 새로운 이메일, 패스워드, 나이 정보로
     *  When 내 정보 수정을 요청하면
     *  Then 내 정보가 수정됨
     * Scenario: 내 정보 삭제
     *  When 발급된 토큰으로 삭제를 요청하면
     *  Then 내 정보가 삭제됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // Scenario: 내 정보 생성
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // Scenario: 토큰 발급 요청
        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        // then
        로그인_성공(loginResponse);
        토큰_발급됨(loginResponse);

        // Scenario: 내 정보 조회
        // given
        TokenResponse tokenResponse = loginResponse.as(TokenResponse.class);
        // when
        ExtractableResponse<Response> findResponse = 토큰으로_내정보_조회_요청(tokenResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // Scenario: 내 정보 수정
        // when
        ExtractableResponse<Response> updateResponse = 토큰으로_내정보_수정_요청(tokenResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // Scenario: 내 정보 삭제
        // when
        ExtractableResponse<Response> deleteResponse = 토큰으로_내정보_삭제_요청(tokenResponse);
        // then
        회원_삭제됨(deleteResponse);
    }
}
