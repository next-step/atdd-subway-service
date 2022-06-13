package nextstep.subway.auth.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.내정보_조회;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    private String memberOneEmail = "oneone@gmail.com";
    private String memberOnePassword = "11";

    @BeforeEach
    public void setUp(){
        super.setUp();
        memberRepository.save(new Member(memberOneEmail, memberOnePassword, 11));
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //When
        ExtractableResponse<Response> response = 로그인_요청(memberOneEmail,memberOnePassword);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(TokenResponse.class).getAccessToken()).isNotEmpty();
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //When
        ExtractableResponse<Response> response = 로그인_요청(memberOneEmail,"WrongPassword");

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //When
        ExtractableResponse<Response> response = 내정보_조회("WrongAccessToken");

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String,String> params = new HashMap<>();
        params.put("password",password);
        params.put("email",email);

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all()
                .when().body(params).post("/login/token")
                .then().log().all()
                .extract();
    }

    public static TokenResponse 로그인_되어있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        return response.as(TokenResponse.class);
    }
}
