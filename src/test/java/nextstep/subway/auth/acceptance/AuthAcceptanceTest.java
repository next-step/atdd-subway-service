package nextstep.subway.auth.acceptance;

import static nextstep.subway.utils.apiHelper.AuthMemberApiHelper.로그인을통한_토큰받기;
import static nextstep.subway.utils.apiHelper.AuthMemberApiHelper.토큰을통해_내정보받기;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.회원_생성을_요청;
import static nextstep.subway.utils.assertionHelper.AuthMemberAssertionHelper.가져온_내정보_확인하기;
import static nextstep.subway.utils.assertionHelper.AuthMemberAssertionHelper.인증실패;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    private Member 내정보;

    @BeforeEach
    public void init() {
        내정보 = new Member("test@naver.com", "testPassword", 30);
        회원_생성을_요청(내정보.getEmail(), 내정보.getPassword(), 내정보.getAge());
    }

    /**
     * background
         * given : member가 등록이 되어있을때
     * given : 로그인을 통해 token을 받고
     * when : token을 통해 내 정보를 조회하면
     * then : 정상적으로 내 정보가 조회된다
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        String token = 로그인을통한_토큰받기(내정보.getEmail(), 내정보.getPassword()).jsonPath().get("accessToken");

        //when
        ExtractableResponse<Response> 토큰을통해_내정보받기_response = 토큰을통해_내정보받기(token);

        //then
        가져온_내정보_확인하기(내정보, 토큰을통해_내정보받기_response);
    }

    /**
     * background
        * given : member가 등록이 되어있을때
     * when : 잘못된 아이디/비밀번호로 로그인을하면
     * then : 상태코드가 401이 반환된다
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //when
        ExtractableResponse<Response> 인증_요청_response = 로그인을통한_토큰받기(내정보.getEmail(), "wrongPassword");

        //then
        인증실패(인증_요청_response);
    }


    /**
     * when : 인가되지 않은 토큰을 통해 내 정보를 조회했을때
     * then : 상태코드가 401이 반환된다
    */
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        //when
        ExtractableResponse<Response> 토큰을통해_내정보받기_response = 토큰을통해_내정보받기("wrongToken!!!");

        //then
        인증실패(토큰을통해_내정보받기_response);
    }

}
