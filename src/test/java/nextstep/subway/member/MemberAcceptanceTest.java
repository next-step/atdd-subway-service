package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

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
     *     Given 회원 등록되어 있고, 로그인 성공하여 토큰을 가져온 후
     *     When 나의 정보를 요청하면
     *     Then 나의 정보를 정상적으로 가져온다.
     */
    @DisplayName("나의 정보를 조회한다.")
    @Test
    void getMyInfo() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 나의_정보_조회_응답 = 나의_정보_조회_요청(accessToken);

        //then
        회원_정보_조회됨(나의_정보_조회_응답, EMAIL, AGE);
    }

    /**
     *     Given 회원 등록되어 있고, 로그인 성공하여 토큰을 가져온 후
     *     When 나의 정보를 수정 후, 재로그인 후 정보조회를 하면
     *     Then 나의 정보가 수정된다.
     */
    @DisplayName("나의 정보를 수정한다.")
    @Test
    void updateMyInfo() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        //when
        나의_정보_수정_요청(accessToken, "변경이메일", "변경패스워드", AGE);
        String newAccessToken = 로그인_요청("변경이메일", "변경패스워드").as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> 나의_정보_조회_응답 = 나의_정보_조회_요청(newAccessToken);

        //then
        회원_정보_조회됨(나의_정보_조회_응답, "변경이메일", AGE);
    }

    /**
     *     Given 회원 등록되어 있고
     *     When 로그인 후, 토큰으로 나의 정보를 삭제 요청하면
     *     Then 정상적으로 삭제가 된다.
     */
    @DisplayName("나의 정보를 삭제한다.")
    @Test
    void deleteMyInfo() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        //when
        String accessToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> 나의_정보_삭제_응답 = 나의_정보_삭제_요청(accessToken);

        //then
        회원_삭제됨(나의_정보_삭제_응답);
    }


    public static ExtractableResponse<Response> 나의_정보_조회_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 나의_정보_삭제_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_정보_수정_요청(String accessToken, String email, String password, Integer age ) {
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
