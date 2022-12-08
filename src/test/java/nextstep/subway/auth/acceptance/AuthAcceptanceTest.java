package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.내_정보_조회;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.회원_정보_조회_실패;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    MemberRepository memberRepository;

    Member 사용자;

    @BeforeEach
    void before() {
        super.setUp();
        사용자 = memberRepository.save(new Member("test@test.com", "password", Age. from(29)));
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = 인증_요청(new TokenRequest(사용자.getEmail(), 사용자.getPassword()));
        // then
        인증_성공(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 존재하지 않는 유저")
    @Test
    void myInfoWithBadBearerAuthEmail() {
        // given
        Member 존재하지_않는_유저 = new Member("notExist@notExist.com", "password", Age.from(29));
        // when
        ExtractableResponse<Response> response = 인증_요청(
                new TokenRequest(존재하지_않는_유저.getEmail(), 존재하지_않는_유저.getPassword()));
        // then
        인증_실패(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 패스워드 불일치")
    @Test
    void myInfoWithBadBearerAuthPassword() {
        // given
        String 틀린_패스워드 = "incorrect";
        // when
        ExtractableResponse<Response> response = 인증_요청(new TokenRequest(사용자.getEmail(), 틀린_패스워드));
        // then
        인증_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = 인증_요청(new TokenRequest(사용자.getEmail(), 사용자.getPassword()));
        // then
        인증_성공(response);

        //given
        String 잘못된_토큰 = "wrong token";
        // when
        ExtractableResponse<Response> getResponse = 내_정보_조회(잘못된_토큰);
        // then
        회원_정보_조회_실패(getResponse);
    }

    public static ExtractableResponse<Response> 인증_요청(TokenRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static TokenRequest 인증_요청_데이터_생성(String email, String password) {
        return new TokenRequest(email, password);
    }

    public static void 인증_성공(ExtractableResponse<Response> response) {
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotNull();
    }

    private void 인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
