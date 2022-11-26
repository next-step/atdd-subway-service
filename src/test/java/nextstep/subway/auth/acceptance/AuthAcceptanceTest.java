package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String WRONG_EMAIL = "wrongemail@email.com";
    private static final String WRONG_PASSWORD = "wrongpassword";
    private static final int AGE = 20;
    private static final int WRONG_AGE = 21;
    private MemberResponse 정상회원;
    private TokenRequest 정상회원_로그인_요청;
    private TokenRequest 비정상회원_로그인_요청;

    @BeforeEach
    void setup() {
        MemberRequest 정상회원_생성_요청 = new MemberRequest(EMAIL, PASSWORD, AGE);
        회원_생성되어_있음(정상회원_생성_요청);
        정상회원_로그인_요청 = new TokenRequest(EMAIL, PASSWORD);
        비정상회원_로그인_요청 = new TokenRequest(WRONG_EMAIL, WRONG_PASSWORD);
    }

    /**
     * Feature: 로그인 기능
     *
     *   Scenario: 로그인을 시도한다.
     *     Given 정상회원 등록되어 있음
     *     And 비정상회원 등록되어 있지 않음
     *     When 정상회원 로그인 요청
     *     Then 정상회원 로그인 됨
     *     When 비정상회원 로그인 요청
     *     Then 비정상회원 로그인 안됨
     */
    @DisplayName("로그인 기능")
    @TestFactory
    Collection<DynamicTest> loginSenario() {
        return Arrays.asList(
                DynamicTest.dynamicTest("존재하는 회원이 로그인을 시도하면 로그인이 된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 로그인_요청(정상회원_로그인_요청);

                    // then
                    로그인됨(response);
                }),
                DynamicTest.dynamicTest("존재하지 않는 회원이 로그인을 시도하면 로그인이 안된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 로그인_요청(비정상회원_로그인_요청);

                    // then
                    로그인_안됨(response);
                })
        );
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private static void 로그인됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 로그인_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
