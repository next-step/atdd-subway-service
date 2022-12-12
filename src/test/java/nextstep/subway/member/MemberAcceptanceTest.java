package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.rest.AuthRestAssured;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.rest.MemberRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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
        // given
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        // when
        ExtractableResponse<Response> createResponse = MemberRestAssured.회원_가입_요청(memberRequest);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = MemberRestAssured.회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberRestAssured.회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = MemberRestAssured.회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    /**
     * Feature: 나의 정보 관리 기능
     *
     * Given 내 정보가 회원으로 등록되어 있음
     * Given 로그인 되어있음
     *
     * Scenario: 나의 정보를 관리한다.
     * When 내 정보 조회 요청
     * Then 내 정보 조회 됨
     * When 내 정보 수정 요청
     * Then 내 정보 수정 됨
     * Given 내 정보(Email)가 수정되었으므로 token 재발급
     * When 내 정보 삭제 요청
     * Then 내 정보 삭제 됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        MemberRestAssured.회원_가입_요청(new MemberRequest(EMAIL, PASSWORD, AGE));
        TokenResponse tokenResponse = AuthRestAssured.로그인_요청(new TokenRequest(EMAIL, PASSWORD))
                .as(TokenResponse.class);
        String accessToken = tokenResponse.getAccessToken();

        // when
        ExtractableResponse<Response> 내정보_조회_요청_결과 = MemberRestAssured.내정보_조회_요청(accessToken);
        // then
        내정보_조회됨(내정보_조회_요청_결과);

        // when
        MemberRequest 내정보_수정_요청 = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ExtractableResponse<Response> 내정보_수정_요청_결과 = MemberRestAssured.내정보_수정_요청(accessToken, 내정보_수정_요청);
        // then
        내정보_수정됨(내정보_수정_요청_결과);

        // given(email 변경했으므로 token 재발급)
        accessToken = AuthRestAssured.로그인_요청(new TokenRequest(NEW_EMAIL, NEW_PASSWORD))
                .as(TokenResponse.class)
                .getAccessToken();
        // when
        ExtractableResponse<Response> 내정보_삭제_요청_결과 = MemberRestAssured.내정보_삭제_요청(accessToken);
        // then
        내정보_삭제됨(내정보_삭제_요청_결과);
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

    private void 내정보_조회됨(ExtractableResponse<Response> response) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(memberResponse.getAge()).isEqualTo(AGE)
        );
    }

    private void 내정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 내정보_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
