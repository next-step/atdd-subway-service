package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.내정보_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.내정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.내정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인_성공_이후_토큰;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_삭제됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_수정됨;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_정보_조회됨;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    private String 로그인_토큰;

    @BeforeEach
    void before() {
        super.setUp();
        ExtractableResponse<Response> 회원_생성_요청_응답 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_요청_응답);
    }

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
        //when: 로그인 요청
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        로그인_토큰 = 로그인_성공_이후_토큰(로그인_요청_응답);

        //then: 내 정보 조회
        ExtractableResponse<Response> 내정보_조회_요청_응답 = 내정보_조회_요청(로그인_토큰);
        회원_정보_조회됨(내정보_조회_요청_응답, EMAIL, AGE);

        //when: 내 정보 수정
        ExtractableResponse<Response> 내정보_수정요청_응답 = 내정보_수정_요청(로그인_토큰, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        //then: 내 정보 수정됨
        회원_정보_수정됨(내정보_수정요청_응답);

        //when: 내 정보 삭제
        ExtractableResponse<Response> 내정보_삭제_요청_응답 = 내정보_삭제_요청(로그인_토큰);

        //then: 내 정보 삭제
        회원_삭제됨(내정보_삭제_요청_응답);
    }

}
