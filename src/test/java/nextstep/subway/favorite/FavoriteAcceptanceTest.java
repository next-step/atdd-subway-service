package nextstep.subway.favorite;

import static nextstep.subway.utils.FavoriteApiHelper.즐겨찾기_목록조회요청;
import static nextstep.subway.utils.FavoriteApiHelper.즐겨찾기_삭제요청;
import static nextstep.subway.utils.FavoriteApiHelper.즐겨찾기_생성요청;
import static nextstep.subway.utils.FavoriteAssertionHelper.즐겨찾기_목록조회_결과확인;
import static nextstep.subway.utils.FavoriteAssertionHelper.즐겨찾기_삭제됨;
import static nextstep.subway.utils.FavoriteAssertionHelper.즐겨찾기_생성됨;
import static org.mockito.Mockito.when;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
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

    @MockBean
    StationRepository stationRepository;

    @MockBean
    LineRepository lineRepository;

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

        when(authService.findMemberByToken(토큰)).thenReturn(
            new LoginMember(1L, 내정보.getEmail(), 내정보.getAge()));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(내정보));
        when(memberRepository.findByEmail(내정보.getEmail())).thenReturn(Optional.of(내정보));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(남부터미널역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));
    }


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
    */
    @Test
    public void 즐겨찾기를_관리한다() {
        //when(즐겨찾기 생성요청)
        ExtractableResponse<Response> 즐겨찾기_생성요청_response_1 = 즐겨찾기_생성요청(토큰, 1L, 4L);
        ExtractableResponse<Response> 즐겨찾기_생성요청_response_2 = 즐겨찾기_생성요청(토큰, 2L, 4L);
        //then
        즐겨찾기_생성됨(즐겨찾기_생성요청_response_1);
        즐겨찾기_생성됨(즐겨찾기_생성요청_response_2);

        //when(즐겨찾기 목록조회)
        ExtractableResponse<Response> 즐겨찾기_목록조회요청_response = 즐겨찾기_목록조회요청(토큰);
        //then
        즐겨찾기_목록조회_결과확인(즐겨찾기_목록조회요청_response);

        //when(즐겨찾기 삭제)
        ExtractableResponse<Response> 즐겨찾기_삭제요청_response = 즐겨찾기_삭제요청(토큰, "1");
        //then
        즐겨찾기_삭제됨(즐겨찾기_삭제요청_response);
    }


    private void 내정보_ID_설정하기(Long id, Member member)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = Member.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(member, id);
    }


}