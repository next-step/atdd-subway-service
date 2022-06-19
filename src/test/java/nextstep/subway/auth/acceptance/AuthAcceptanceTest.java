package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("Bearer Auth 인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private TokenRequest 정상요청;
    private TokenRequest 비밀번호_불일치_요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @TestFactory
    Stream<DynamicTest> 인증_관련_기능_시나리오() {
        return Stream.of(
                dynamicTest("로그인을 성공한다.", this::로그인을_성공한다),
                dynamicTest("비밀번호가 일치하지 않으면 로그인에 실패한다.", this::비밀번호가_일치하지_않으면_로그인에_실패한다),
                dynamicTest("유효하지 않은 토큰으로 api를 호출하면 예외가 발생한다", this::유효하지_않은_토큰으로_api를_호출하면_예외가_발생한다)
        );
    }

    @Test
    private void 로그인을_성공한다() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_됨(로그인_요청_응답);
    }

    @Test
    private void 비밀번호가_일치하지_않으면_로그인에_실패한다() {
        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, NEW_PASSWORD);

        // then
        로그인_실패(로그인_요청_응답);
    }

    @Test
    private void 유효하지_않은_토큰으로_api를_호출하면_예외가_발생한다() {
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

    private static void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
    }

    private static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
