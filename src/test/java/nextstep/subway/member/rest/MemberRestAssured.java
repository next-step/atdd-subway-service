package nextstep.subway.member.rest;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.member.dto.MemberRequest;
import org.springframework.http.MediaType;

public class MemberRestAssured {

    public static ExtractableResponse<Response> 회원가입_요청(MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내정보_조회_요청(String token) {
        Header authHeader = new Header(AuthorizationExtractor.AUTHORIZATION, "Bearer " + token);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(authHeader)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}
