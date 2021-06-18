package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthToken;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_성공됨;
import static nextstep.subway.member.MemberAcceptanceTestRequest.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

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

    @TestFactory
    @DisplayName("나의 정보를 조회한다.")
    Stream<DynamicTest> 나의_정보를_조회한다() {
        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("회원을 등록한다", 회원_생성_요청_및_성공함(EMAIL, PASSWORD, AGE)),
                dynamicTest("로그인을 요청한다", 로그인_요청_성공됨(new TokenRequest(EMAIL, PASSWORD), authToken)),
                dynamicTest("회원 정보를 조회한다", 나의_정보_조회_요청_및_성공함(authToken, EMAIL, AGE))
        );
    }

    @TestFactory
    @DisplayName("나의 정보를 수정한다.")
    Stream<DynamicTest> 나의_정보를_수정한다() {
        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("회원을 등록한다", 회원_생성_요청_및_성공함(EMAIL, PASSWORD, AGE)),
                dynamicTest("로그인을 요청한다", 로그인_요청_성공됨(new TokenRequest(EMAIL, PASSWORD), authToken)),
                dynamicTest("회원 정보를 수정한다", 나의_정보_수정_요청_및_성공함(authToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE)),
                dynamicTest("이메일이 바뀐 기존의 토큰으로는 수정된 회원 정보를 조회할 수 없다", 나의_정보_조회_요청_및_실패함(authToken)),
                dynamicTest("새로운 계정으로 로그인을 요청한다", 로그인_요청_성공됨(new TokenRequest(NEW_EMAIL, NEW_PASSWORD), authToken)),
                dynamicTest("바뀐 정보로 회원 정보를 조회한다", 나의_정보_조회_요청_및_성공함(authToken, NEW_EMAIL, NEW_AGE))
        );
    }

    @TestFactory
    @DisplayName("나의 정보를 삭제한다.")
    Stream<DynamicTest> 나의_정보를_삭제한다() {
        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("회원을 등록한다", 회원_생성_요청_및_성공함(EMAIL, PASSWORD, AGE)),
                dynamicTest("로그인을 요청한다", 로그인_요청_성공됨(new TokenRequest(EMAIL, PASSWORD), authToken)),
                dynamicTest("회원 정보를 삭제한다", 나의_정보_삭제_요청_및_성공함(authToken)),
                dynamicTest("삭제된 회원 정보를 조회한다", 나의_정보_조회_요청_및_실패함(authToken))
        );
    }
}
