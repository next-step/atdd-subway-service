package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void before() {
        //given: 회원 등록 되어 있음
        super.setUp();
        ExtractableResponse<Response> 회원_생성_요청 = 회원_생성을_요청("toughchb@gmail.com", "password", 18);
        회원_생성됨(회원_생성_요청);
    }

    @DisplayName("Bearer Auth 정상 로그인")
    @Test
    void myInfoWithBearerAuth() {
        TokenRequest tokenRequest = new TokenRequest("toughchb@gmail.com", "password");

        //when: 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        //then: 로그인 됨
        로그인_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        TokenRequest tokenRequest = new TokenRequest("toughchb@gmail.com", "wrong_password");

        //when: 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        //then: 로그인 실패
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(tokenRequest).
                when().post("/login/token").
                then().log().all().
                extract();
    }
}
