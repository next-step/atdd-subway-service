package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.AuthToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
;
import static nextstep.subway.auth.domain.AuthTestSnippet.로그인_요청;
import static nextstep.subway.member.domain.MemberTestSnippet.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@DisplayName("회원 관리 인수 테스트")
class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 id로 관리한다.")
    @Test
    void manageMemberWithId() {
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
        회원_정보_수정됨(updateResponse, createResponse, NEW_EMAIL, NEW_AGE);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("회원 정보를 token으로 관리한다.")
    @Test
    void manageMemberWithToken() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);

        // then
        AuthToken token = loginResponse.as(AuthToken.class);

        // when
        ExtractableResponse<Response> findResponse = 토큰으로_회원_정보_조회_요청(token);

        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 토큰으로_회원_정보_수정_요청(token, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        회원_정보_수정됨(updateResponse, createResponse, NEW_EMAIL, NEW_AGE);
        ExtractableResponse<Response> reLoginResponse = 로그인_요청(NEW_EMAIL, NEW_PASSWORD);
        token = reLoginResponse.as(AuthToken.class);

        // when
        ExtractableResponse<Response> deleteResponse = 토큰으로_회원_삭제_요청(token);

        // then
        회원_삭제됨(deleteResponse);
    }
}
