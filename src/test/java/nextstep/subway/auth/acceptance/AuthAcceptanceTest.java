package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 권한 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private MemberRequest registeredMember;
    private MemberRequest unregisteredMember;

    @BeforeEach
    void registerMember() {
        registeredMember = new MemberRequest("test@google.com", "1234", 25);
        unregisteredMember = new MemberRequest("test2@google.com", "5678", 25);

        registerMember(registeredMember);
    }

    @DisplayName("등록된 회원 로그인 성공")
    @Test
    void loginSuccess() {
        ExtractableResponse<Response> response = loginRequest(registeredMember);
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
    }

    @DisplayName("미등록 회원 로그인 실패")
    @Test
    void loginFail01() {
        ExtractableResponse<Response> response = loginRequest(unregisteredMember);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("비밀번호가 틀리면 로그인 실패")
    @Test
    void loginFail02() {

        MemberRequest member = new MemberRequest(registeredMember.getEmail(),
                                                 registeredMember.getPassword() + "1",
                                                 registeredMember.getAge());

        ExtractableResponse<Response> response = loginRequest(member);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("INVALID_TOKEN");
        ExtractableResponse<Response> response = findMemberOfMineRequest(tokenResponse);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void registerMember(MemberRequest memberRequest) {
        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                       .body(memberRequest)
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .when().post("/members")
                       .then().log().all()
                       .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> loginRequest(MemberRequest memberRequest) {
        TokenRequest tokenRequest = new TokenRequest(memberRequest.getEmail(),
                                                     memberRequest.getPassword());

        return RestAssured.given().log().all()
                          .body(tokenRequest)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/login/token")
                          .then().log().all()
                          .extract();
    }

    private ExtractableResponse<Response> findMemberOfMineRequest(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                          .auth().oauth2(tokenResponse.getAccessToken())
                          .accept(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/members/me")
                          .then().log().all()
                          .extract();
    }
}
