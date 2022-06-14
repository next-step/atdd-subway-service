package nextstep.subway.member;

import static nextstep.subway.utils.apiHelper.AuthMemberApiHelper.로그인을통한_토큰받기;
import static nextstep.subway.utils.apiHelper.AuthMemberApiHelper.토큰을통해_내정보받기;
import static nextstep.subway.utils.assertionHelper.AuthMemberAssertionHelper.가져온_내정보_확인하기;
import static nextstep.subway.utils.assertionHelper.AuthMemberAssertionHelper.인증실패;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.내정보_삭제_요청;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.내정보_수정_요청;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.회원_삭제_요청;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.회원_생성을_요청;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.회원_정보_수정_요청;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.회원_정보_조회_요청;
import static nextstep.subway.utils.assertionHelper.MemberAssertionHelper.회원_삭제됨;
import static nextstep.subway.utils.assertionHelper.MemberAssertionHelper.회원_생성됨;
import static nextstep.subway.utils.assertionHelper.MemberAssertionHelper.회원_정보_수정됨;
import static nextstep.subway.utils.assertionHelper.MemberAssertionHelper.회원_정보_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL,
            NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    /**
      *Feature: 사용자 관련 기능
     *   Scenario: 나의 정보를 관리(조회/수정/삭제)
     *     When 사용자 생성요청
     *     Then 사용자 생성됨
     *     When 사용자 정보를 통해 로그인을 하면
     *     Then 토큰이 발행된다.
     *     Given 사용자 이메일 변경
     *     When 토큰을 사용하여 사용자 변경요청을 하고
     *     When 변경된 이메일로 토큰을 새로 생성받은 뒤
     *     Then 내 정보를 조회하면 변경된 이메일이 조회됨
     *     When 토큰을 사용하여 사용자 삭제요청을 하면
     *     Then 사용자가 조회되지 않음
    */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        //when(사용자 생성 요청)
        ExtractableResponse<Response> 회원생성_response = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then(사용자 생성됨)
        회원_생성됨(회원생성_response);

        //when(사용자 정보를 통해 로그인)
        ExtractableResponse<Response> 로그인을통한_토큰받기_response = 로그인을통한_토큰받기(EMAIL, PASSWORD);
        //then(토큰 발행)
        String 토큰 = 로그인을통한_토큰받기_response.jsonPath().get("accessToken").toString();

        //when(me를통한 사용자변경)
        ExtractableResponse<Response> 내정보_수정_요청_resposne = 내정보_수정_요청(NEW_EMAIL, NEW_PASSWORD,
            NEW_AGE, 토큰);
        //when(토큰재발급)
        토큰 = 로그인을통한_토큰받기(NEW_EMAIL, NEW_PASSWORD).jsonPath().get("accessToken").toString();
        //then(변경된 정보 확인)
        ExtractableResponse<Response> 토큰을통해_내정보받기_response = 토큰을통해_내정보받기(토큰);
        가져온_내정보_확인하기(new Member(NEW_EMAIL, NEW_PASSWORD, NEW_AGE), 토큰을통해_내정보받기_response);

        //when(me를 통한 사용자 삭제)
        ExtractableResponse<Response> 내정보_삭제_요청_response = 내정보_삭제_요청(토큰);
        //then(사용자가 조회되지 않음)
        토큰을통해_내정보받기_response = 토큰을통해_내정보받기(토큰);
        인증실패(토큰을통해_내정보받기_response);
    }
}
