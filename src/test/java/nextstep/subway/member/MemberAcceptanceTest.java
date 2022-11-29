package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.*;
import static nextstep.subway.member.MemberAcceptanceStep.*;
import static nextstep.subway.member.MyInfoAcceptanceStep.*;

public class MemberAcceptanceTest extends AcceptanceTest {
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
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
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
    @TestFactory
    Stream<DynamicTest> manageMyInfo() {
        생성된_회원(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인된_회원(EMAIL, PASSWORD).as(TokenResponse.class);
        return Stream.of(
                DynamicTest.dynamicTest("내 정보 조회", () -> {
                    // when
                    ExtractableResponse<Response> findResponse = 내_정보_조회(tokenResponse);
                    // then
                    회원_정보_조회됨(findResponse, EMAIL, AGE);
                }),
                DynamicTest.dynamicTest("내 정보 수정", () -> {
                    // when
                    ExtractableResponse<Response> updateResponse = 내_정보_수정(tokenResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
                    // then
                    회원_정보_수정됨(updateResponse);
                }),
                DynamicTest.dynamicTest("내 정보 삭제", () -> {
                    // given
                    TokenResponse newToken = 로그인_요청(NEW_EMAIL, NEW_PASSWORD).as(TokenResponse.class);
                    // when
                    ExtractableResponse<Response> deleteResponse = 내_정보_삭제(newToken);
                    // then
                    회원_삭제됨(deleteResponse);
                })
        );
    }
}
