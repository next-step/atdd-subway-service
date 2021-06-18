package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.*;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_성공됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_실패함;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class AuthAcceptanceTest extends AcceptanceTest {

    public static TokenRequest 등록된_계정_토큰_요청 = new TokenRequest(EMAIL, PASSWORD);
    public static TokenRequest 등록되지_않은_계정_토큰_요청 = new TokenRequest(NEW_EMAIL, NEW_PASSWORD);

    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> response = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(response);
    }

    @TestFactory
    @DisplayName("로그인을 시도한다")
    Stream<DynamicTest> 로그인을_시도한다() {
        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(등록된_계정_토큰_요청, authToken))
        );
    }

    @TestFactory
    @DisplayName("없는 계정으로 로그인시 실패한다")
    Stream<DynamicTest> 없는_계정으로_로그인시_실패한다() {
        return Stream.of(
                dynamicTest("없는 계정으로 로그인 시도시 실패한다.", 로그인_요청_실패함(등록되지_않은_계정_토큰_요청))
        );
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }


}
