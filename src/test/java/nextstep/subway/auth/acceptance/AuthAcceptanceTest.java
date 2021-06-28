package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.비회원;
import static nextstep.subway.member.MemberAcceptanceTest.성인_회원;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptancePerClassTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptancePerClassTest {

    @BeforeAll
    void setup() {
        회원_생성됨(회원_생성을_요청(성인_회원));
    }

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() {
        // When
        ExtractableResponse<Response> response = 로그인_요청(성인_회원);

        // Then
        로그인_성공함(response);
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFail() {
        // When
        ExtractableResponse<Response> response = 로그인_요청(비회원);

        // Then
        로그인_실패함(response);
    }

    @DisplayName("토큰기반인증(Bearer Auth) 성공")
    @Test
    void myInfoWithBearerAuth() {
        // Given
        TokenResponse tokenResponse = 로그인_요청(성인_회원).as(TokenResponse.class);

        // When
        ExtractableResponse<Response> response = 토큰으로_나의_회원정보_조회_요청(tokenResponse.getAccessToken());

        // Then
        나의_회원정보_조회_성공함(response);
    }

    @DisplayName("토큰기반인증(Bearer Auth) 실패")
    @Test
    void myInfoWithWrongBearerAuth() {
        // When
        ExtractableResponse<Response> response = 토큰으로_나의_회원정보_조회_요청("유효하지않은_토큰");

        // Then
        나의_회원정보_조회_실패함(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(MemberRequest memberRequest) {
        return post(new TokenRequest(memberRequest.getEmail(), memberRequest.getPassword()), "/login/token");
    }

    public static TokenResponse 로그인_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotEmpty();
        return response.as(TokenResponse.class);
    }

    public static void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 토큰으로_나의_회원정보_조회_요청(String accessToken) {
        return get("/members/me", accessToken);
    }

    public static void 나의_회원정보_조회_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(성인_회원.getEmail());
    }

    private static void 나의_회원정보_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
