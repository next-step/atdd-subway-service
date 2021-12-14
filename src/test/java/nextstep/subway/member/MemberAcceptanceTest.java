package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_조회;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NEW_EMAIL = "newemail@email.com";
    private static final String NEW_PASSWORD = "newpassword";
    private static final int AGE = 20;
    private static final int NEW_AGE = 21;

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
        // when
        ExtractableResponse<Response> 회원_생성을_요청_응답 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(회원_생성을_요청_응답);

        // when
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        로그인_성공(로그인_요청_응답);

        // when
        String 회원_토큰 = 토큰_조회(로그인_요청_응답);
        ExtractableResponse<Response> 토큰으로_회원_정보_조회_응답 = 토큰으로_회원_정보_조회(회원_토큰);
        // then
        회원_정보_조회됨(토큰으로_회원_정보_조회_응답, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 토큰으로_회원_정보_수정_응답 = 토큰으로_회원_정보_수정(회원_토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        String 변경된_회원_토큰 = 변경된_토큰_조회(NEW_EMAIL, NEW_PASSWORD);
        // then
        회원_정보_수정됨(토큰으로_회원_정보_수정_응답);

        // when
        ExtractableResponse<Response> 토큰으로_회원_정보_삭제_응답 = 토큰으로_회원_정보_삭제(변경된_회원_토큰);
        // then
        회원_삭제됨(토큰으로_회원_정보_삭제_응답);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = MemberRequest.of(email, password, age);
        return 생성_요청("/members", memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return 조회_요청(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");
        MemberRequest memberRequest = MemberRequest.of(email, password, age);
        return 수정_요청(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return 삭제_요청(uri);
    }

    public static ExtractableResponse<Response> 토큰으로_회원_정보_조회(String accessToken) {
        return 조회_요청("/members/me", accessToken);
    }

    public static ExtractableResponse<Response> 토큰으로_회원_정보_수정(String accessToken, String newEmail, String newPassword, int newAge) {
        MemberRequest memberRequest = MemberRequest.of(newEmail, newPassword, newAge);
        return 수정_요청("/members/me", memberRequest, accessToken);
    }

    public static ExtractableResponse<Response> 토큰으로_회원_정보_삭제(String accessToken) {
        return 삭제_요청("/members/me", accessToken);
    }

    public static String 변경된_토큰_조회(String email, String password) {
        return 토큰_조회(로그인_요청(email, password));
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
