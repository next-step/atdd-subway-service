package nextstep.subway.member.domain;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.domain.MemberTestSnippet.*;

@DisplayName("회원 관리 유닛  테스트")
class MemberTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final Age AGE = new Age(20);
    public static final Age NEW_AGE = new Age(21);

    public ExtractableResponse<Response> createResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @Test
    @DisplayName("회원 조회 및 확인")
    void findMember() {
        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);
    }

    @Test
    @DisplayName("회원 수정 및 확인")
    void updateMember() {
        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        회원_정보_수정됨(updateResponse, createResponse, NEW_EMAIL, NEW_AGE);
    }

    @Test
    @DisplayName("회원 삭제 및 확인")
    void deleteMember() {
        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(deleteResponse);
    }
}
