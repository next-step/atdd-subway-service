package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.step.MemberAcceptanceStep;
import org.assertj.core.api.Assertions;
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
        ExtractableResponse<Response> createResponse = MemberAcceptanceStep.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        MemberAcceptanceStep.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = MemberAcceptanceStep.회원_정보_조회_요청(createResponse);
        // then
        MemberAcceptanceStep.회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberAcceptanceStep.회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        MemberAcceptanceStep.회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberAcceptanceStep.회원_삭제_요청(createResponse);
        // then
        MemberAcceptanceStep.회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        ExtractableResponse<Response> createResponse = MemberAcceptanceStep.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        MemberAcceptanceStep.회원_생성됨(createResponse);

        TokenResponse tokenResponse = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();

        내_정보_조회(accessToken);

        내_정보_수정(accessToken);

        내_정보_삭제(accessToken);
    }

    private void 내_정보_조회(String accessToken) {
        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청(accessToken);
        // then
        내_정보_조회됨(response,EMAIL, AGE);
    }

    private ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all().extract();
    }

    public static void 내_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private void 내_정보_수정(String accessToken) {
        // when
        MemberRequest memberRequest = new MemberRequest("mkkim90@gmail.com","password", 20);
        ExtractableResponse<Response> response = 내_정보_수정_요청(accessToken, memberRequest);

        // then
        내_정보_수정_됨(response);
    }

    private ExtractableResponse<Response> 내_정보_수정_요청(String accessToken, MemberRequest memberRequest) {
        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    private void 내_정보_삭제(String accessToken) {
        // when
        ExtractableResponse<Response> response = 내_정보_삭제_요청(accessToken);

        // then
        내_정보_삭제_됨(response);
    }

    private void 내_정보_수정_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 내_정보_삭제_요청(String accessToken) {
        return RestAssured
                .given().log().all().auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    private void 내_정보_삭제_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
