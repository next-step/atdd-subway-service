package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceFixture.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFixture.로그인_요청_성공;
import static nextstep.subway.member.MemberAcceptanceFixture.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NEW_EMAIL = "newemail@email.com";
    private static final String NEW_PASSWORD = "newpassword";
    private static final int AGE = 20;
    private static final int NEW_AGE = 21;

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
     * Feature: 나의 정보 관리
     *
     * When: 회원 생성 요청
     * Then: 회원 생성됨
     *
     * When: 로그인 요청
     * Then: 로그인 성공(토큰 응답)
     *
     * Given: 로그인 성공 토큰 저장
     * When: 내 정보 조회(토큰)
     * Then: 내 정보 조회 응답
     *
     * When: 내 정보 수정(토큰, 수정 값)
     * Then: 내 정보 수정 응답
     *
     * Given: 변경된 이메일로 토큰 재요청
     * When: 내 정보 삭제(토큰)
     * Then: 내 정보 삭제 응답
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {

        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        // then
        로그인_요청_성공(로그인_요청_응답);

        // given
        String 로그인_성공_토큰_값 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();
        // when
        ExtractableResponse<Response> 내_회원_정보_조회_응답 = 내_회원_정보_조회_요청(로그인_성공_토큰_값);
        // then
        내_회원_정보_조회_응답_성공(내_회원_정보_조회_응답, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 내_회원_정보_수정_응답 = 내_회원_정보_수정_요청(로그인_성공_토큰_값, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        내_회원_정보_수정됨(내_회원_정보_수정_응답);

        // given
        ExtractableResponse<Response> 새로운_이메일_토큰_응답 = 로그인_요청(new TokenRequest(NEW_EMAIL, NEW_PASSWORD));
        String 새로운_토큰_생성_응답 = 새로운_이메일_토큰_응답.as(TokenResponse.class).getAccessToken();
        // when
        ExtractableResponse<Response> 내_회원_삭제_응답 = 내_회원_삭제_요청(새로운_토큰_생성_응답);
        // then
        내_회원_정보_삭제됨(내_회원_삭제_응답);
    }


}
