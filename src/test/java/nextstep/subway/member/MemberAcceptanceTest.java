package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인을_요청;
import static nextstep.subway.member.MemberTestFixture.나의_정보_삭제_요청;
import static nextstep.subway.member.MemberTestFixture.나의_정보_수정_요청;
import static nextstep.subway.member.MemberTestFixture.나의_정보_조회_요청;
import static nextstep.subway.member.MemberTestFixture.회원_삭제_요청;
import static nextstep.subway.member.MemberTestFixture.회원_삭제됨;
import static nextstep.subway.member.MemberTestFixture.회원_생성됨;
import static nextstep.subway.member.MemberTestFixture.회원_생성을_요청;
import static nextstep.subway.member.MemberTestFixture.회원_정보_수정_요청;
import static nextstep.subway.member.MemberTestFixture.회원_정보_수정됨;
import static nextstep.subway.member.MemberTestFixture.회원_정보_조회_요청;
import static nextstep.subway.member.MemberTestFixture.회원_정보_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberAcceptanceTest extends AcceptanceTest {
    private String EMAIL = "email@email.com";
    private String PASSWORD = "password";
    private String NEW_EMAIL = "newemail@email.com";
    private String NEW_PASSWORD = "newpassword";
    private int AGE = 20;
    private int NEW_AGE = 21;

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
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse token = 로그인을_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 나의_정보_조회_요청(token);
        // then
        회원_정보_조회됨(response, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse
                = 나의_정보_수정_요청(token, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 나의_정보_삭제_요청(token);
        // then
        회원_삭제됨(deleteResponse);

    }
}
