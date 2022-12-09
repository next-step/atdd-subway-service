package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.acceptance.MemberAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthAcceptanceTest extends AcceptanceTest {
    
    private final String EMAIL = "ychxexn@gmail.com";
    private final String PASSWORD = "1234";
    private final Integer AGE = 5;
    
    /**
     * Given 회원이 등록되어 있고
     * When 로그인 요청을 하면
     * Then 토큰을 조회할 수 있다
     */
    @DisplayName("Bearer Auth")
    @Test
    void 토큰_발급() {
        // given
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        
        // when
        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
        
        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    /**
     * Given 회원이 등록되어 있고
     * When 잘못된 로그인 정보로 로그인 요청을 하면
     * Then 토큰을 조회할 수 없다
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void 토큰_발급_실패() {
        // given
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "1");

        // then
        assertEquals(response.statusCode(), HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 등록된 회원으로 로그인을 요청하고
     * When 잘못된 토큰 정보로 정보 조회 요청을 하면
     * Then 정보를 조회할 수 없다
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void 유효하지_않은_토큰으로_정보_조회() {
        // given
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> findResponse = MemberAcceptanceTest.토큰으로_회원_정보_조회_요청("INVALID_TOKEN");

        // then
        assertEquals(findResponse.statusCode(), HttpStatus.UNAUTHORIZED.value());
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
}
