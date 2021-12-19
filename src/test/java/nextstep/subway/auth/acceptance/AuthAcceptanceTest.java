package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // 회원 등록 되어 있음
        MemberRequest memberRequest = new MemberRequest("jdragon@woo.com","12345",20);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all();

        // 로그인 요청
        Map<String, String> params = new HashMap<>();
        params.put("email", "jdragon@woo.com");
        params.put("password", "12345");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse).isNotNull();

//        MemberResponse memberResponse = RestAssured
//                .given().log().all()
//                .auth().oauth2(tokenResponse.getAccessToken())
//                .when().get("/members/me")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .extract().as(MemberResponse.class);

    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // 회원 등록 되어 있음
        MemberRequest memberRequest = new MemberRequest("jdragon@woo.com","12345",20);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all();

        // 로그인 요청
        Map<String, String> params = new HashMap<>();
        params.put("email", "jdragon@woo.com");
        params.put("password", "123456789");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // 회원 등록 되어 있음
        MemberRequest memberRequest = new MemberRequest("jdragon@woo.com","12345",20);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all();

        // 로그인 요청
        Map<String, String> params = new HashMap<>();
        params.put("email", "jdragon@woo.com");
        params.put("password", "12345");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        assertThat(tokenResponse).isNotNull();

        MemberResponse memberResponse = RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/members/me")
                .then().log().all()
                .extract().as(MemberResponse.class);

        assertThat(memberResponse.getEmail()).isEqualTo(memberRequest.getEmail());
        assertThat(memberResponse.getAge()).isEqualTo(memberRequest.getAge());
    }

}
