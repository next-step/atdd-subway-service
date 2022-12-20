package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTestFixture.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AuthAcceptanceTestFixture {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 토큰_생성_요청(new TokenRequest(EMAIL, PASSWORD));
        토큰_생성됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        String 잘못된_이메일 = EMAIL + "1111";
        String 잘못된_비밀번호 = PASSWORD + "1111";
        TokenRequest request1 = new TokenRequest(EMAIL, 잘못된_비밀번호);
        TokenRequest request2 = new TokenRequest(잘못된_이메일, PASSWORD);

        ExtractableResponse<Response> response1 = 토큰_생성_요청(request1);
        ExtractableResponse<Response> response2 = 토큰_생성_요청(request2);

        assertAll(
                () -> 토큰_생성_실패함(response1),
                () -> 토큰_생성_실패함(response2)
        );
    }

    /**
     * Given 회원 생성되어 있음
     *
     * When 유효하지 않은 토큰 사용하여 나의 정보 조회 요청하면
     * Then 나의 정보 조회에 실패한다
     *
     * When 유효하지 않은 토큰 사용하여 나의 정보 수정 요청하면
     * Then 나의 정보 수정에 실패한다
     *
     * When 유효하지 않은 토큰 사용하여 나의 정보 삭제 요청하면
     * Then 나의 정보 삭제에 실패한다
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        String notValidToken = "@@@@@";

        // When 유효하지 않은 토큰 사용하여 나의 정보 조회 요청하면
        ExtractableResponse<Response> response1 =  나의_정보_조회_요청(notValidToken);
        // Then 나의 정보 조회에 실패한다
        나의_정보_조회_실패(response1);

        // When 유효하지 않은 토큰 사용하여 나의 정보 수정 요청하면
        ExtractableResponse<Response> response2 = 나의_정보_수정_요청(notValidToken, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
        // Then 나의 정보 수정에 실패한다
        나의_정보_수정_실패(response2);

        // When 유효하지 않은 토큰 사용하여 나의 정보 삭제 요청하면
        ExtractableResponse<Response> response3 = 나의_정보_삭제_요청(notValidToken);
        // Then 나의 정보 삭제에 실패한다
        나의_정보_삭제_실패(response3);
    }

}
