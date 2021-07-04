package nextstep.subway.auth.acceptance;

import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationRequest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원인 계정으로 로그인을 시도")
    @Test
    void 회원인_계정으로_로그인을_시도() {
        //given
        final String email = "email@email.com";
        final String password = "password";
        final Integer age = 29;

        MemberAcceptanceTest.회원_생성되어_있음(email, password, age);

        // when
        ExtractableResponse<Response> response = 로그인을_요청(email, password);

        // then
        로그인이_응답됨(response);
    }   

    @DisplayName("회원이 아닌 계정으로 로그인을 시도")
    @Test
    void 회원이_아닌_계정으로_로그인_시도() {
        //given
        final String email = "email@email.com";
        final String password = "password";
        final Integer age = 29;

        MemberAcceptanceTest.회원_생성되어_있음(email, password, age);

        // when
        ExtractableResponse<Response> response = 로그인을_요청("unregistered", "unregistered");

        // then
        로그인이_실패됨(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private ExtractableResponse<Response> 로그인을_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/login/token")
            .then().log().all().extract();
    }

    private void 로그인이_응답됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 로그인이_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
