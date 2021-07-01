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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NEW_EMAIL = "newemail@email.com";
    private static final String NEW_PASSWORD = "newpassword";
    private static final int AGE = 20;
    private static final int NEW_AGE = 21;

    private ExtractableResponse<Response> createResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        // 회원이 생성 되어 있음
        createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // given
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
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> responseForToken = 토큰_발급을_요청(EMAIL, PASSWORD);
        // then
        토큰_발급_받음(responseForToken);

        // when
        String tokenMine = responseForToken.jsonPath().getString("accessToken");
        ExtractableResponse<Response> responseForMyInfo = 나의_정보를_요청(tokenMine);
        // then
        나의_정보_받음(responseForMyInfo);

        // when
        ExtractableResponse<Response> responseForUpdateMyInfo = 나의_정보를_수정_요청(tokenMine, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        나의_정보_수정됨(responseForUpdateMyInfo);

        // when
        ExtractableResponse<Response> responseForNewToken = 토큰_발급을_요청(NEW_EMAIL, NEW_PASSWORD);
        // then
        토큰_발급_받음(responseForNewToken);
        String newTokenMine = responseForNewToken.jsonPath().getString("accessToken");
        나의_수정된_정보_확인(나의_정보를_요청(newTokenMine));

        // when
        ExtractableResponse<Response> responseForDeleteMyInfo = 나의_정보_삭제_요청(newTokenMine);
        // then
        나의_정보_삭제됨(responseForDeleteMyInfo);

    }

    private void 나의_정보_삭제됨(ExtractableResponse<Response> responseForDeleteMyInfo) {
        assertThat(responseForDeleteMyInfo.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 나의_정보_삭제_요청(String newTokenMine) {
        return RestAssured.given().log().all()
                .auth().oauth2(newTokenMine)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    private void 나의_수정된_정보_확인(ExtractableResponse<Response> responseForUpdateMyInfo) {
        MemberResponse updateMember = responseForUpdateMyInfo.jsonPath().getObject(".", MemberResponse.class);
        assertAll(
                () -> assertEquals(updateMember.getEmail(), NEW_EMAIL),
                () -> assertEquals(updateMember.getAge(), NEW_AGE)
        );
    }

    private void 나의_정보_수정됨(ExtractableResponse<Response> responseForUpdateMyInfo) {
        assertThat(responseForUpdateMyInfo.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 나의_정보를_수정_요청(String tokenMine, String newEmail, String newPassword, int newAge) {
        MemberRequest updateInfo = new MemberRequest(newEmail, newPassword, newAge);

        return RestAssured.given().log().all()
                .auth().oauth2(tokenMine)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateInfo)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    private void 나의_정보_받음(ExtractableResponse<Response> responseForMyInfo) {
        String responseEmail = responseForMyInfo.jsonPath().getString("email");
        assertThat(responseEmail).isEqualTo(EMAIL);
    }

    private ExtractableResponse<Response> 나의_정보를_요청(String tokenMine) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenMine)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private void 토큰_발급_받음(ExtractableResponse<Response> responseForToken) {
        assertThat(responseForToken.jsonPath().getString("accessToken")).isNotBlank();
    }

    private ExtractableResponse<Response> 토큰_발급을_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
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
}
