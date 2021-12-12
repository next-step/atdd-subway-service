package nextstep.subway.member.acceptance;


import static nextstep.subway.member.step.MemberAcceptanceStep.내정보_조회_요청;
import static nextstep.subway.member.step.MemberAcceptanceStep.내정보_조회_일치함;
import static nextstep.subway.member.step.MemberAcceptanceStep.로그인_토큰발급;
import static nextstep.subway.member.step.MemberAcceptanceStep.로그인_회원_내정보_삭제;
import static nextstep.subway.member.step.MemberAcceptanceStep.로그인_회원_정보_수정_요청;
import static nextstep.subway.member.step.MemberAcceptanceStep.회원_생성을_요청;
import static nextstep.subway.member.step.MemberAcceptanceStep.회원_정보_수정됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("나의 정보를 관리한다.")
class MemberProfileManageAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    private String token;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = 로그인_토큰발급(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("로그인 회원 내정보 관리 기능")
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> 회원정보수정_응답 = 로그인_회원_정보_수정_요청(token, NEW_EMAIL, NEW_PASSWORD,
            NEW_AGE);
        // then
        회원_정보_수정됨(회원정보수정_응답);

        // when
        token = 로그인_토큰발급(NEW_EMAIL, NEW_PASSWORD);
        ExtractableResponse<Response> 내정보_조회_응답 = 내정보_조회_요청(token);
        // then
        내정보_조회_일치함(내정보_조회_응답, NEW_EMAIL, NEW_AGE);

        // when
        ExtractableResponse<Response> 내_정보_삭제_응답 = 로그인_회원_내정보_삭제(token);
        // then
        assertThat(내_정보_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
