package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.*;
import static nextstep.subway.member.MemberAcceptanceStep.*;

@DisplayName("로그인 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@nextstep.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 30;

    @DisplayName("로그인을 시도한다.")
    @Test
    void login() {
        // Given 회원 등록되어 있음
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        // When 등록된 이메일이 아닌 다른 이메일로 요청
        ExtractableResponse<Response> 로그인_요청_실패_결과1 = 로그인_요청("bad_email", PASSWORD);
        // Then 로그인 실패됨
        로그인_이메일_실패됨(로그인_요청_실패_결과1);

        // When 올바른 비밀번호가 아닌 것으로 요청
        ExtractableResponse<Response> 로그인_요청_실패_결과2 = 로그인_요청(EMAIL, "bad_password");
        // Then 로그인 실패됨
        로그인_비밀번호_실패됨(로그인_요청_실패_결과2);

        // When 로그인 요청
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(EMAIL, PASSWORD);
        // Then 로그인 됨
        로그인_응답됨(로그인_요청_결과);
        로그인_됨(로그인_요청_결과);

        // When 유효하지 않은 토큰으로 정보 조회
        ExtractableResponse<Response> 나의_정보_조회_요청_실패_결과 = 나의_정보_조회_요청("not_valid_token");
        // Then 토큰 검증 실패됨
        토큰_검증_실패됨(나의_정보_조회_요청_실패_결과);

        // When 유효한 토큰으로 정보 조회
        ExtractableResponse<Response> 나의_정보_조회_요청_결과 = 나의_정보_조회_요청(로그인_요청_결과.as(TokenResponse.class).getAccessToken());
        // Then 토큰 검증됨
        나의_정보_응답됨(나의_정보_조회_요청_결과);
        나의_정보_확인(나의_정보_조회_요청_결과, EMAIL, AGE);
    }
}
