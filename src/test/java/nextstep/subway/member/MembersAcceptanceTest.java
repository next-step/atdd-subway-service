package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberAcceptanceStep.회원_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceStep.회원_삭제됨;
import static nextstep.subway.member.MemberAcceptanceStep.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceStep.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceStep.회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceStep.회원_정보_수정됨;
import static nextstep.subway.member.MemberAcceptanceStep.회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceStep.회원_정보_조회됨;
import static nextstep.subway.member.MembersMeAcceptanceStep.나의_정보_삭제_요청;
import static nextstep.subway.member.MembersMeAcceptanceStep.나의_정보_삭제됨;
import static nextstep.subway.member.MembersMeAcceptanceStep.나의_정보_수정_요청;
import static nextstep.subway.member.MembersMeAcceptanceStep.나의_정보_수정됨;
import static nextstep.subway.member.MembersMeAcceptanceStep.나의_정보_조회_요청;
import static nextstep.subway.member.MembersMeAcceptanceStep.나의_정보_조회됨;

class MembersAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    /**
     * Feature: 회원 정보 관리 기능
     *
     * Scenario: 회원 정보를 관리한다.
     *   When: 회원 생성 요청 함
     *   Then: 회원 생성됨
     *   When: 회원 정보 조회 요청 함
     *   Then: 회원 정보 조회됨
     *   When: 회원 정보 수정 요청 함
     *   Then: 회원 정보 수정됨
     *   When: 회원 정보 삭제 요청 함
     *   Then: 회원 정보 삭제됨
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        회원_삭제됨(deleteResponse);
    }


    /**
     * Feature: 나의 정보 관리 기능
     *   Background
     *     Given 회원가입이 되어 있음
     *     And 로그인하여 토큰 정보를 알고 있음
     *   Scenario: 나의 정보를 관리한다.
     *     When 나의 정보를 조회 요청함
     *     Then 나의 정보 조회됨
     *     When 나의 정보 수정 요청함
     *     Then 나의 정보 수정됨
     *     Given 토큰 재발급됨
     *     When 나의 정보 삭제 요청함
     *     Then 나의 정보 삭제됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        ExtractableResponse<Response> 생성요청 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(생성요청);
        TokenResponse 토큰_정보 = MembersMeAcceptanceStep.로그인됨(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> 조회응답 = 나의_정보_조회_요청(토큰_정보);
        나의_정보_조회됨(조회응답, EMAIL, AGE);

        ExtractableResponse<Response> 수정응답 = 나의_정보_수정_요청(토큰_정보, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        나의_정보_수정됨(수정응답);

        토큰_정보 = 토큰_재발급됨();

        ExtractableResponse<Response> 삭제응답 = 나의_정보_삭제_요청(토큰_정보);
        나의_정보_삭제됨(삭제응답);
    }

    private TokenResponse 토큰_재발급됨() {
        return MembersMeAcceptanceStep.로그인됨(NEW_EMAIL, NEW_PASSWORD, AGE);
    }

}
