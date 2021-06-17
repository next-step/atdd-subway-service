package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인을 시도한다")
    @Test
    void manageMyInfo() {
        //given 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        //when 등록된 Email과 Password로 로그인
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        //then 로그인 성공
        로그인_됨(로그인_응답);

        //when 등록된 회원정보와 다른 값으로 로그인
        ExtractableResponse<Response> 잘못된_로그인_응답 = 로그인_요청(EMAIL, "invalidPassword");

        //then
        로그인_실패(잘못된_로그인_응답);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //given 등록된 Email과 Password로 로그인
        TokenResponse 로그인_응답 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        //when 발급된 토큰으로 내 정보 조회
        ExtractableResponse<Response> 내_정보 = 내_정보_조회(로그인_응답);

        내_정보_조회됨(내_정보);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        TokenResponse 로그인_응답 = response.as(TokenResponse.class);
        assertThat(로그인_응답.getAccessToken()).isNotNull();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new TokenRequest(email, password))
            .when()
            .post("/login/token")
            .then().log().all()
            .extract();
    }

    private void 내_정보_조회됨(ExtractableResponse<Response> response) {
        MemberResponse 내_정보 = response.as(MemberResponse.class);
        assertThat(내_정보.getEmail()).isEqualTo(EMAIL);
        assertThat(내_정보.getAge()).isEqualTo(AGE);
    }

    private ExtractableResponse<Response> 내_정보_조회(TokenResponse token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when()
            .get("/members/me")
            .then().log().all()
            .extract();
    }
}
