package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.RestAssuredApi;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 인수 테스트")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        String createdLocationUri = 회원_등록됨(회원_생성_요청(EMAIL, PASSWORD, AGE));

        회원_정보_조회됨(회원_정보_조회_요청(createdLocationUri), EMAIL, AGE);

        MemberRequest request = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(회원_정보_수정_요청(createdLocationUri, request));

        회원_삭제됨(회원_삭제_요청(createdLocationUri));
    }

    @DisplayName("내 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        회원_등록됨(회원_생성_요청(EMAIL, PASSWORD, AGE));
        TokenResponse token = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        내_정보_조회됨(내_정보_조회_요청(token), EMAIL, AGE);

        MemberRequest request = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        내_정보_수정됨(내_정보_수정_요청(token, request));

        TokenResponse updatedToken = 로그인_요청(NEW_EMAIL, NEW_PASSWORD).as(TokenResponse.class);
        내_정보_삭제됨(내_정보_삭제_요청(updatedToken));
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        MemberRequest request = new MemberRequest(email, password, age);
        return RestAssuredApi.post("/members", request);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(String uri) {
        return RestAssuredApi.get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(String uri, MemberRequest request) {
        return RestAssuredApi.put(uri, request);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(String uri) {
        return RestAssuredApi.delete(uri);
    }

    public static String 회원_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.header("Location");
    }

    public void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        회원_정보_일치됨(response, email, age);
    }

    public void 회원_정보_일치됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.as(MemberResponse.class))
                .extracting("email", "age")
                .containsExactly(email, age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(TokenResponse token) {
        return RestAssuredApi.authGet("members/me", token.getAccessToken());
    }

    public ExtractableResponse<Response> 내_정보_수정_요청(TokenResponse token, MemberRequest request) {
        return RestAssuredApi.authPut("members/me", token.getAccessToken(), request);
    }

    public ExtractableResponse<Response> 내_정보_삭제_요청(TokenResponse token) {
        return RestAssuredApi.authDelete("/members/me", token.getAccessToken());
    }

    public void 내_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        회원_정보_일치됨(response, email, age);
    }

    public void 내_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public void 내_정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
