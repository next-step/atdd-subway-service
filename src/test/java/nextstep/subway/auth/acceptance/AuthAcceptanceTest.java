package nextstep.subway.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.MemberAcceptanceTestSupport;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.로그인_되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestSupport.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("인증을 통한 로그인 기능 테스트")
    void login() {
        // given
        // 회원 등록되어 있음
        회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD
                , MemberAcceptanceTest.AGE);
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);

        // when
        // 로그인 요청
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        // then
        // 로그인 됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    @DisplayName("토큰을 이용하여 내 정보 조회")
    @Test
    void myInfoWithBearerAuth() {
        // given
        // 회원 로그인 되어 있음
        회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD
                , MemberAcceptanceTest.AGE);
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        String accessToken = 로그인_요청(tokenRequest)
                .as(TokenResponse.class).getAccessToken();

        // when
        // 토큰으로 내 정보 요청
        ExtractableResponse<Response> myInfoResponse = MemberAcceptanceTestSupport.나의_정보_요청(accessToken);
        MemberResponse memberResponse = myInfoResponse.as(MemberResponse.class);

        // then
        // 내 정보 조회 됨
        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(tokenRequest.getEmail());
    }

    @DisplayName("잘못된 비밀번로로 로그인 시도 할 경우 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        // 회원 등록되어 있음
        회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD
                , MemberAcceptanceTest.AGE);

        // when
        // 잘못된 비밀번호로 로그인 요청
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.NEW_PASSWORD);
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        // then
        // 로그인 인증 안됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("유효하지 않은 토큰으로 내정보 조회 시 오류")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        // 회원 로그인 되어 있음
        String accessToken = 로그인_되어_있음();

        // when
        // 잘못된 토큰으로 나의 정보 요청
        String wrongAccessToken = accessToken + "test";
        ExtractableResponse<Response> myInfoResponse = MemberAcceptanceTestSupport.나의_정보_요청(wrongAccessToken);

        // then
        // 내 정보 조회 안됨
        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
