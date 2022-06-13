package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청_성공;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberAcceptanceTest extends AcceptanceTest {
    // TODO : 합칠 수 있으면 합치기
    enum MemberType {
        MINE("/members/mine"),
        OTHER("/members/{id}");

        private String uri;

        MemberType(String uri) {
            this.uri = uri;
        }
    }

    public static final String MINE_URI = "/members/me";
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    /**
     *  When 회원을 생성하면
     *  Then 생성된다
     *  When 회원 정보를 조회하면
     *  Then 조회된다
     *  When 회원 정보를 수정하면
     *  Then 수정된다
     *  When 회원 정보를 삭제하면
     *  Then 삭제된다
     */
    @Test
    @DisplayName("회원 정보를 관리한다.")
    void manageMember() {
        // when && then
        ExtractableResponse<Response> 생성_결과 = 회원_생성하고_확인();

        // when && then
        회원_조회하고_확인(생성_결과);

        // when && then
        회원_수정하고_확인(생성_결과);

        // when && then
        회원_삭제하고_확인(생성_결과);
    }

    /**
     *  Given 내 정보가 등록되어 있고
     *  When 내 정보로 로그인을 하면
     *  Then 토큰 값이 발급된다
     *  When 내 정보를 조회하면
     *  Then 조회된다
     *  When 내 정보를 수정하면
     *  Then 수정된다
     *  When 내 정보를 삭제하면
     *  Then 삭제된다
     */
    @Test
    @DisplayName("나의 정보를 관리한다.")
    void manageMyInfo() {
        // given
        회원_생성하고_확인();

        // when && then
        TokenResponse 발급된_토큰_정보 = 로그인_요청하고_토큰_확인();

        // when && then
        내_정보_조회하고_확인(발급된_토큰_정보);

        // when && then
        내_정보_수정하고_확인(발급된_토큰_정보);

        // when && then
        내_정보_삭제하고_확인(발급된_토큰_정보);
    }

    private TokenResponse 로그인_요청하고_토큰_확인() {
        // when
        ExtractableResponse<Response> 로그인_요청_결과 = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        // then
        TokenResponse 로그인_요청_성공 = 로그인_요청_성공(로그인_요청_결과);
        return 로그인_요청_성공;
    }

    private ExtractableResponse<Response> 회원_생성하고_확인() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        return createResponse;
    }

    private void 회원_조회하고_확인(ExtractableResponse<Response> createResponse) {
        // when
        ExtractableResponse<Response> 회원_정보_조회_결과 = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(회원_정보_조회_결과, EMAIL, AGE);
    }

    private void 내_정보_조회하고_확인(TokenResponse 토큰_정보) {
        // when
        ExtractableResponse<Response> 내_정보_조회_결과 = 내_정보_조회_요청(토큰_정보);
        // then
        회원_정보_조회됨(내_정보_조회_결과, EMAIL, AGE);
    }

    private void 회원_수정하고_확인(ExtractableResponse<Response> createResponse) {
        // when
        ExtractableResponse<Response> 회원_정보_수정_결과 = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(회원_정보_수정_결과);
    }

    private void 내_정보_수정하고_확인(TokenResponse 토큰_정보) {
        // when
        ExtractableResponse<Response> 내_정보_수정_결과 = 내_정보_수정_요청(토큰_정보, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(내_정보_수정_결과);
    }

    private void 회원_삭제하고_확인(ExtractableResponse<Response> createResponse) {
        // when
        ExtractableResponse<Response> 삭제된_회원_결과 = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(삭제된_회원_결과);
    }

    private void 내_정보_삭제하고_확인(TokenResponse 토큰_정보) {
        // when
        ExtractableResponse<Response> 삭제된_내_정보_결과 = 내_정보_삭제_요청(토큰_정보);
        // then
        회원_삭제됨(삭제된_내_정보_결과);
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

    public static ExtractableResponse<Response> 내_정보_조회_요청(TokenResponse 토큰_정보) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰_정보.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(MINE_URI)
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

    public static ExtractableResponse<Response> 내_정보_수정_요청(TokenResponse 토큰_정보, String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰_정보.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put(MINE_URI)
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

    public static ExtractableResponse<Response> 내_정보_삭제_요청(TokenResponse 토큰_정보) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰_정보.getAccessToken())
                .when().delete(MINE_URI)
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

    // TODO : 합칠 수 있으면 합치기
    private static String URI_생성(ExtractableResponse<Response> response, MemberType memberType) {
        if (memberType == MemberType.MINE) {
            return memberType.uri;
        }
        return response.header("Location");
    }
}
