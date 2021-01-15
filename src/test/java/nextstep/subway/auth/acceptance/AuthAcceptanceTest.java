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

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("인증을 통한 로그인 기능 테스트")
    void login() {
        // given
        // 회원 등록되어 있음
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.NEW_PASSWORD
                , MemberAcceptanceTest.AGE);
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.NEW_PASSWORD);

        // when
        // 로그인 요청
        ExtractableResponse<Response> response = AuthAcceptanceTestSupport.로그인_요청(tokenRequest);
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
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.NEW_PASSWORD
                , MemberAcceptanceTest.AGE);
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.NEW_PASSWORD);
        String accessToken = AuthAcceptanceTestSupport.로그인_요청(tokenRequest)
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

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

}
