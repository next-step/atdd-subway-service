package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthAcceptanceTest extends AcceptanceTest {

    private String email = "cyr9210@nate.com";
    private String password = "cyr12345";

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        memberRepository.save(new Member(email, password, 31));
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        TokenRequest tokenRequest = new TokenRequest(email, password);
        //when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        //then
        로그인_완료됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 비밀번호 불일치")
    @Test
    void myInfoWithBadBearerAuth_invalidPassword() {
        //given
        TokenRequest tokenRequest = new TokenRequest(email, "badPassword");
        //when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        //then
        로그인_실패함(response);
    }

    @DisplayName("Bearer Auth 로그인 실패 - 존재하지 않는 이메일")
    @Test
    void myInfoWithBadBearerAuth_notExistsEmail() {
        //given
        TokenRequest tokenRequest = new TokenRequest("cyr9210@gmail.com", password);
        //when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        //then
        로그인_실패함(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> response = MemberAcceptanceTest.내_정보_조회_요청("bad_token");
        //then
        로그인_실패함(response);
    }

    public static String 로그인_토큰_얻기(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        return response.body().jsonPath().getString("accessToken");
    }

    private static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_완료됨(ExtractableResponse<Response> response) {
        String responseToken = response.body().jsonPath().getString("accessToken");
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.statusCode()),
                () -> assertThat(responseToken).isNotBlank()
        );
    }

    private void 로그인_실패함(ExtractableResponse<Response> response) {
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());
    }

}
