package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    @Test
    void manageMyInfo() {
        // given
        MemberRequest 테스트계정 = new MemberRequest("probitanima11@gmail.com", "11", 10);
        MemberRequest 암호오입력_테스트계정 = new MemberRequest("probitanima11@gmail.com", "22", 10);
        MemberRequest 회원정보수정된_테스트계정 = new MemberRequest("probitanima11@gmail.com", "11", 20);
        MemberResponse 정상생성된_테스트계정정보 = new MemberResponse("probitanima11@gmail.com", 10);
        MemberResponse 수정된_테스트계정정보 = new MemberResponse("probitanima11@gmail.com", 20);

        회원_생성을_요청(테스트계정.getEmail(), 테스트계정.getPassword(), 테스트계정.getAge());

        // when
        ExtractableResponse<Response> mismatchPasswordResponse = AuthAcceptanceTest.JWT_요청(암호오입력_테스트계정);
        // then
        AuthAcceptanceTest.인증_못받음(mismatchPasswordResponse);

        // when
        ExtractableResponse<Response> correctAccountResponse = AuthAcceptanceTest.JWT_요청(테스트계정);
        // then
        String accessJwt = JWT_받음(correctAccountResponse);

        // when
        ExtractableResponse<Response> searchMemberInfoResponse = 나의_회원정보_조회_요청(accessJwt);
        // then
        나의_회원정보_조회됨(searchMemberInfoResponse, 정상생성된_테스트계정정보);

        // when
        ExtractableResponse<Response> modifyMemberInfoResponse = 나의_회원정보_수정_요청(accessJwt, 회원정보수정된_테스트계정);
        // then
        나의_회원정보_수정됨(modifyMemberInfoResponse);

        // when
        ExtractableResponse<Response> searchMemberInfoResponseForModify = 나의_회원정보_조회_요청(accessJwt);
        // then
        나의_회원정보_조회됨(searchMemberInfoResponseForModify, 수정된_테스트계정정보);

        // when
        ExtractableResponse<Response> deleteMemberInfoResponse = 나의_회원정보_삭제_요청(accessJwt);
        // then
        나의_회원정보_삭제됨(deleteMemberInfoResponse);

        // when
        ExtractableResponse<Response> searchMemberInfoResponseAfterDelete = 나의_회원정보_조회_요청(accessJwt);
        // then
        나의_회원정보_삭제됨(searchMemberInfoResponseAfterDelete);
    }

    private void 나의_회원정보_삭제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 나의_회원정보_삭제_요청(String accessJwt) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessJwt)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    private void 나의_회원정보_수정됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 나의_회원정보_수정_요청(String accessJwt, MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessJwt)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 나의_회원정보_조회_요청(String accessJwt) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessJwt)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 나의_회원정보_조회됨(ExtractableResponse<Response> response, MemberResponse compareMemberInfo) {
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> Assertions.assertThat(response.as(MemberResponse.class).getEmail()).isEqualTo(compareMemberInfo.getEmail()),
            () -> Assertions.assertThat(response.as(MemberResponse.class).getAge()).isEqualTo(compareMemberInfo.getAge())
        );
    }

    public static String JWT_받음(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.as(TokenResponse.class).getAccessToken();
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
}
