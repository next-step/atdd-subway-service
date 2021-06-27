package nextstep.subway.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;
    public static final String BEARER = "Bearer ";

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> 회원_정보_생성됨 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(회원_정보_생성됨);

        //when
        ExtractableResponse<Response> 회원_로그인_요청_응답 = 회원_로그인_요청(EMAIL, PASSWORD);
        //then
        String 토큰 = 토큰_추출(회원_로그인_요청_응답);

        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회(토큰, EMAIL, PASSWORD, AGE);
        // then
        내_정보_조회_응답_확인(내_정보_조회_응답, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 내_정보_수정 = 내_정보_수정(토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_로그인_요청_응답 = 회원_로그인_요청(NEW_EMAIL, NEW_PASSWORD);
        String 변경된_토큰 = 토큰_추출(회원_로그인_요청_응답);
        // then
        내_정보_수정_응답_확인(내_정보_수정);
        ExtractableResponse<Response> 내_정보_수정_후_조회_응답 = 내_정보_조회(변경된_토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        내_정보_조회_응답_확인(내_정보_수정_후_조회_응답, NEW_EMAIL, NEW_AGE);

        // when
        내_정보_제거(변경된_토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        ExtractableResponse<Response> 내_정보_제거_후_조회_응답 = 내_정보_조회(변경된_토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        제거된_내_정보_응답_확인(내_정보_제거_후_조회_응답);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private String 토큰_추출(ExtractableResponse<Response> 회원_로그인_요청_응답) {
        assertThat(회원_로그인_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        TokenResponse tokenResponse = 회원_로그인_요청_응답.as(TokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    private static ExtractableResponse<Response> 회원_로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private void 내_정보_조회_응답_확인(ExtractableResponse<Response> 내_정보_조회_응답, String email, int age) {
        assertThat(내_정보_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberResponse memberResponse = 내_정보_조회_응답.as(MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private ExtractableResponse<Response> 내_정보_조회(String 토큰, String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().header("authorization", BEARER + 토큰).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내_정보_수정(String 토큰, String newEmail, String newPassword, int newAge) {
        MemberRequest memberRequest = new MemberRequest(newEmail, newPassword, newAge);

        return RestAssured
                .given().header("authorization", BEARER + 토큰).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내_정보_제거(String 토큰, String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().header("authorization", BEARER + 토큰).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    private void 내_정보_수정_응답_확인(ExtractableResponse<Response> 내_정보_수정) {
        assertThat(내_정보_수정.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 제거된_내_정보_응답_확인(ExtractableResponse<Response> 내_정보_수정) {
        assertThat(내_정보_수정.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
