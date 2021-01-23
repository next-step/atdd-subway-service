package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberRequest member = new MemberRequest("test1@test.com", "1111", 40);
        RestAssured
                .given()
                .body(member)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/members")
                .then()
                .extract();
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        TokenRequest params = new TokenRequest("test1@test.com", "1111");

        ExtractableResponse<Response> response = 로그인_토큰_생성(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }



    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        TokenRequest params = new TokenRequest("test1@test.com", "2222");

        ExtractableResponse<Response> response = 로그인_토큰_생성(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        String token = "erdfdf";

        LoginMember member = 로그인_토큰_유효체크(token);

        assertThat(member.getEmail()).isNullOrEmpty();

        ExtractableResponse<Response> response = RestAssured
                            .given()
                            .body(member)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when()
                            .get("/members/me")
                            .then()
                            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 로그인_토큰_생성(TokenRequest params) {
        return RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/login/token")
                .then()
                .extract();
    }

    public static LoginMember 로그인_토큰_유효체크(String token) {
        return RestAssured
                .given()
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/login/token")
                .then()
                .extract()
                .as(LoginMember.class);
    }

}
