package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.step.MemberAcceptanceStep;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 32;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        MemberAcceptanceStep.회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        MemberAcceptanceStep.회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "wrong_password");

        // then
        로그인_됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> createResponse = MemberAcceptanceStep.회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        MemberResponse memberResponse = MemberAcceptanceStep.회원_정보_조회_요청(createResponse).as(MemberResponse.class);
        LoginMember loginMember = new LoginMember(memberResponse.getId(), memberResponse.getEmail(), memberResponse.getAge());

        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청(loginMember, "wrong_token");

        // then
        내_정보_요청_인증_실패됨(response, HttpStatus.UNAUTHORIZED);
    }

    public static ExtractableResponse<Response> 로그인_되어있음(String email, String password) {
        return 로그인_요청(email, password);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 내_정보_조회_요청(LoginMember loginMember, String accessToken) {
        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .body(loginMember)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all().extract();
    }

    private void 내_정보_요청_인증_실패됨(ExtractableResponse<Response> response, HttpStatus unauthorized) {
        Assertions.assertThat(response.statusCode()).isEqualTo(unauthorized.value());
    }
}
