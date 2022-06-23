package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceResponse.로그인_성공_토큰_반환;
import static nextstep.subway.member.MemberAcceptanceRequest.나의_정보_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceRequest.나의_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceRequest.나의_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceRequest.회원_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceRequest.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceRequest.회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceRequest.회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceResponse.나의_정보_삭제_요청_성공;
import static nextstep.subway.member.MemberAcceptanceResponse.나의_정보_수정_요청_성공;
import static nextstep.subway.member.MemberAcceptanceResponse.나의_정보_요청_성공;
import static nextstep.subway.member.MemberAcceptanceResponse.회원_삭제됨;
import static nextstep.subway.member.MemberAcceptanceResponse.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceResponse.회원_정보_수정됨;
import static nextstep.subway.member.MemberAcceptanceResponse.회원_정보_조회됨;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

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
    @TestFactory
    Stream<DynamicTest> manageMyInfo() {
        AtomicReference<String> accessToken = new AtomicReference<>("");
        AtomicReference<String> secondAccessToken = new AtomicReference<>("");


        return Stream.of(
                dynamicTest("로그인이 성공하면 토큰을 반환한다", () -> {
                    ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
                    accessToken.set(로그인_성공_토큰_반환(response));
                }),
                dynamicTest("토큰으로 나의 정보를 조회한다", () -> {
                    ExtractableResponse<Response> response = 나의_정보_조회_요청(accessToken.get());
                    나의_정보_요청_성공(response, EMAIL, AGE);
                }),
                dynamicTest("토큰으로 나의 정보를 수정한다", () -> {
                    ExtractableResponse<Response> response = 나의_정보_수정_요청(accessToken.get(), new MemberRequest(NEW_EMAIL, PASSWORD, AGE));
                    나의_정보_수정_요청_성공(response);
                }),
                dynamicTest("새로운 토큰을 반환한다", () -> {
                    ExtractableResponse<Response> reLoginSuccessResponse = 로그인_요청(NEW_EMAIL, PASSWORD);
                    secondAccessToken.set(로그인_성공_토큰_반환(reLoginSuccessResponse));
                }),
                dynamicTest("새로운 토큰으로 나의 정보를 조회한다", () -> {
                    ExtractableResponse<Response> findResponse = 나의_정보_조회_요청(secondAccessToken.get());
                    나의_정보_요청_성공(findResponse, NEW_EMAIL, AGE);
                }),
                dynamicTest("새로운 토큰으로 나의 정보를 삭제한다", () -> {
                    ExtractableResponse<Response> response = 나의_정보_삭제_요청(secondAccessToken.get());
                    나의_정보_삭제_요청_성공(response);
                })
        );
    }
}
