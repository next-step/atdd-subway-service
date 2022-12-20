package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.토큰_값;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberAcceptanceTest extends MemberAcceptanceTestFixture {

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
     * Feature: 나의 정보를 관리한다
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 나의 정보를 관리
     *     When 나의 정보 조회 요청
     *     Then 나의 정보 조회됨
     *
     *     When 나의 정보 수정 요청
     *     Then 나의 정보 수정됨
     *
     *     When 수정된 나의 정보 조회 요청
     *     Then 수정된 나의 정보 조회됨
     *
     *     When 나의 정보 삭제 요청
     *     Then 나의 정보 삭제됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // When 나의 정보 조회 요청
        ExtractableResponse<Response> response = 나의_정보_조회_요청(myAccessToken);
        // Then 나의 정보 조회됨
        나의_정보_조회됨(response, EMAIL, AGE);

        // When 나의 정보 수정 요청
        ExtractableResponse<Response> response2 = 나의_정보_수정_요청(myAccessToken, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
        // Then 나의 정보 수정됨
        나의_정보_수정됨(response2);
        String myNewAccessToken = 토큰_값(response2);

        // When 수정된 나의 정보 조회 요청
        ExtractableResponse<Response> response3 = 나의_정보_조회_요청(myNewAccessToken);
        // Then 수정된 나의 정보 조회됨
        나의_정보_조회됨(response3, NEW_EMAIL, NEW_AGE);

        // When 나의 정보 삭제 요청
        ExtractableResponse<Response> response4 = 나의_정보_삭제_요청(myNewAccessToken);
        // Then 나의 정보 삭제됨
        나의_정보_삭제됨(response4);
    }
}
