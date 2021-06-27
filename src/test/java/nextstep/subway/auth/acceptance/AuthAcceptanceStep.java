package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceStep {
    public static final String LOGIN_TOKEN = "/login/token";

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post(LOGIN_TOKEN)
                .then().log().all().extract();
    }

    public static void 로그인_응답됨(ExtractableResponse<Response> 로그인_요청_결과) {
        assertThat(로그인_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 로그인_됨(ExtractableResponse<Response> 로그인_요청_결과) {
        TokenResponse actual = 로그인_요청_결과.as(TokenResponse.class);
        assertThat(actual.getAccessToken()).isNotNull();
    }

    public static void 로그인_실패됨(ExtractableResponse<Response> 로그인_요청_실패_결과) {
        assertThat(로그인_요청_실패_결과.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 나의_정보_확인(ExtractableResponse<Response> 나의_정보_조회_요청_결과, String email, int age) {
        MemberResponse actual = 나의_정보_조회_요청_결과.as(MemberResponse.class);
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getAge()).isEqualTo(age);
    }
}
