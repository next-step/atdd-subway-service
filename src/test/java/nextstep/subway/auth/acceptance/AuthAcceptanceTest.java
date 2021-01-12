package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String email = "wootecam@test.com";
        String password = "1a2b3c";
        int age = 20;

        // given
        회원_등록되어_있음(email, password, age);

        // when
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(email, password);

        // then
        로그인_성공함(로그인_요청_결과);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청("test@test.com", "1a2s3d4f");

        // then
        로그인_실패함(로그인_요청_결과);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> 회원_기능_요청_결과 = 회원_기능_요청("testToken");

        // then
        회원_인증_실패함(회원_기능_요청_결과);
    }

    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, int age) {
        MemberRequest memberRequest = MemberRequest.builder()
                .email(email)
                .password(password)
                .age(age)
                .build();

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = TokenRequest.builder()
                .email(email)
                .password(password)
                .build();

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static void 로그인_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getString("accessToken")).isNotNull();
    }

    public static void 로그인_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 회원_기능_요청(String accessToken) {
        return MemberAcceptanceTest.내_정보_조회_요청(accessToken);
    }

    public static void 회원_인증_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
