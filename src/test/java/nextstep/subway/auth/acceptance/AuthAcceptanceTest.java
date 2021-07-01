package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 인증 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {
    private String email = "test@test.com";
    private String password = "1234";

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        // 회원_가입_되어_있음
        회원_가입_되어_있음(email, password, 16);

        // when
        // 로그인 시도함
        TokenResponse tokenResponse = 로그인_시도(new TokenRequest(email, password)).as(TokenResponse.class);

        // then
        // 로그인 완료됨
        assertThat(tokenResponse.getAccessToken()).isNotBlank();

        // when
        // 내 정보 조회
        MemberResponse meInfo = 내_정보_조회_시도(tokenResponse).jsonPath().getObject(".", MemberResponse.class);

        // then
        // 내정보 조회됨
        assertThat(meInfo.getEmail()).isEqualTo(email);
    }

    private ExtractableResponse<Response> 내_정보_조회_시도(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효 하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private ExtractableResponse<Response> 로그인_시도(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원_가입_되어_있음(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured.given().log().all()
                .body(memberRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all()
                .extract();
    }
}
