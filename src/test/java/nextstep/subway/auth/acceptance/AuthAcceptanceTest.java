package nextstep.subway.auth.acceptance;

import static nextstep.subway.utils.AuthMemberApiHelper.로그인을통한_토큰받기;
import static nextstep.subway.utils.AuthMemberApiHelper.토큰을통해_내정보받기;
import static nextstep.subway.utils.AuthMemberAssertionHelper.가져온_내정보_확인하기;
import static nextstep.subway.utils.AuthMemberAssertionHelper.인증실패;
import static org.mockito.Mockito.when;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Optional;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AuthAcceptanceTest extends AcceptanceTest {

    @MockBean
    MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void init() {
        member = new Member("test@naver.com", "testPassword", 30);
    }

    /**
     * given : member가 등록이 되어있을때 (mocking)
     * given : 로그인을 통해 token을 받고
     * when : token을 통해 내 정보를 조회하면
     * then : 정상적으로 내 정보가 조회된다
     */
    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //given
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(memberRepository.findById(null)).thenReturn(Optional.of(member));
        String token = 로그인을통한_토큰받기(member.getEmail(), member.getPassword()).jsonPath()
            .get("accessToken");

        //when
        ExtractableResponse<Response> 토큰을통해_내정보받기_response = 토큰을통해_내정보받기(token);

        //then
        가져온_내정보_확인하기(member, 토큰을통해_내정보받기_response);
    }

    /**
     * given : member가 등록이 되어있을때 (mocking)
     * when : 잘못된 아이디/비밀번호로 로그인을하면
     * then : 상태코드가 401이 반환된다
     */
    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        //given
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        //when
        ExtractableResponse<Response> 인증_요청_response = 로그인을통한_토큰받기(member.getEmail(),
            "wrongPassword");

        //then
        인증실패(인증_요청_response);
    }


    /**
     * when : 인가되지 않은 토큰을 통해 내 정보를 조회했을때
     * then : 에러가 발생한다.
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
