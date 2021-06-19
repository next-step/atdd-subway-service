package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageMyInfo() {

        String email = "my@email";
        String password = "1234";
        int age = 29;

        MemberRequest member = new MemberRequest(email, password, age);
        BearerAuthToken token = new BearerAuthToken();

        MemberRequest updateMember = new MemberRequest(email, password, age + 1);

        return Stream.of(
            dynamicTest("회원 가입", () -> 회원_생성을_요청(email, password, age)),
            dynamicTest("로그인 성공 및 토큰 생성", loginAndGetToken(member, token)),
            dynamicTest("내 정보 확인 성공", findMyInformationSuccess(member, token)),
            dynamicTest("내 정보 수정 성공", updateMyInformationSuccess(updateMember, token)),
            dynamicTest("수정된 정보로 조회 성공", findMyInformationSuccess(updateMember, token)),
            dynamicTest("회원 정보 삭제 성공", deleteMyInformationSuccess(token)),
            dynamicTest("탈퇴한 정보 찾기 실패", findMyInformationFail(token))
        );
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> loginRequest(MemberRequest member) {
        return RestAssured.given().log().all()
                          .body(member)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/login/token")
                          .then().log().all()
                          .extract();
    }

    private Executable loginAndGetToken(MemberRequest member, BearerAuthToken bearerAuthToken) {
        return () -> {
            ExtractableResponse<Response> response = loginRequest(member);
            TokenResponse tokenResponse = response.as(TokenResponse.class);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(tokenResponse.getAccessToken()).isNotBlank();

            bearerAuthToken.changeTo(tokenResponse);
        };
    }

    private ExtractableResponse<Response> findMyInformationRequest(BearerAuthToken bearerAuthToken) {
        return RestAssured.given().log().all()
                          .auth().oauth2(bearerAuthToken.getToken())
                          .accept(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/members/me")
                          .then().log().all()
                          .extract();
    }

    private Executable findMyInformationSuccess(MemberRequest member, BearerAuthToken bearerAuthToken) {
        return () -> {
            ExtractableResponse<Response> response = findMyInformationRequest(bearerAuthToken);

            MemberResponse memberResponse = response.as(MemberResponse.class);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
            assertThat(memberResponse.getAge()).isEqualTo(member.getAge());
        };
    }

    private Executable updateMyInformationSuccess(MemberRequest member, BearerAuthToken bearerAuthToken) {
        return () -> {
            ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                           .auth().oauth2(bearerAuthToken.getToken())
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .body(member)
                           .when().put("/members/me")
                           .then().log().all()
                           .extract();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        };
    }

    private Executable deleteMyInformationSuccess(BearerAuthToken bearerAuthToken) {
        return () -> {
            ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                           .auth().oauth2(bearerAuthToken.getToken())
                           .when().delete("/members/me")
                           .then().log().all()
                           .extract();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        };
    }

    private Executable findMyInformationFail(BearerAuthToken bearerAuthToken) {
        return () -> {
            ExtractableResponse<Response> response = findMyInformationRequest(bearerAuthToken);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        };
    }

    private static class BearerAuthToken {
        private String token;

        public String getToken() {
            return token;
        }

        public void changeTo(TokenResponse tokenResponse) {
            this.token = tokenResponse.getAccessToken();
        }
    }
}
