package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.*;

class MemberAcceptanceTest extends MemberAcceptanceTestSupport {
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
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> tokenResponse = 토큰_발급_요청(EMAIL, PASSWORD);
        // then
        토큰_생성_완료(tokenResponse);

        // when
        String accessToken = getAccessToken(tokenResponse);
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(accessToken);
        // then
        내_정보_조회됨(findResponse);

        // when
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(accessToken, EMAIL, PASSWORD, AGE);
        // then
        내_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(accessToken);
        // then
        내_정보_삭제됨(deleteResponse);
    }
}
