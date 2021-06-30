package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.member.MemberAcceptanceTest;
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
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.common.error.ErrorResponse;
import nextstep.subway.member.dto.MemberRequest;

public class AuthAcceptanceTest extends AcceptanceTest {

    private MemberRequest memberRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        memberRequest = new MemberRequest("hi@gmail.com", "1234", 37);
    }

    /**
     * Feature: 로그인 기능
     *
     * Scenario: 로그인을 시도한다.
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원이_등록되어_있음(memberRequest);

        // when
        ExtractableResponse<Response> 로그인_요청결과 = 로그인_요청(new TokenRequest(memberRequest.getEmail(), memberRequest.getPassword()));

        // then
        로그인_확인됨(로그인_요청결과);
    }

    /**
     * Feature: 로그인 실패
     *
     * Scenario: 잘못된 정보로 로그인 시도시 실패한다.
     * Given 회원 등록되어 있음
     * When 잘못된 비밀번호로 로그인 요청
     * Then 로그인 실패됨
     * And 요청 실패메시지 확인됨
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        회원이_등록되어_있음(memberRequest);
        TokenRequest 잘못된_비밀번호_요청 = new TokenRequest(memberRequest.getEmail(), memberRequest.getPassword() + "a");

        // when
        ExtractableResponse<Response> 잘못된_비밀번호_로그인_요청결과 = 잘못된_정보로_로그인_요청(잘못된_비밀번호_요청);

        // then
        로그인_실패됨(잘못된_비밀번호_로그인_요청결과);
        오류_메시지_확인됨(잘못된_비밀번호_로그인_요청결과, "비밀번호가 맞지 않습니다.");
    }

    /**
     * Feature: 유효하지 않은 토큰
     *
     * Scenario: 유효하지 않은 토큰으로 사용자 정보 조회시 실패한다.
     * Given 회원 등록되어 있음
     * And 로그인 되어 있음
     * When 유효하지 않은 토큰으로 사용자 정보조회 요청
     * Then 사용자 정보조회 실패
     * And 요청 실패메시지 확인됨
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        회원이_등록되어_있음(memberRequest);
        String token = 로그인_되어_있음(new TokenRequest(memberRequest.getEmail(), memberRequest.getPassword()))
                .as(TokenResponse.class)
                .getAccessToken();

        // when
        ExtractableResponse<Response> 사용자_정보조회_결과 = 유효하지_않은_토큰으로_사용자_정보조회_요청(token + "a",
                new MemberRequest(memberRequest.getEmail(), memberRequest.getPassword(), 33));

        // then
        유효하지_않은_토큰으로_사용자_정보조회_요청실패(사용자_정보조회_결과);
        오류_메시지_확인됨(사용자_정보조회_결과, "유효한 토큰이 아닙니다.");
    }

    private ExtractableResponse<Response> 유효하지_않은_토큰으로_사용자_정보조회_요청(String token, MemberRequest memberRequest) {
        String headerValue = AuthorizationExtractor.BEARER_TYPE + " " + token;
        return RestAssured.given().log().all()
                .header(AuthorizationExtractor.AUTHORIZATION, headerValue)
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_되어_있음(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원이_등록되어_있음(MemberRequest memberRequest) {
        return MemberAcceptanceTest.회원_생성을_요청(memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge());
    }

    private ExtractableResponse<Response> 잘못된_정보로_로그인_요청(TokenRequest tokenRequest) {
        return 로그인_요청(tokenRequest);
    }

    private void 유효하지_않은_토큰으로_사용자_정보조회_요청실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 오류_메시지_확인됨(ExtractableResponse<Response> response, String errorMessage) {
        assertThat(response.as(ErrorResponse.class).getErrorMessage()).isEqualTo(errorMessage);
    }

    private void 로그인_확인됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(TokenResponse.class).getAccessToken()).isNotBlank()
        );
    }
}
