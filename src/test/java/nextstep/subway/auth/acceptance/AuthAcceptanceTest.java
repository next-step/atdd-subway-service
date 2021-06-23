package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private String email;
    private String password;
    private TokenRequest request;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        email = "joenggyu0@gmail.com";
        password = "1234";
        회원_등록이_완료됨(회원_등록을_요청함(new MemberRequest(email, password, 30)));
        request = new TokenRequest(email, password);
    }

    @DisplayName("로그인을 시도")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(request);
        로그인_됨(response);

    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(request);
        로그인_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        LoginMember member = new LoginMember(1L, "joenggyu0@gmail.com", 30);
        ExtractableResponse<Response> response = 유효한_토큰_테스트(member);
        로그인_실패됨(response);

    }

    private void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 회원_등록이_완료됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 로그인_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        response.body().jsonPath().getObject(".", TokenResponse.class);
    }

    private ExtractableResponse<Response> 유효한_토큰_테스트(LoginMember request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members/me")
                .then().log().all().
                        extract();

    }

    private ExtractableResponse<Response> 회원_등록을_요청함(MemberRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/member")
                .then().log().all().
                        extract();
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all().
                        extract();
    }
}
