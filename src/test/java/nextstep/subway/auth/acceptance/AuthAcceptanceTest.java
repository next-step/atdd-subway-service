package nextstep.subway.auth.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.로그인_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.잘못된_토큰_요청_응답;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.정상적인_토큰_요청_응답;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.토큰과_함께_요청_시도;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestActions.회원등록_되어있음;
import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AuthAcceptanceTest extends AcceptanceTest {

    /**
     * <p>Feature: 로그인 기능
     * <p>  Background
     * <p>    Given 회원 등록되어 있음
     *
     * <p>  Scenario: 로그인을 성공한다
     * <p>    When 정상적인 아이디 패스워드로 로그인 요청
     * <p>    Then 로그인 성공
     *
     * <p>  Scenario: 로그인을 실패한다
     * <p>    When 잘못된 아이디 패스워드로 로그인 요청
     * <p>    Then 로그인 실패
     *
     * <p>  Scenario: 인증에 성공한다
     * <p>    Given 로그인 성공 및 토큰 발급완료
     * <p>    When 정상 토큰으로 요청을 시도
     * <p>    Then 정상 토큰으로 응답 받음
     *
     * <p>  Scenario: 인증에 실패한다
     * <p>    Given 로그인 성공 및 토큰 발급완료
     * <p>    When 잘못된 토큰으로 요청을 시도
     * <p>    Then 잘못된 토큰으로 응답 거부
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        회원등록_되어있음(EMAIL, PASSWORD, AGE);
    }


    /**
     * Given 회원등록이 되어 있다
     * <p>
     * When 정상적인 아이디 패스워드로 로그인 요청을 시도하면
     * <p>
     * Then 로그인에 성공한다
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        //then
        로그인_성공(response);
    }

    /**
     * Given 회원등록이 되어 있다
     * <p>
     * When 잘못된 아이디 패스워드로 로그인 요청을 시도하면
     * <p>
     * Then 로그인에 실패한다
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "xxxx");

        //then
        로그인_실패(response);
    }

    /**
     * Given 로그인 성공 및 토큰 발급이 완료되어있다
     * <p>
     * When 정상 토큰으로 회원정보 요청을 시도하면
     * <p>
     * Then 정상 토큰으로 응답을 받는다
     */
    @DisplayName("Bearer Auth 유효한 토큰")
    @Test
    void myInfoWithRightBearerAuth() {
        //given
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        String accessToken = loginResponse.jsonPath().getString("accessToken");

        //when
        ExtractableResponse<Response> accessTokenResponse = 토큰과_함께_요청_시도(accessToken);

        //then
        정상적인_토큰_요청_응답(accessTokenResponse, HttpStatus.OK.value(), EMAIL, AGE);
    }

    /**
     * Given 로그인 성공 및 토큰 발급이 완료되어있다
     * <p>
     * When 잘못된 토큰으로 회원정보 요청을 시도하면
     * <p>
     * Then 잘못된 토큰으로 응답을 받지 못한다
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given
        로그인_요청(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> accessTokenResponse = 토큰과_함께_요청_시도("xxxx");

        //then
        잘못된_토큰_요청_응답(accessTokenResponse, HttpStatus.BAD_REQUEST.value());
    }


}
