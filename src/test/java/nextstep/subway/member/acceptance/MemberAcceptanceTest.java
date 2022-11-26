package nextstep.subway.member.acceptance;

import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_되어_있음;
import static nextstep.subway.member.acceptance.MemberRestAssured.내_정보_삭제_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.내_정보_수정_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.내_정보_조회_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_삭제_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_생성되어_있음;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_생성을_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_정보_수정_요청;
import static nextstep.subway.member.acceptance.MemberRestAssured.회원_정보_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 기능")
public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NEW_EMAIL = "newemail@email.com";
    private static final String NEW_PASSWORD = "newpassword";
    private static final int AGE = 20;
    private static final int NEW_AGE = 21;
    private MemberRequest 정상회원_등록_요청;

    @BeforeEach
    void setup() {
        정상회원_등록_요청 = new MemberRequest(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(정상회원_등록_요청);
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

    /**
     * Feature: 내 정보 관련 기능
     *
     *   Background
     *     Given 정상회원 생성되어 있음
     *     And 정상회원 로그인 되어 있음
     *     And 비정상회원 생성되어 있지 않음
     *
     *   Scenario: 내 정보를 관리
     *     When 정상회원이 자신의 정보 조회 요청
     *     Then 자신의 정보 조회됨
     *     When 정상회원이 자신의 정보 수정 요청
     *     Then 자신의 정보 수정됨
     *     Given 변경된 정보로 정상회원 재 로그인 되어 있음
     *     When 정상회원이 자신의 정보 삭제 요청
     *     Then 자신의 정보 삭제됨
     *     When 유효하지 않은 토큰을 가진 비정상회원이 자신의 정보 조회 요청
     *     Then 자신의 정보 조회 실패됨
     *     When 유효하지 않은 토큰을 가진 비정상회원이 자신의 정보 수정 요청
     *     Then 자신의 정보 수정 실패됨
     *     When 유효하지 않은 토큰을 가진 비정상회원이 자신의 정보 삭제 요청
     *     Then 자신의 정보 삭제 실패됨
     */
    @DisplayName("내 정보 관리")
    @TestFactory
    Collection<DynamicTest> manageMyInfoSenario() {
        // Background
        회원_생성되어_있음(정상회원_등록_요청);
        ExtractableResponse<Response> loginResponse = 로그인_되어_있음(new TokenRequest(정상회원_등록_요청.getEmail(), 정상회원_등록_요청.getPassword()));
        String accessToken = loginResponse.jsonPath().getString("accessToken");
        String wrongAccessToken = "wrongAccessToken";

        return Arrays.asList(
                DynamicTest.dynamicTest("정상회원은 자신의 정보를 조회할 수 있다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 내_정보_조회_요청(accessToken);

                    // then
                    내_정보_조회됨(response);
                }),
                DynamicTest.dynamicTest("정상회원은 자신의 정보를 수정할 수 있다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 내_정보_수정_요청(accessToken, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));

                    // then
                    내_정보_수정됨(response);
                }),
                DynamicTest.dynamicTest("정상회원은 자신의 정보를 삭제할 수 있다.", () -> {
                    // given
                    ExtractableResponse<Response> reLoginResponse = 로그인_되어_있음(new TokenRequest(NEW_EMAIL, NEW_PASSWORD));

                    // when
                    ExtractableResponse<Response> response = 내_정보_삭제_요청(reLoginResponse.jsonPath().getString("accessToken"));

                    // then
                    내_정보_삭제됨(response);
                }),
                DynamicTest.dynamicTest("유효하지 않은 토큰을 가진 비정상회원은 자신의 정보를 조회할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 내_정보_조회_요청(wrongAccessToken);

                    // then
                    내_정보_조회_안됨(response);
                }),
                DynamicTest.dynamicTest("유효하지 않은 토큰을 가진 비정상회원은 자신의 정보를 수정할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 내_정보_수정_요청(wrongAccessToken, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));

                    // then
                    내_정보_수정_안됨(response);
                }),
                DynamicTest.dynamicTest("유효하지 않은 토큰을 가진 비정상회원은 자신의 정보를 삭제할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 내_정보_삭제_요청(wrongAccessToken);

                    // then
                    내_정보_삭제_안됨(response);
                })
        );
    }

    private static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertAll(
                () -> assertThat(memberResponse.getId()).isNotNull(),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(email),
                () -> assertThat(memberResponse.getAge()).isEqualTo(age)
        );
    }

    private static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static void 내_정보_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 내_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 내_정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static void 내_정보_조회_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 내_정보_수정_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static void 내_정보_삭제_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
