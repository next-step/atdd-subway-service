package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청_및_검증;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.*;

class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> response = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(response);
    }

    @TestFactory
    @DisplayName("로그인을 시도한다")
    Stream<DynamicTest> 로그인을_시도한다() {
        return Stream.of(
                dynamicTest("로그인을 요청 및 검증한다", 로그인_요청_및_검증(new TokenRequest(EMAIL, PASSWORD)))
        );
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }


}
