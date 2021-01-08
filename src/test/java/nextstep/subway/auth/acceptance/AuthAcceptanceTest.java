package nextstep.subway.auth.acceptance;

import io.jsonwebtoken.Jwts;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.MemberAcceptanceTest.토큰으로_개인정보_조회;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인, 인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private String 이메일;
    private String 비밀번호;
    private Integer 나이;

    @BeforeEach
    void setUpSignUp() {
        이메일 = "jaenyeong.dev@gmail.com";
        비밀번호 = "1234";
        나이 = 31;

        회원_생성을_요청(이메일, 비밀번호, 나이);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        final ExtractableResponse<Response> 응답 = 로그인_요청(이메일, 비밀번호);
        final String 토큰 = 응답.as(TokenResponse.class).getAccessToken();
        final String 토큰_내_이메일 = getUserEmail(토큰);
        final ExtractableResponse<Response> 토큰_요청_응답 = 토큰으로_개인정보_조회(토큰);

        assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(토큰).isNotNull();
        assertThat(토큰_내_이메일).isEqualTo(이메일);
        assertThat(토큰_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private String getUserEmail(final String token) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private static ExtractableResponse<Response> POST_요청_보내기(final Map<String, String> 전달_파라미터, final String 요청_URI) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(전달_파라미터)
            .when().post(요청_URI)
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        final Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return POST_요청_보내기(params, "/login/token");
    }

    @DisplayName("Bearer Auth 로그인시 잘못된 비밀번호 입력으로 로그인 실패")
    @Test
    void myInfoWithBadBearerAuthByPassword() {
        final ExtractableResponse<Response> 응답 = 로그인_요청(이메일, "틀린 비밀번호");
        권한없음_확인됨(응답);
    }

    private void 권한없음_확인됨(final ExtractableResponse<Response> 응답) {
        assertThat(응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 로그인시 잘못된 이메일 입력으로 로그인 실패")
    @Test
    void myInfoWithBadBearerAuthByEmail() {
        final ExtractableResponse<Response> 응답 = 로그인_요청("틀린 이메일", 비밀번호);
        권한없음_확인됨(응답);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        final String 틀린_토큰 = "올바르지 못한 토큰입니다.";

        final ExtractableResponse<Response> 응답 = 토큰으로_개인정보_조회(틀린_토큰);
        권한없음_확인됨(응답);
    }
}

