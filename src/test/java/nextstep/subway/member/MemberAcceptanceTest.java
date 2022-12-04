package nextstep.subway.member;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 정보 관리 관련 기능")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

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

    /**
     * Scenario : 내 정보 관리 기능
     *
     * When : 회원 생성을 요청
     * Then : 회원 생성됨
     * When : 로그인 토큰 생성 요청
     * Then : 로그인 토큰 생성 성공함
     * When : 토큰으로 회원 정보 조회 요청
     * Then : 회원 정보 조회됨
     * When : 토큰으로 회원 정보 수정 요청(Email 주소도 같이 수정)
     * Then : 회원 정보 수정됨
     * When : 로그인 신규 토큰 생성 요청(새로운 Email 주소 기반)
     * Then : 로그인 신규 토큰 생성 성공함
     * When : 토큰으로 회원 삭제 요청
     * Then : 회원 삭제됨
     *
     */
    @DisplayName("나의 정보를 관리한다.(with accessToken)")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> createTokenResponse = 로그인_토큰_생성_요청(tokenRequest);
        //then
        로그인_토큰_생성_성공함(createTokenResponse);

        // when
        String accessToken = createTokenResponse.as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청_with_accessToken(accessToken);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청_with_accessToken(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        tokenRequest = new TokenRequest(NEW_EMAIL, NEW_PASSWORD);
        createTokenResponse = 로그인_토큰_생성_요청(tokenRequest);
        //then
        로그인_토큰_생성_성공함(createTokenResponse);
        accessToken = 로그인_토큰_생성_요청(tokenRequest).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청_with_accessToken(accessToken);
        // then
        회원_삭제됨(deleteResponse);
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

    public static ExtractableResponse<Response> 회원_정보_조회_요청_with_accessToken(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청_with_accessToken(String accessToken, String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청_with_accessToken(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }
}
