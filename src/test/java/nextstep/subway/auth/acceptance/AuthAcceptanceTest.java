package nextstep.subway.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberRestHelper;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    private final String email = "test@test.com";
    private final String password = "p@ssw0rd";
    private final int age = 20;


    @BeforeEach
    void setup() {
        MemberRestHelper.회원_생성을_요청(email, password, age);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {

        String accessToken = AuthRestHelper.토큰_구하기(email, password);

        assertThat(accessToken).isNotBlank();
        assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();

        ExtractableResponse<Response> memberResponse = MemberRestHelper.내_정보_조회(accessToken);
        MemberResponse memberBody = memberResponse.as(MemberResponse.class);

        assertThat(memberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberBody.getEmail()).isEqualTo(email);
        assertThat(memberBody.getAge()).isEqualTo(age);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        TokenRequest request = new TokenRequest(email, "");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        String accessToken = "DIRTY_TOKEN";

        ExtractableResponse<Response> memberResponse = MemberRestHelper.내_정보_조회(accessToken);

        assertThat(memberResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
