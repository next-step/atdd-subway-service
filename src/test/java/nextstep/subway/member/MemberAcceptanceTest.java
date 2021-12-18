package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {

        //내 정보를 생성한다
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        //내 정보 생성됨 확인
        회원_생성됨(createResponse);

        //로그인 요청
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_요청(tokenRequest);
        String accessToken = loginResponse.body().jsonPath().getString("accessToken");

        //내 정보를 조회한다
        ExtractableResponse<Response> findResponse = 내_정보_조회_요청(createResponse, accessToken, EMAIL, AGE);

        //내 정보 조회됨 확인
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        //내 정보를 수정한다
        ExtractableResponse<Response> updateResponse = 내_정보_수정_요청(createResponse, accessToken, EMAIL, PASSWORD, 21);

        //내 정보 수정됨 확인
        회원_정보_수정됨(updateResponse);

        //내 정보를 삭제한다
        ExtractableResponse<Response> deleteResponse = 내_정보_삭제_요청(createResponse, accessToken, EMAIL, AGE);

        //내 정보 삭제됨 확인
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

    public static ExtractableResponse<Response> 내_정보_조회_요청(ExtractableResponse<Response> response, String accessToken, String email, Integer age) {
        String location = response.header("Location");
        Long id = extractId(location);

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("email", email);
        params.put("age", age);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .params(params)
                .when()
                .get("/members/me")
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(ExtractableResponse<Response> response, String accessToken, String email, String password, Integer age) {

        String location = response.header("Location");
        Long id = extractId(location);

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("email", email);
        params.put("age", age);

        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .params(params)
                .body(memberRequest)
                .when()
                .put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_삭제_요청(ExtractableResponse<Response> response, String accessToken, String email, Integer age) {

        String location = response.header("Location");
        Long id = extractId(location);

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("email", email);
        params.put("age", age);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .params(params)
                .when()
                .delete("/members/me")
                .then().log().all()
                .extract();
    }
}
