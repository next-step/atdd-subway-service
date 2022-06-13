package nextstep.subway.favorite;

import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.FavoriteApiHelper.즐겨찾기_목록조회요청;
import static nextstep.subway.utils.FavoriteApiHelper.즐겨찾기_삭제요청;
import static nextstep.subway.utils.FavoriteApiHelper.즐겨찾기_생성요청;
import static nextstep.subway.utils.FavoriteAssertionHelper.즐겨찾기_목록조회_결과확인;
import static nextstep.subway.utils.FavoriteAssertionHelper.즐겨찾기_삭제됨;
import static nextstep.subway.utils.FavoriteAssertionHelper.즐겨찾기_생성됨;
import static nextstep.subway.utils.LineAcceptanceHelper.지하철_노선_등록되어_있음;
import static nextstep.subway.utils.LineSectionApiHelper.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.utils.MemberApiHelper.회원_생성을_요청;
import static org.mockito.Mockito.when;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @MockBean
    AuthService authService;

    private LoginMember 내정보;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 양재역;
    private String 토큰;

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
        // TODO: 2022/06/13 질문 : mock을 사용하니, 실제 테스트 대상 객체를 저장시 외래키 에러 발생..
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineRequest 이호선_저장요청 = new LineRequest("이호선", "녹색", 강남역.getId(), 교대역.getId(), 10);
        LineRequest 신분당선_저장요청 = new LineRequest("신분당선", "빨강색", 강남역.getId(), 양재역.getId(), 9);
        LineRequest 삼호선_저장요청 = new LineRequest("삼호선", "주황색", 남부터미널역.getId(), 양재역.getId(), 8);

        LineResponse 이호선 = 지하철_노선_등록되어_있음(이호선_저장요청).as(LineResponse.class);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(신분당선_저장요청).as(LineResponse.class);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(삼호선_저장요청).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 7);

        토큰 = "token";

        Member 내정보 = new Member("test@test.com", "testPw", 32);

        String 내정보_ID = 회원_생성을_요청("test@test.com", "testPw", 32).response().getHeader("Location")
            .split("/")[2];

        when(authService.findMemberByToken(토큰)).thenReturn(
            new LoginMember(Long.parseLong(내정보_ID), 내정보.getEmail(), 내정보.getAge()));
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
        ExtractableResponse<Response> 즐겨찾기_생성요청_response_1 = 즐겨찾기_생성요청(토큰, 강남역.getId(),
            남부터미널역.getId());
        ExtractableResponse<Response> 즐겨찾기_생성요청_response_2 = 즐겨찾기_생성요청(토큰, 교대역.getId(),
            남부터미널역.getId());
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


}