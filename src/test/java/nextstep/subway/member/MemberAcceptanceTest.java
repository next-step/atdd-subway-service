package nextstep.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_요청;
import static nextstep.subway.member.MemberRestAssured.내_정보_삭제_요청;
import static nextstep.subway.member.MemberRestAssured.내_정보_수정_요청;
import static nextstep.subway.member.MemberRestAssured.내_정보_조회_요청;
import static nextstep.subway.member.MemberRestAssured.회원_삭제_요청;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
import static nextstep.subway.member.MemberRestAssured.회원_정보_수정_요청;
import static nextstep.subway.member.MemberRestAssured.회원_정보_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
        final String[] accessToken = new String[1];
        return Stream.of(
                dynamicTest("회원 등록되어 있음", () -> {
                    ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
                    회원_생성됨(createResponse);
                }),
                dynamicTest("로그인 정보로 조회 됨", () -> {
                    ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
                    AuthAcceptanceTest.로그인_됨(loginResponse);
                    accessToken[0] = loginResponse.as(TokenResponse.class).getAccessToken();

                    ExtractableResponse<Response> myInfoResponse = 내_정보_조회_요청(accessToken[0]);
                    내_정보_조회됨(myInfoResponse, EMAIL, AGE);
                }),
                dynamicTest("내 정보 수정 요청", () -> {
                    ExtractableResponse<Response> myInfoPutResponse = 내_정보_수정_요청(accessToken[0], new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));

                    내_정보_수정됨(myInfoPutResponse);
                }),
                dynamicTest("내 정보 삭제 요청", () -> {
                    ExtractableResponse<Response> myInfoDeleteResponse = 내_정보_삭제_요청(accessToken[0]);

                    내_정보_삭제됨(myInfoDeleteResponse);
                })
        );
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

    private void 내_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberResponse.getId()).isNotNull(),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(email),
                () -> assertThat(memberResponse.getAge()).isEqualTo(age)
        );
    }

    private void 내_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 내_정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
