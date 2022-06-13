package nextstep.subway.favorite;

import static nextstep.subway.utils.AuthMemberApiHelper.토큰을통해_내정보받기;
import static org.mockito.Mockito.when;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @MockBean
    AuthService authService;
    @MockBean
    MemberRepository memberRepository;
    LoginMember 내정보;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    Station 강남역;
    Station 교대역;
    Station 남부터미널역;
    Station 양재역;
    String 토큰;

    /**
     *Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     *
     * 경로
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");

        이호선 = new Line("이호선", "녹색", 강남역, 교대역, 10);
        신분당선 = new Line("신분당선", "빨강색", 강남역, 양재역, 9);
        삼호선 = new Line("삼호선", "주황색", 남부터미널역, 양재역, 8);
        삼호선.addStation(교대역, 남부터미널역, 7);

        토큰 = "token";

        Member 내정보 = new Member("test@test.com", "testPw", 32);
        내정보_ID_설정하기(1L, 내정보);

        when(authService.findMemberByToken(토큰)).thenReturn(new LoginMember(1L, 내정보.getEmail(), 내정보.getAge()));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(내정보));
        when(memberRepository.findByEmail(내정보.getEmail())).thenReturn(Optional.of(내정보));
    }

    private void 내정보_ID_설정하기(Long id, Member member)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = Member.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(member, 1L);
    }
}