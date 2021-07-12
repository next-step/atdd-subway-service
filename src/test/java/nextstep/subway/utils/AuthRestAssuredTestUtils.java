package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthRestAssuredTestUtils {

    public static void 회원_등록됨(String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        RestAssured.given().log().all()
            .body(memberRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/members")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured.given().log().all()
            .body(tokenRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/login/token")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 잘못된_정보로_로그인을_요청(String email, String password) {
        return 로그인_요청(email, password);
    }

    public static String 로그인_됨(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
        return tokenResponse.getAccessToken();
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
