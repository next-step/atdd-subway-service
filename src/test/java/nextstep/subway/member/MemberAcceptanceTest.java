package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethods.*;
import static nextstep.subway.member.MemberAcceptanceMethods.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 등록한다.")
    @Test
    void addMember() {
        // give
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(memberRequest);

        // then
        회원_생성됨(createResponse);
    }

    @DisplayName("등록된 회원 정보를 조회한다.")
    @Test
    void findMember() {
        // give
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_등록되어_있음(memberRequest);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);
    }

    @DisplayName("등록된 회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_등록되어_있음(memberRequest);

        // when
        MemberRequest updateMemberRequest = MemberRequest.of(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, updateMemberRequest);

        // then
        회원_정보_수정됨(updateResponse);
    }

    @DisplayName("등록된 회원 정보를 삭제한다.")
    @Test
    void removeMember() {
        // given
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_등록되어_있음(memberRequest);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나(로그인한 회원)의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        MemberRequest memberRequest = MemberRequest.of(EMAIL, PASSWORD, AGE);
        회원_등록되어_있음(memberRequest);

        TokenRequest tokenRequest = TokenRequest.of(EMAIL, PASSWORD);
        TokenResponse token = 회원_로그인_요청(tokenRequest).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> 조회_Response = 로그인한_회원_정보_조회_요청(token);

        // then
        회원_정보_조회됨(조회_Response, EMAIL, AGE);

        // when
        MemberRequest memberUpdateRequest = MemberRequest.of(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> 수정_Response = 로그인한_회원_정보_수정_요청(token, memberUpdateRequest);

        // then
        회원_정보_수정됨(수정_Response);

        // when
        ExtractableResponse<Response> 삭제_Response = 로그인한_회원_정보_삭제_요청(token);

        // then
        회원_삭제됨(삭제_Response);
    }
}
