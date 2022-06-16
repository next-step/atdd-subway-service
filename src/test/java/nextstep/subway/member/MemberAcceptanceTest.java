package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestAssuredRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청_및_토큰_추출;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/members";
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
        // given
        Member 회원 = 회원_생성을_요청(EMAIL, PASSWORD, AGE).as(Member.class);
        String token = 로그인_요청_및_토큰_추출(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회(회원.getId(), 회원.getEmail(), 회원.getAge(), token);
        // then
        회원_정보_조회됨(내_정보_조회_응답, EMAIL, AGE);

        // when
        MemberRequest memberUpdateRequestBody = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> 내_정보_수정_응답 =
                내_정보_수정(회원.getId(), 회원.getEmail(), 회원.getAge(), memberUpdateRequestBody, token);
        ExtractableResponse<Response> 회원_정보 = 회원_정보_조회_요청(PATH + "/" + 회원.getId());
        // then
        회원_정보_수정됨(내_정보_수정_응답);
        회원_정보_조회됨(회원_정보, NEW_EMAIL, NEW_AGE);

        // when
        ExtractableResponse<Response> 내_정보_삭제_응답 = 내_정보_삭제(회원.getId(), 회원.getEmail(), 회원.getAge(), token);
        ExtractableResponse<Response> 삭제_회원_정보 = 회원_정보_조회_요청(PATH + "/" + 회원.getId());
        // then
        회원_삭제됨(내_정보_삭제_응답);
        회원_정보_없음(삭제_회원_정보);
    }

    @DisplayName("내 정보 관리시 잘못된 정보로 요청하면 예외가 발생해야 한다")
    @Test
    void manageMyInfoByInvalidInfo() {
        // given
        Member 회원 = 회원_생성을_요청(EMAIL, PASSWORD, AGE).as(Member.class);
        String token = 로그인_요청_및_토큰_추출(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회(회원.getId(), NEW_EMAIL, NEW_AGE, token);
        // then
        회원_정보_요청_실패됨(내_정보_조회_응답);

        // when
        MemberRequest memberUpdateRequestBody = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> 내_정보_수정_응답 =
                내_정보_수정(회원.getId(), NEW_EMAIL, NEW_AGE, memberUpdateRequestBody, token);
        // then
        회원_정보_요청_실패됨(내_정보_수정_응답);

        // when
        ExtractableResponse<Response> 내_정보_삭제_응답 = 내_정보_삭제(회원.getId(), NEW_EMAIL, NEW_AGE, token);
        // then
        회원_정보_요청_실패됨(내_정보_삭제_응답);
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

        return 회원_정보_조회_요청(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(String path) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(path)
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

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, Integer age) {
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

    public static void 회원_정보_없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 회원_정보_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 내_정보_조회(Long id, String email, Integer age, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("email", email);
        params.put("age", age);

        return RestAssuredRequest.getRequest(PATH + "/me", params, token);
    }

    public static ExtractableResponse<Response> 내_정보_수정(
            Long id, String email, Integer age, MemberRequest body, String token
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("email", email);
        params.put("age", age);

        return RestAssuredRequest.putRequest(PATH + "/me", params, body, token);
    }

    public static ExtractableResponse<Response> 내_정보_삭제(
            Long id, String email, Integer age, String token
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("email", email);
        params.put("age", age);

        return RestAssuredRequest.deleteRequest(PATH + "/me", params, token);
    }
}
