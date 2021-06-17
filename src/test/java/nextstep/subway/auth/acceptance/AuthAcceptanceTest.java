package nextstep.subway.auth.acceptance;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Date;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인을 시도한다")
    @Test
    void manageMyInfo() {
        //given 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        //when 등록된 Email과 Password로 로그인
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청(EMAIL, PASSWORD);

        //then 로그인 성공
        로그인_됨(로그인_응답);

        //when 등록된 회원정보와 다른 값으로 로그인
        ExtractableResponse<Response> 잘못된_로그인_응답 = 로그인_요청(EMAIL, "invalidPassword");

        //then
        로그인_실패(잘못된_로그인_응답);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //given 등록된 Email과 Password로 로그인
        TokenResponse 로그인_응답 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        //when 발급된 토큰으로 내 정보 조회
        ExtractableResponse<Response> 내_정보 = 내_정보_조회(로그인_응답.getAccessToken());

        내_정보_조회됨(내_정보);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //given 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //given 등록된 Email과 Password로 로그인
        로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        //when 잘못된 토큰으로 로그인 시도
        ExtractableResponse<Response> 잘못된_조회_응답 = 내_정보_조회("BadBearerToken");

        내_정보_조회_실패(잘못된_조회_응답);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //given 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //given 등록된 Email과 Password로 로그인
        로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
        String 유효시간_지난_토큰 = 유효시간_지난_토큰_생성();

        //when 유효하지 않은 토큰으로 로그인 시도
        ExtractableResponse<Response> 유효하지_않은_토큰_조회 = 내_정보_조회(유효시간_지난_토큰);

        내_정보_조회_실패(유효하지_않은_토큰_조회);
    }

    public static void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        TokenResponse 로그인_응답 = response.as(TokenResponse.class);
        assertThat(로그인_응답.getAccessToken()).isNotNull();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new TokenRequest(email, password))
            .when()
            .post("/login/token")
            .then().log().all()
            .extract();
    }

    private void 내_정보_조회됨(ExtractableResponse<Response> response) {
        MemberResponse 내_정보 = response.as(MemberResponse.class);
        assertThat(내_정보.getEmail()).isEqualTo(EMAIL);
        assertThat(내_정보.getAge()).isEqualTo(AGE);
    }

    private ExtractableResponse<Response> 내_정보_조회(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/members/me")
            .then().log().all()
            .extract();
    }

    private void 내_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private String 유효시간_지난_토큰_생성() {
        Claims claims = Jwts.claims().setSubject(EMAIL);
        Date oneHourBefore = new Date(new Date().getTime() - 3600000);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(oneHourBefore)
            .setExpiration(oneHourBefore)
            .signWith(SignatureAlgorithm.HS256, "secretKey")
            .compact();
    }
}
