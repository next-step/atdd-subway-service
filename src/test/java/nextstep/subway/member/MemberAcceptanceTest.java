package nextstep.subway.member;

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

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
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
        final ExtractableResponse<Response> 생성_요청_응답 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(생성_요청_응답);

        final ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        final String 액세스_토큰 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

        final ExtractableResponse<Response> 개인정보_조회_응답 = 토큰으로_개인정보_조회(액세스_토큰);
        final MemberResponse 조회된_내정보 = 개인정보_조회_응답.as(MemberResponse.class);

        assertThat(조회된_내정보.getEmail()).isEqualTo(EMAIL);
        assertThat(조회된_내정보.getAge()).isEqualTo(AGE);

        final ExtractableResponse<Response> 회원_정보_수정_요청_응답 = 회원_정보_수정_요청(생성_요청_응답, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        assertThat(회원_정보_수정_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ExtractableResponse<Response> 로그인_재요청_응답 = 로그인_요청(NEW_EMAIL, NEW_PASSWORD);
        final String 재발급된_액세스_토큰 = 로그인_재요청_응답.as(TokenResponse.class).getAccessToken();

        final ExtractableResponse<Response> 개인정보_수정_후_조회_응답 = 토큰으로_개인정보_조회(재발급된_액세스_토큰);
        final MemberResponse 수정_후_조회된_내정보 = 개인정보_수정_후_조회_응답.as(MemberResponse.class);

        assertThat(회원_정보_수정_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(수정_후_조회된_내정보.getEmail()).isEqualTo(NEW_EMAIL);
        assertThat(수정_후_조회된_내정보.getAge()).isEqualTo(NEW_AGE);

        final ExtractableResponse<Response> 회원_삭제_요청_응답 = 회원_삭제_요청(생성_요청_응답);
        assertThat(회원_삭제_요청_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        final ExtractableResponse<Response> 삭제_후_개인정보_조회 = 토큰으로_개인정보_조회(액세스_토큰);
        assertThat(삭제_후_개인정보_조회.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 토큰으로_개인정보_조회(final String 토큰) {
        return RestAssured.given().log().all()
            .auth().oauth2(토큰)
            .when().get("/members/me")
            .then()
            .log().all()
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
