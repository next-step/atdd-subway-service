package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인을_성공하면_토큰을_발급받는다;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String BASE_URI = "/members";
    public static final String MY_INFO_URI = BASE_URI + "/me";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;
    private TokenRequest 나의_로그인_요청;
    private MemberRequest 나의_수정된_정보 = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        나의_로그인_요청 = 회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

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
        // given
        String 토큰 = 로그인을_성공하면_토큰을_발급받는다(로그인_요청함(나의_로그인_요청));

        // 내 정보를 조회한다.
        ExtractableResponse<Response> 내_정보_조회_요청 = 내_정보_조회함(토큰);

        내_정보_조회됨(내_정보_조회_요청, 나의_로그인_요청);

        // 내 정보를 수정한다.
        ExtractableResponse<Response> 내_정보_수정_요청 = RestAssured.given().log().all()
                .auth().oauth2(토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(나의_수정된_정보)
                .when().put(MY_INFO_URI)
                .then().log().all().extract();

        내_정보_수정됨(내_정보_수정_요청);

        ExtractableResponse<Response> 수정된_내_정보_조회_요청 = 내_정보_조회함(토큰);

        내_정보_조회됨(수정된_내_정보_조회_요청, 나의_수정된_정보);

        // 내 정보를 삭제한다.


    }

    private ExtractableResponse<Response> 내_정보_조회함(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get(MY_INFO_URI)
                .then().log().all().extract();
    }

    private void 내_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 내_정보_조회됨(ExtractableResponse<Response> response, TokenRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        MemberResponse memberResponse = response.jsonPath().getObject("", MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(request.getEmail());
    }

    private void 내_정보_조회됨(ExtractableResponse<Response> response, MemberRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        MemberResponse memberResponse = response.jsonPath().getObject("", MemberResponse.class);
        assertThat(memberResponse.getEmail()).isEqualTo(request.getEmail());
    }

    private TokenRequest 회원_등록되어_있음(String email, String password, int age) {
        회원_생성을_요청(email, password, age);
        return new TokenRequest(email, password);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post(BASE_URI)
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
}
