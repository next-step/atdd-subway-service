package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.나의_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {
    /**
     *   Scenario: 로그인을 시도한다.
     *     Given 회원 등록되어 있음
     *     When 로그인 요청
     *     Then 로그인 됨
     */
    @DisplayName("아이디 패스워드로 로그인 요청 후 성공한다.")
    @Test
    void myInfoWithBearerAuth() {
        //Given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //When
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);
        //Then
        로그인_성공(로그인_응답);
    }

    /**
     *     Given 회원 등록되어 있고
     *     When 잘못된 회원 정보로 로그인을 요청하면
     *     Then 로그인이 실패한다.
     */
    @DisplayName("잘못된 패스워드로 로그인 요청을 하면 실패한다.")
    @Test
    void myInfoWithBadBearerAuth() {
        //Given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //When
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, "wrongPassword");
        //Then
        로그인_실패(로그인_응답);
    }

    /**
     *     When 잘못된 토큰으로
     *     Then 로그인이 실패한다.
     */
    @DisplayName("유효하지 않은 토큰으로 로그인 요청을 하면 실패한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> 로그인_응답 = 나의_정보_조회_요청("invalidToken");
        //Then
        로그인_실패(로그인_응답);
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


    public static void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
