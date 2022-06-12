package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공후_토큰_조회됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_시도함;
import static nextstep.subway.member.MemberAcceptanceSupport.나의_정보_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.나의_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.나의_정보_일치함;
import static nextstep.subway.member.MemberAcceptanceSupport.나의_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_삭제됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_수정됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_조회됨;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    private ExtractableResponse<Response> createResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

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
    @TestFactory
    Stream<DynamicTest> manageMyInfo() {
        AtomicReference<String> accessToken = new AtomicReference<>("");
        AtomicReference<String> newAccessToken = new AtomicReference<>("");

        return Stream.of(
            dynamicTest("로그인을 시도해서 토큰을 반환받는다" , () -> {
                ExtractableResponse<Response> loginSuccessResponse = 로그인_시도함(EMAIL, PASSWORD);
                accessToken.set(로그인_성공후_토큰_조회됨(loginSuccessResponse));
            }),
            dynamicTest("반환받은 토큰으로 나의 정보 조회를 시도하면 정보가 조회된다", () -> {
                ExtractableResponse<Response> response = 나의_정보_조회_요청(accessToken.get());
                나의_정보_일치함(response, EMAIL, AGE);
            }),
            dynamicTest("토큰을 이용해 나의 정보 수정을 요청한다", () -> {
                ExtractableResponse<Response> updateResponse = 나의_정보_수정_요청(accessToken.get(), new MemberRequest(NEW_EMAIL, PASSWORD, AGE));
                회원_정보_수정됨(updateResponse);
            }),
            dynamicTest("로그인을 시도해 새로운 토큰정보를 반환받는다", () -> {
                ExtractableResponse<Response> reLoginSuccessResponse = 로그인_시도함(NEW_EMAIL, PASSWORD);
                로그인_성공됨(reLoginSuccessResponse);
                newAccessToken.set(로그인_성공후_토큰_조회됨(reLoginSuccessResponse));
            }),
            dynamicTest("토큰을 이용해 수정된 정보를 조회하면 수정된 정보가 조회된다", () -> {
                ExtractableResponse<Response> findResponse = 나의_정보_조회_요청(newAccessToken.get());
                나의_정보_일치함(findResponse, NEW_EMAIL, AGE);
            }),
            dynamicTest("토큰을 이용해 나의 정보를 삭제하면 정상적으로 삭제된다", () -> {
                ExtractableResponse<Response> deleteResponse = 나의_정보_삭제_요청(newAccessToken.get());
                회원_삭제됨(deleteResponse);
            })
        );
    }
}
