package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.나의_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private String email = "7271kim@naver.com";
    private String password = "password";
    private int age = 20;
    private ExtractableResponse<Response> 김석진;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        //given 회원등록이 되어있다.
        김석진 = 회원_생성을_요청(email, password, age);
    }

    @DisplayName("로그인을 시도한다. - 성공")
    @Test
    void myInfoWithBearerAuth() {
        //when 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        //Then 로그인 됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();

        //when 로그인된 토큰으로 나의 정보를 요청한다.
        TokenResponse tokenResponse = new TokenResponse(response.jsonPath().getString("accessToken"));
        response = 나의_정보_조회_요청(tokenResponse);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(MemberResponse.class).getEmail()).isEqualTo(email);

    }

    @DisplayName("로그인을 시도한다. - 실패")
    @Test
    void fail() {
        //given
        //유효하지 않은 토큰
        TokenResponse tokenResponse = new TokenResponse("no");

        //when 잘못된 ID, PW로 로그인을 요청한다.
        ExtractableResponse<Response> response = 로그인_요청("none", "none");

        //then 로그인 실패한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        //when 잘못된 토큰으로 정보를 조회한다
        response = 나의_정보_조회_요청(tokenResponse);

        //then 실패한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new TokenRequest(email, password))
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }
}
