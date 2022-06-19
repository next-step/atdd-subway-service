package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.factory.AuthAcceptanceFactory.*;
import static nextstep.subway.member.factory.MemberAcceptanceFactory.내_정보_조회_요청;
import static nextstep.subway.member.factory.MemberAcceptanceFactory.회원_등록되어_있음;

public class AuthAcceptanceTest extends AcceptanceTest {
    private String 이메일;
    private String 패스워드;
    private int 나이;

    @BeforeEach
    public void setUp() {
        super.setUp();

        이메일 = "14km@github.com";
        패스워드 = "a1s2d3f4!@#$";
        나이 = 20;

        회원_등록되어_있음(이메일, 패스워드, 나이);
    }

    @Test
    @DisplayName("로그인 요청 정상 응답 테스트")
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(이메일, 패스워드);

        로그인_성공됨(로그인_요청_결과);
    }

    @Test
    @DisplayName("로그인 요청 잘못된 패스워드 실패 테스트")
    void myInfoWithBadBearerAuth() {
        String 잘못된_패스워드 = "1234";

        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(이메일, 잘못된_패스워드);

        로그인_실패됨(로그인_요청_결과);
    }

    @DisplayName("유효하지 않은 Bearer Auth 토큰으로 정보 조회 실패 테스트")
    @Test
    void myInfoWithWrongBearerAuth() {
        String 잘못된_토큰 = "accessToken";

        ExtractableResponse<Response> 내_정보_조회_요청_결과 = 내_정보_조회_요청(잘못된_토큰);

        로그인_실패됨(내_정보_조회_요청_결과);
    }
}
