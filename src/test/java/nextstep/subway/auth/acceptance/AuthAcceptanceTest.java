package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.AuthToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;

import java.util.stream.Stream;

import static nextstep.subway.auth.domain.AuthTestSnippet.로그인_요청_및_성공_확인;
import static nextstep.subway.auth.domain.AuthTestSnippet.로그인_요청_및_실패_확인;
import static nextstep.subway.member.domain.MemberTestSnippet.권한_없이_회원_정보_요청_및_실패_확인;
import static nextstep.subway.member.domain.MemberTestSnippet.회원_생성_요청_및_성공_확인;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "jordy-torvalds@jordy-torvalds.o-r.kr";
    private static final String CORRECT_PASSWORD = "jordy";
    private static final String WRONG_PASSWORD = "jordy*";
    private static final int AGE = 29;

    private static final AuthToken illegalAccessToken = new AuthToken("illegal.illegal.illegal");

    @DisplayName("권한을 관리 한다")
    @TestFactory
    Stream<DynamicTest> manageAuth() {
        return Stream.of(
                dynamicTest("회원 생성 요청 및 성공 확인", 회원_생성_요청_및_성공_확인(EMAIL, CORRECT_PASSWORD, 29)),
                dynamicTest("유효하지 않은 아이디와 비밀번호로 로그인 요청 및 실패 확인", 로그인_요청_및_실패_확인(EMAIL, WRONG_PASSWORD)),
                dynamicTest("로그인 요청 및 성공 확인", 로그인_요청_및_성공_확인(EMAIL, CORRECT_PASSWORD)),
                dynamicTest("로그인 성공 후 잘못된 토큰으로 요청시 실패 확인", 권한_없이_회원_정보_요청_및_실패_확인(illegalAccessToken))
        );
    }
}
