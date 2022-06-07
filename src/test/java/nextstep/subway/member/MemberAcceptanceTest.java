package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인한_회원_정보_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인한_회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인한_회원_정보_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_등록됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_삭제됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_수정됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberAcceptanceTest extends AcceptanceTest {
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
     * Given. 나의 정보를 회원등록한다.
     *        등록된 회원정보로 토큰 발급을 요청한다.
     * When. 발급된 토큰으로 나의 회원정보를 요청한다.
     * Then. 나의 회원정보가 정상적으로 조회된다.
     * When. 발급된 토큰으로 나의 회원정보를 수정한다.
     * Then. 나의 회원정보가 정상적으로 수정된다.
     * When. 변경된 정보로 토큰 정보를 다시 조회한다.
     *       발급된 토큰으로 나의 회원정보를 삭제한다.
     * Then. 나의 회원정보가 삭제된다.
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);
        회원_등록됨(memberRequest);

        TokenResponse tokenResponse = 로그인_요청(TokenRequest.of(EMAIL, PASSWORD)).as(TokenResponse.class);

        // when.
        ExtractableResponse<Response> 로그인된_회원_정보_Response = 로그인한_회원_정보_요청(tokenResponse);

        // then.
        회원_정보_조회됨(로그인된_회원_정보_Response, EMAIL, AGE);

        // when.
        MemberRequest updateMemberRequest = memberRequest.of(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> 로그인된_회원_정보_수정_Response = 로그인한_회원_정보_수정_요청(tokenResponse, updateMemberRequest);

        // then.
        회원_정보_수정됨(로그인된_회원_정보_수정_Response);

        // when
        TokenResponse newTokenResponse = 로그인_요청(TokenRequest.of(NEW_EMAIL, NEW_PASSWORD)).as(TokenResponse.class);
        ExtractableResponse<Response> 로그인된_회원_정보_삭제_Response = 로그인한_회원_정보_삭제_요청(newTokenResponse);

        // then
        회원_삭제됨(로그인된_회원_정보_삭제_Response);
    }
}
