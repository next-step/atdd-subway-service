package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.내정보_수정;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.내정보_수정성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.내정보_조회;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.로그인_실패;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.로그인_요청시도;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.유효하지않은_토큰;
import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.유효한_토큰;
import static nextstep.subway.member.MemberAcceptanceFactory.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String YANG_EMAIL = "rhfpdk92@naver.com";
    private static final String YANG_PASSWORD = "password";
    private static final Integer YANG_AGE = 31;

    @BeforeEach
    void setUp2() {
        회원_생성을_요청(YANG_EMAIL, YANG_PASSWORD, YANG_AGE);
    }

    /**
     * Given 회원 등록되어 있음- setup
     * When 로그인 요청
     * Then 로그인 됨
     */
    @DisplayName("Bearer Auth - 로그인 성공")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> 로그인_요청결과 = 로그인_요청시도(new TokenRequest(YANG_EMAIL, YANG_PASSWORD));

        로그인_성공(로그인_요청결과);
    }

    /**
     * Given 회원 등록되어 있음- setup
     * When 잘못된 패스워드로 로그인 요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth - 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> 로그인_요청결과 = 로그인_요청시도(new TokenRequest(YANG_EMAIL, "INVALID_PASSWORD"));

        로그인_실패(로그인_요청결과);
    }

    /**
     * Given 회원 등록되어 있음- setup
     * When 유효한 토큰으로 확인 요청
     * Then 로그인 성공
     */
    @DisplayName("Bearer Auth - 유효한 토큰")
    @Test
    void myInfoWithValidBearerAuth() {
        TokenResponse 유효한_토큰 = 로그인_요청시도(new TokenRequest(YANG_EMAIL, YANG_PASSWORD)).as(TokenResponse.class);
        ExtractableResponse<Response> 내정보_조회결과 = 내정보_조회(유효한_토큰);

        유효한_토큰(내정보_조회결과);
    }

    /**
     * Given 회원 등록되어 있음- setup
     * When 잘못된 토큰으로 확인 요청
     * Then 로그인 실패
     */
    @DisplayName("Bearer Auth - 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse 유효하지않은_토큰 = 로그인_요청시도(new TokenRequest(YANG_EMAIL, YANG_PASSWORD)).as(TokenResponse.class);
        ExtractableResponse<Response> 토큰_확인_결과 = 내정보_조회(유효하지않은_토큰);

        유효하지않은_토큰(토큰_확인_결과);
    }

    /**
     * Given 회원 등록되어 있음- setup
     * When 유효한 토큰으로 수정 요청
     * Then 수정 성공
     */
    @DisplayName("내정보 수정 - 유효한 토큰")
    @Test
    void updateMyInfoWithValidToken() {
        MemberRequest 수정할_회원정보 = new MemberRequest("update@Email.com", "updatePassword", 100);
        TokenResponse 유효한_토큰 = 로그인_요청시도(new TokenRequest(YANG_EMAIL, YANG_PASSWORD)).as(TokenResponse.class);
        ExtractableResponse<Response> 업데이트_결과 = 내정보_수정(수정할_회원정보, 유효한_토큰);

        내정보_수정성공(업데이트_결과);
    }

}
