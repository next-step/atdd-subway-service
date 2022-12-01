package nextstep.subway.member;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.acceptance.AuthAcceptanceTestStep;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MembersMeAcceptanceStep {

    private static final String MEMBERS_ME = "/members/me";

    static ExtractableResponse<Response> 나의_정보_조회_요청(TokenResponse token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(MEMBERS_ME)
                .then().log().all()
                .extract();
    }

    static void 나의_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    static ExtractableResponse<Response> 나의_정보_수정_요청(TokenResponse token,
                                                            String newEmail, String newPassword, int newAge) {
        MemberRequest memberRequest = new MemberRequest(newEmail, newPassword, newAge);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put(MEMBERS_ME)
                .then().log().all()
                .extract();
    }

    static void 나의_정보_수정됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static ExtractableResponse<Response> 나의_정보_삭제_요청(TokenResponse token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(MEMBERS_ME)
                .then().log().all()
                .extract();
    }

    static void 나의_정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static TokenResponse 로그인됨(String email, String password, int age) {
        return AuthAcceptanceTestStep.로그인_요청(email, password, age)
                .body().as(TokenResponse.class);
    }
}
