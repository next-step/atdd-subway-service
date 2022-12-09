package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_후_토큰_조회;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     *
     * Feature: 회원 정보 기능
     *
     *   Scenario: 회원정보 생성 후 조회, 수정, 삭제 테스트를 한다.
     *     When 회원 생성 요청
     *     Then 회원 생성 됨
     *     When 회원 조회를 요청
     *     Then 회원 조회 됨
     *     When 회원 수정 요청
     *     Then 회원 수정 됨
     *     When 회원 삭제 요청
     *     Then 회원 삭제 됨
     *
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when 회원 생성 요청
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then 회원 생성 됨
        회원_생성됨(createResponse);

        // when 회원 조회를 요청
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then 회원 조회 됨
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when 회원 수정 요청
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then 회원 수정 됨
        회원_정보_수정됨(updateResponse);

        // when 회원 삭제 요청
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then 회원 삭제 됨
        회원_삭제됨(deleteResponse);
    }

    /**
     *
     * Feature: 내 정보 기능
     *
     *   Scenario: 내 정보에서 조회, 수정, 삭제 테스트를 한다.
     *     When 내 정보 조회 요청
     *     Then 내 정보 조회 됨
     *     When 내 정보 수정 요청
     *     Then 내 정보 수정 됨
     *     When 내 정보 삭제 요청
     *     Then 내 정보 삭제 됨
     *
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        //회원정보 조회
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String token = 로그인_후_토큰_조회(EMAIL, PASSWORD);
        //when 내 정보 조회 요청
        ExtractableResponse<Response> retrieveMyInformationResponse = 내_정보_조회_요청(token);
        //then 내 정보 조회 됨
        내_정보_조회됨(retrieveMyInformationResponse, EMAIL, AGE);

        //회원정보 수정
        //when 내 정보 수정 요청
        ExtractableResponse<Response> updateMyInformationResponse =
                내_정보_수정_요청(NEW_EMAIL, NEW_PASSWORD, NEW_AGE, token);
        //then 내 정보 수정 됨
        내_정보_수정됨(updateMyInformationResponse);

        //회원정보 삭제
        //when 내 정보 삭제 요청
        String newToken = 로그인_후_토큰_조회(NEW_EMAIL, NEW_PASSWORD);
        ExtractableResponse<Response> deleteMyInformationResponse = 내_정보_삭제_요청(newToken);
        //then 내 정보 삭제 됨
        내_정보_삭제됨(deleteMyInformationResponse);
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

    private static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response,
                                                             String email, String password, Integer age) {
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

    private static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내_정보_수정_요청(String email, String password, int age, String token) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 내_정보_삭제_요청(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    private static void 내_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        LoginMember memberResponse = response.as(LoginMember.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private static void 내_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 내_정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
