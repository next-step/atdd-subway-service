package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.나의_회원정보_조회_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰으로_나의_회원정보_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptancePerMethodTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 기능")
public class MemberAcceptanceTest extends AcceptancePerMethodTest {

    public static final MemberRequest 성인_회원 = new MemberRequest("jhh992000@gmail.com", "1234", 39);
    public static final MemberRequest 청소년_회원 = new MemberRequest("teenager@gmail.com", "1234", 18);
    public static final MemberRequest 어린이_회원 = new MemberRequest("child@gmail.com", "1234", 12);
    public static final MemberRequest 아기_회원 = new MemberRequest("baby@gmail.com", "1234", 5);
    public static final MemberRequest 수정회원 = new MemberRequest("newemail@email.com", "newpassword", 21);
    public static final MemberRequest 비회원 = new MemberRequest("nonexistent@gmail.com", "1234", 20);

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(성인_회원);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, 성인_회원);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, 수정회원);
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
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(성인_회원);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(성인_회원);
        // then
        TokenResponse tokenResponse = 로그인_성공함(loginResponse);

        // when
        ExtractableResponse<Response> response = 토큰으로_나의_회원정보_조회_요청(tokenResponse.getAccessToken());
        // then
        나의_회원정보_조회_성공함(response);

        // when
        ExtractableResponse<Response> updateResponse = 나의_회원_정보_수정_요청(수정회원, tokenResponse.getAccessToken());
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> newLoginResponse = 로그인_요청(수정회원);
        // then
        TokenResponse newTokenResponse = 로그인_성공함(newLoginResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_탈퇴_요청(newTokenResponse.getAccessToken());
        // then
        회원_탈퇴됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(MemberRequest memberRequest) {
        return post(memberRequest, "/members");
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        return get(response.header("Location"));
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, MemberRequest memberRequest) {
        return put(memberRequest, response.header("Location"));
    }

    public static ExtractableResponse<Response> 나의_회원_정보_수정_요청(MemberRequest memberRequest, String accessToken) {
        return put(memberRequest, "/members/me", accessToken);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        return delete(response.header("Location"));
    }

    public static ExtractableResponse<Response> 회원_탈퇴_요청(String accessToken) {
        return delete("/members/me", accessToken);
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, MemberRequest memberRequest) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(memberRequest.getEmail());
        assertThat(memberResponse.getAge()).isEqualTo(memberRequest.getAge());
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_탈퇴됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
