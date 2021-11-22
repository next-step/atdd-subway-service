package nextstep.subway.member;

import static nextstep.subway.member.MemberAcceptanceMethods.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;

public class MemberAcceptanceTest extends AcceptanceTest {
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
}
