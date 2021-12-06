package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 토큰 발급 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("이메일과 암호로 로그인을 한다")
    @Test
    void myInfoWithBearerAuth() {
        // given
        String password = "1234";
        MemberResponse 이수현 = MemberAcceptanceTest.회원_등록되어_있음("suhyun@email.com", password, 22);

        // when
        ExtractableResponse<Response> response = 로그인_요청(이수현.getEmail(), password);

        // then
        로그인_됨(response);
    }

    @DisplayName("잘못된 이메일과 암호를 입력하면 로그인에 실패한다")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String password = "1234";
        MemberResponse 이수현 = MemberAcceptanceTest.회원_등록되어_있음("suhyun@email.com", password, 22);

        // when - 틀린 비밀번호
        ExtractableResponse<Response> response = 로그인_요청(이수현.getEmail(), "wrong_password");

        // then
        로그인_실패(response);

        // when - 틀린 이메일
        response = 로그인_요청("wrong_email", password);

        // then
        로그인_실패(response);
    }

    @DisplayName("유효하지 않은 토큰을 검사한다")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        로그인_되어_있음("suhyun@email.com", "1234", 22);

        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청("wrong_token");

        // then
        내_정보_조회_실패(response);
    }

    private ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + accessToken)
                .when().get("/members/me")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all().extract();
    }

    private TokenResponse 로그인_되어_있음(String email, String password, Integer age) {
        MemberResponse user = MemberAcceptanceTest.회원_등록되어_있음(email, password, age);
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return 로그인_됨(response);
    }

    private TokenResponse 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse tokenResponse = response.jsonPath().getObject(".", TokenResponse.class);
        ExtractableResponse<Response> myInfoResponse = 내_정보_조회_요청(tokenResponse.getAccessToken());
        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        return tokenResponse;
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 내_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
