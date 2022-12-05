package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private final static String email = "eunveloper@gmail.com";
    private final static String password = "1234";
    private final static String failPassword = "12345";

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(email, password, 28);
        MemberAcceptanceTest.회원_생성됨(createResponse);
    }

    @DisplayName("정상 로그인 시도")
    @Test
    void myInfoWithBearerAuth() {
        // When 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        // Then 로그인 됨
        로그인_성공함(response);
    }

    @DisplayName("비정상 로그인 시도")
    @Test
    void myInfoWithBadBearerAuth() {
        // When 잘못된 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(email, failPassword);

        // Then 로그인 되지 않음
        로그인_실패함(response);

    }

    @DisplayName("유효한 토큰으로 정보 요청")
    @Test
    void myInfoWithRightBearerAuth() {
        // Given 로그인 요청 후 토큰 발급
        String accessToken = 로그인_요청(email, password).as(TokenResponse.class).getAccessToken();

        // When 유효한 토큰으로 정보 조회 요청
        ExtractableResponse<Response> response = 내정보_요청(accessToken);

        // Then 요청이 성공
        내정보_요청_성공함(response);
    }

    @DisplayName("유효하지 않은 토큰으로 정보 요청")
    @Test
    void myInfoWithWrongBearerAuth() {
        // When 잘못된 토큰으로 정보 조회 요청
        String accessToken = "failToken";
        ExtractableResponse<Response> response = 내정보_요청(accessToken);

        // Then 요청이 실패
        내정보_요청_실패함(response);
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest request = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_성공함(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 내정보_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private void 내정보_요청_성공함(ExtractableResponse<Response> response) {
        MemberResponse memberResponse = response.as(MemberResponse.class);

        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 내정보_요청_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
