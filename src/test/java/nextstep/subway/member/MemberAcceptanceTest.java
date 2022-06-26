package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthTestUtils.*;
import static nextstep.subway.member.MemberTestUtils.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    private ExtractableResponse<Response> createResponse;

    @BeforeEach
    void init() {
        createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
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
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        회원_로그인_성공확인(loginResponse);

        TokenResponse loginToken = loginResponse.as(TokenResponse.class);

        // when
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(loginToken);
        // then
        내_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(loginToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        내_정보_수정됨(updateResponse);


        // when
        TokenResponse newLoginToken = 로그인_요청(NEW_EMAIL, NEW_PASSWORD).as(TokenResponse.class);
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(newLoginToken);
        // then
        내_정보_삭제됨(deleteResponse);
    }
}
