package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethod.*;
import static nextstep.subway.member.MemberAcceptanceMethod.*;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String 이메일 = "user@mail.com";
    private static final String 패스워드 = "password";
    private static final int 나이 = 35;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성을_요청(이메일, 패스워드, 나이);
    }

    /**
     * Given 회원 등록되어 있음
     * When 로그인 요청
     * Then 로그인 되고 토큰 발급됨
     */
    @DisplayName("이메일과 패스워드를 이용하여 로그인을 요청하고 토큰을 발급한다.")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(이메일, 패스워드));

        // then
        로그인_성공(response);
        토큰_발급됨(response);
    }

    /**
     * Given 등록되지 않은 회원 정보
     * When 로그인 요청
     * Then 로그인 실패
     */
    @DisplayName("등록되지 않은 회원 정보로 로그인 요청 시 로그인이 실패한다.")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        String 등록되지_않은_이메일 = "bad@mail.com";
        String 등록되지_않은_패스워드 = "bad_password";

        // when
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest(등록되지_않은_이메일, 등록되지_않은_패스워드));

        // then
        로그인_실패(response);
    }

    /**
     * Given 유효하지 않은 토큰
     * When 토큰으로 내 정보 조회 요청
     * Then 정보 조회 실패
     */
    @DisplayName("유효하지 않은 토큰으로 내 정보 조회 요청 시 조회할 수 없다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        String 유효하지_않은_토큰 = "WrongToken";

        // when
        ExtractableResponse<Response> response = 토큰으로_내정보_조회_요청(new TokenResponse(유효하지_않은_토큰));

        // then
        정보_조회_실패(response);
    }
}
