package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_성공됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_실패함;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTestRequest.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static final TokenRequest 등록된_계정_토큰_요청 = new TokenRequest(EMAIL, PASSWORD);
    public static final TokenRequest 등록되지_않은_계정_토큰_요청 = new TokenRequest(NEW_EMAIL, NEW_PASSWORD);

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성됨(회원_생성을_요청(EMAIL, PASSWORD, AGE));
    }

    @TestFactory
    @DisplayName("로그인을 시도한다")
    Stream<DynamicTest> 로그인을_시도한다() {
        AuthToken authToken = new AuthToken();
        return Stream.of(
                dynamicTest("등록된 계정으로 로그인 시도시 성공한다.", 로그인_요청_성공됨(등록된_계정_토큰_요청, authToken)),
                dynamicTest("로그인이 되었는지 확인한다", 나의_정보_조회_요청_및_성공함(authToken, EMAIL, AGE))
        );
    }

    @TestFactory
    @DisplayName("없는 계정으로 로그인시 실패한다")
    Stream<DynamicTest> 없는_계정으로_로그인시_실패한다() {
        return Stream.of(
                dynamicTest("없는 계정으로 로그인 시도시 실패한다.", 로그인_요청_실패함(등록되지_않은_계정_토큰_요청))
        );
    }

    @TestFactory
    @DisplayName("유효하지 않는 토큰으로 정보 조회시 실패한다")
    Stream<DynamicTest> myInfoWithWrongBearerAuth() {
        AuthToken authToken = new AuthToken("ASDF.ASDF.ASDF");
        return Stream.of(
                dynamicTest("유효하지 않는 토큰으로 조회시 실패한다.", 나의_정보_조회_요청_및_권한없음(authToken))
        );
    }
}
