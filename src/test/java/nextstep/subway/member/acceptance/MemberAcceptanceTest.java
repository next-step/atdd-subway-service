package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestUtils.로그인되어_있음;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestUtils.인증_실패;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestUtils.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @Test
    @DisplayName("회원 정보를 관리한다.")
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
     * Feature: 나의 정보를 관리한다.
     *
     *   Background
     *     Given 나의 정보가 회원으로 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 나의 정보(회원 정보) 조회 요청 - 유효하지 않은 토큰
     *     Then 인증 실패
     *     When 나의 정보(회원 정보) 수정 요청 - 유효하지 않은 토큰
     *     Then 인증 실패
     *     When 나의 정보(회원) 삭제 요청 - 유효하지 않은 토큰
     *     Then 인증 실패
     *     When 나의 정보(회원 정보) 조회 요청
     *     Then 회원 정보 조회됨
     *     When 나의 정보(회원 정보) 수정 요청
     *     Then 회원 정보 수정됨
     *     When 나의 정보(회원) 삭제 요청
     *     Then 회원 삭제됨
     */

    @TestFactory
    @DisplayName("나의 정보 관리 통합 인수 테스트")
    Collection<DynamicTest> manageMyInfo() {
        // background
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD).as(TokenResponse.class);

        return Arrays.asList(
                dynamicTest("유효하지 않은 토큰으로 나의 정보를 조회하면 인증 실패된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 나의_정보_조회_요청("wrongToken");

                    // then
                    인증_실패(response);
                }),
                dynamicTest("유효하지 않은 토큰으로 나의 정보를 수정하면 인증 실패된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 나의_정보_수정_요청("wrongToken", NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

                    // then
                    인증_실패(response);
                }),
                dynamicTest("유효하지 않은 토큰으로 나의 정보를 삭제하면 인증 실패된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 나의_정보_삭제_요청("wrongToken");

                    // then
                    인증_실패(response);
                }),
                dynamicTest("나의 정보 조회 요청을 하면 회원 정보가 조회된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 나의_정보_조회_요청(tokenResponse.getAccessToken());

                    // then
                    회원_정보_조회됨(response, EMAIL, AGE);
                }),
                dynamicTest("나의 정보 수정을 요청하면 회원 정보가 수정된다.", () -> {
                    // when
                    ExtractableResponse<Response> updateResponse = 나의_정보_수정_요청(tokenResponse.getAccessToken(), NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

                    // then
                    회원_정보_수정됨(updateResponse);
                }),
                dynamicTest("나의 정보 삭제 요청을 하면 회원 정보가 삭제된다.", () -> {
                    // when
                    ExtractableResponse<Response> deleteResponse = 나의_정보_삭제_요청(tokenResponse.getAccessToken());

                    // then
                    회원_삭제됨(deleteResponse);
                })
        );
    }
}
