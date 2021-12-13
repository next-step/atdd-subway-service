package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;

@DisplayName("인증테스트")
public class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "jennie267@email.com";
    public static final String PASSWORD = "jenniepw";
    public static final int AGE = 20;
    
    @BeforeEach
    public void setUp() {
        super.setUp();
        MemberAcceptanceTest.회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("로그인 기능 시나리오 통합 테스트")
    @Test
    void 로그인_기능_통합_테스트() {
        // When
        ExtractableResponse<Response> 잘못된_이메일_로그인  = 로그인_요청("fail@email.com", PASSWORD);

        // Then
        로그인_실패(잘못된_이메일_로그인);
        
        // When
        ExtractableResponse<Response> 잘못된_비밀번호_로그인  = 로그인_요청(EMAIL, "failpw");

        // Then
        로그인_실패(잘못된_비밀번호_로그인);
        
        // When
        ExtractableResponse<Response> 올바른_로그인  = 로그인_요청(EMAIL, PASSWORD);
        
        // Then
        로그인_성공(올바른_로그인);
        
        // Given
        String 토큰 = 토큰_조회(올바른_로그인);
        
        // When
        ExtractableResponse<Response> 회원_정보_조회 = 회원_정보_조회_요청(토큰);
        
        // Then
        회원_정보_조회_됨(회원_정보_조회);
        
    }
    
    @DisplayName("올바른 토큰으로 내 정보 조회 성공")
    @Test
    void myInfoWithBearerAuth() {
        // Given
        ExtractableResponse<Response> 로그인_성공_응답  = 로그인_요청(EMAIL, PASSWORD);
        String 토큰 = 토큰_조회(로그인_성공_응답);
        
        // When
        ExtractableResponse<Response> 회원_정보_조회 = 회원_정보_조회_요청(토큰);
        
        // Then
        회원_정보_조회_됨(회원_정보_조회);
    }

    @DisplayName("잘못된 정보로 조회 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        /*        // Given
        ExtractableResponse<Response> 로그인_실패_응답  = 로그인_요청(EMAIL, "wrongpw");
        String 토큰 = 토큰_조회(로그인_실패_응답);
        
        // When
        ExtractableResponse<Response> 회원_정보_조회 = 회원_정보_조회_요청(토큰);
        
        // Then
        회원_정보_조회_실패(회원_정보_조회);*/
    }

    @DisplayName("잘못된 토큰으로 내 정보 조회 실패")
    @Test
    void myInfoWithWrongBearerAuth() {
        // When
        ExtractableResponse<Response> 회원_정보_조회 = 회원_정보_조회_요청("wrongtoken");
        
        // Then
        회원_정보_조회_실패(회원_정보_조회);
    }
    
    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(TokenRequest.of(email, password))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }
    
    private void 로그인_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
    
    private void 로그인_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    
    public static String 토큰_조회(ExtractableResponse<Response> response) {
        return response.as(TokenResponse.class).getAccessToken();
    }
    
    private ExtractableResponse<Response> 회원_정보_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
    
    private void 회원_정보_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    
    private void 회원_정보_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
    
}
