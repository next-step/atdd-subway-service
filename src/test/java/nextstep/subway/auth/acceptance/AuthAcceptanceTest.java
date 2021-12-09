package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.acceptance.MemberTestApi.회원_등록되어_있음;
import static nextstep.subway.member.acceptance.MyMemberTestApi.내_정보_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    MemberRequest 비회원;

    @BeforeEach
    public void setUp() {
        super.setUp();
        비회원 = new MemberRequest("doyoung@email.com", "doyoung-passrod", 21);
    }

    @DisplayName("Bearer Auth: 로그인을 시도한다.")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_등록되어_있음(비회원.getEmail(), 비회원.getPassword(), 비회원.getAge());
        final MemberRequest 회원가입자 = 비회원;
        // when
        ExtractableResponse<Response> response = 로그인_요청(회원가입자.getEmail(), 회원가입자.getPassword());
        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(비회원.getEmail(), 비회원.getPassword());
        // then
        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String 유효하지않은_토큰 = "유효하지않은토큰이라눙!";
        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청(유효하지않은_토큰);
        // then
        토큰_인증_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        final TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static TokenResponse 회원_등록_후_로그인_되어_있음(String email, String password, int age) {
        회원_등록되어_있음(email, password, age);
        return 로그인_되어_있음(email, password);
    }

    public static TokenResponse 로그인_되어_있음(String email, String password) {
        return 로그인_요청(email, password).as(TokenResponse.class);
    }

    private void 토큰_인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
    }
}
