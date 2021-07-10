package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.AuthToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class AuthAcceptanceTest extends AcceptanceTest {

    public static final String email = "jordy-torvalds@jordy-torvalds.o-r.kr";
    public static final String correctPassword = "jordy";
    public static final String wrongPassword = "jordy*";
    public static final int age = 29;

    static final AuthToken illegalAccessToken = new AuthToken("illegal.illegal.illegal");

    @DisplayName("권한을 관리 한다")
    @TestFactory
    Stream<DynamicTest> manageAuth() {
        final String email = "jordy-torvalds@jordy-torvalds.o-r.kr";
        final String correctPassword ="jordy";

        return Stream.of(
                dynamicTest("회원 생성 요청 및 성공 확인", 회원_생성_요청_및_성공_확인(email, correctPassword, 29)),
                dynamicTest("유효하지 않은 아이디와 비밀번호로 로그인 요청 및 실패 확인", 로그인_요청_및_실패_확인(email, wrongPassword)),
                dynamicTest("로그인 요청 및 성공 확인", 로그인_요청_및_성공_확인(email, correctPassword)),
                dynamicTest("로그인 성공 후 잘못된 토큰으로 요청시 실패 확인", 권한_없이_회원_정보_요청_및_실패_확인(illegalAccessToken))
        );
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank();
    }

    public static void 로그인_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    public static Executable 로그인_요청_및_실패_확인(String email, String password) {
        return () -> {
            ExtractableResponse<Response> loginResponse = 로그인_요청(email, password);

            //then
            로그인_실패_확인(loginResponse);
        };
    }

    public static Executable 로그인_요청_및_성공_확인(String email, String password) {
        return () -> {
            ExtractableResponse<Response> loginResponse = 로그인_요청(email, password);

            //then
            로그인_성공_확인(loginResponse);
        };
    }
}
