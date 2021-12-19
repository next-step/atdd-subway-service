package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private MemberRequest 회원;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원 = new MemberRequest("jdragon@woo.com", "12345", 20);
        회원등록됨(회원);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(회원);

        로그인_성공(response);
        TokenResponse tokenResponse = 토큰_생성됨(response);

        ExtractableResponse<Response> myInfoResponse = 내_정보_조회_요청(tokenResponse.getAccessToken());

        내_정보_조회_성공(myInfoResponse);
        내_정보_조회_결과_확인(myInfoResponse, 회원);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // 회원 등록 되어 있음
        MemberRequest memberRequest = new MemberRequest("pobi@woo.com", "123", 20);

        ExtractableResponse<Response> response = 로그인_요청(memberRequest);

        로그인_실패(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        ExtractableResponse<Response> response = 로그인_요청(회원);

        로그인_성공(response);
        TokenResponse tokenResponse = 토큰_생성됨(response);

        String inValidToken = "ABCDEDAFAAC1NiJ9.eyJzdWIiOiJqZHJhZ29uQHdvby5jb20iLCJpYXQiOjE2Mzk4OTEyMDUsImV4cCI6MTYzOTg5NDgwNX0.Vs0FFJSQS_7o8WHbhq47W7gxTPZiqn7-YAhr4xdT7RA";
        ExtractableResponse<Response> myInfoResponse = 내_정보_조회_요청(inValidToken);

        유효하지_않은_토큰_내_정보_조회_실패(myInfoResponse);
    }

    private void 유효하지_않은_토큰_내_정보_조회_실패(ExtractableResponse<Response> myInfoResponse) {
        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(myInfoResponse.jsonPath().getObject("message", String.class)).isEqualTo("유효하지 않은 토큰입니다");
    }

    private void 회원등록됨(MemberRequest 회원) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(회원)
                .when().post("/members")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private TokenResponse 토큰_생성됨(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse).isNotNull();
        return tokenResponse;
    }

    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 로그인_요청(MemberRequest memberRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("email", memberRequest.getEmail());
        params.put("password", memberRequest.getPassword());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 내_정보_조회_결과_확인(ExtractableResponse<Response> myInfoResponse, MemberRequest member) {
        MemberResponse memberResponse = myInfoResponse.as(MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
        assertThat(memberResponse.getAge()).isEqualTo(member.getAge());
    }

    private void 내_정보_조회_성공(ExtractableResponse<Response> myInfoResponse) {
        assertThat(myInfoResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public ExtractableResponse<Response> 내_정보_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

}
