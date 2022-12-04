package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.member.dto.MemberRequest;
import org.springframework.http.MediaType;

public class MyInfoFixture {

    public static ExtractableResponse<Response> 내_정보_조회(final String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 내정보_수정(final String token, final String newEmail,
        final String newPassword, final int newAge) {
        MemberRequest memberRequest = new MemberRequest(newEmail, newPassword, newAge);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .body(memberRequest)
            .when().put("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 내정보_삭제(final String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/members/me")
            .then().log().all()
            .extract();
    }
}
