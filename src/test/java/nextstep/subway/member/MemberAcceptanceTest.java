package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.내정보_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.내정보_삭제됨;
import static nextstep.subway.member.MemberAcceptanceSupport.내정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.내정보_조회_성공;
import static nextstep.subway.member.MemberAcceptanceSupport.내정보_조회_실패;
import static nextstep.subway.member.MemberAcceptanceSupport.내정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_삭제됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_수정됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;
    private final String INVALID_TOKEN = "invalid_token";

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

    @DisplayName("내 정보 조회")
    @Test
    void myInfoWithBearerAuth() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        ExtractableResponse<Response> response = 내정보_조회_요청(accessToken);
        내정보_조회_성공(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        로그인_요청(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내정보_조회_요청(INVALID_TOKEN);
        내정보_조회_실패(response);
    }

    @DisplayName("내 정보 수정")
    @Test
    void myInfoUpdateWithBearerAuth() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        내정보_수정_요청(accessToken, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));

        accessToken = 로그인_요청(NEW_EMAIL, NEW_PASSWORD).jsonPath().getString("accessToken");
        ExtractableResponse<Response> response = 내정보_조회_요청(accessToken);
        assertAll(
            () -> assertThat(response.jsonPath().getString("email")).isEqualTo(NEW_EMAIL),
            () -> assertThat(response.jsonPath().getInt("age")).isEqualTo(NEW_AGE)
        );
    }

    @DisplayName("내 정보 삭제")
    @Test
    void myInfoDeleteWithBearerAuth() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        ExtractableResponse<Response> response = 내정보_삭제_요청(accessToken);
        내정보_삭제됨(response);
    }
}
