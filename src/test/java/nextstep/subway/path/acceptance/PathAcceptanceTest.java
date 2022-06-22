package nextstep.subway.path.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSteps.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceSteps.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceSteps.*;
import static nextstep.subway.station.StationAcceptanceSteps.지하철역_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private final String 어린이_이메일 = "email@email.com";
    private final String 청소년_이메일 = "email15@email.com";
    private final String 어른_이메일 = "email25@email.com";
    private final String 비밀번호 = "password";

    private final int 어린이 = 8;
    private final int 청소년 = 15;
    private final int 어른 = 25;

    private TokenResponse 어린이_사용자;
    private TokenResponse 청소년_사용자;
    private TokenResponse 어른_사용자;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 정자역;

    /**
     * Feature: 지하철 경로 검색
     * Scenario: 두 역의 최단 거리 경로를 조회
     *   Given 지하철역이 등록되어있음
     *   And 지하철 노선이 등록되어있음
     *   And 지하철 노선에 지하철역이 등록되어있음
     *
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     *                          |
     *                          정자역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 남부터미널역.getId(), 양재역.getId(), 3)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 5);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 50);

        회원_생성을_요청(어린이_이메일, 비밀번호, 어린이);
        회원_생성을_요청(청소년_이메일, 비밀번호, 청소년);
        회원_생성을_요청(어른_이메일, 비밀번호, 어른);

        어린이_사용자 = 로그인_요청(어린이_이메일, 비밀번호).as(TokenResponse.class);
        청소년_사용자 = 로그인_요청(청소년_이메일, 비밀번호).as(TokenResponse.class);
        어른_사용자 = 로그인_요청(어른_이메일, 비밀번호).as(TokenResponse.class);
    }

    @Test
    @DisplayName("최단 경로 조회")
    public void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 양재역.getId());

        // then
        지하철_최단_경로_조회됨(response);
        지하철_최단_경로_지하철역_순서_정렬됨(response, Arrays.asList(교대역, 남부터미널역, 양재역));
        지하철_최단_경로_거리(response, 8);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 {INTERNAL_SERVER_ERROR}")
    public void findPathSameSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 교대역.getId());

        // then
        지하철_최단_경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 {INTERNAL_SERVER_ERROR}")
    public void notConnectSourceAndTarget() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);
        StationResponse 삼각지역 = 지하철역_등록되어_있음("삼각지역").as(StationResponse.class);
        LineResponse 사호선 = 지하철_노선_등록되어_있음(new LineRequest("4호선", "bg-blue-600", 서울역.getId(), 삼각지역.getId(), 8)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 삼각지역.getId());

        // then
        지하철_최단_경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 {INTERNAL_SERVER_ERROR}")
    public void findPathNoExistStation() {
        // given
        Long 존재하지않는역 = 4885L;

        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(교대역.getId(), 존재하지않는역);

        // then
        지하철_최단_경로_조회_실패됨(response);
    }

    @Test
    @DisplayName("거리에 따른 운임비용: 기본운임 1,250원, 10km초과∼50km까지(5km마다 100원), 50km초과 시 (8km마다 100원)")
    void getFareByDistanceBased() {
        // when
        ExtractableResponse<Response> 거리_10km_이내_요금 = 지하철_최단_경로_조회(강남역.getId(), 양재역.getId());
        // then
        지하철_요금_조회됨(거리_10km_이내_요금, 1_250);

        // when
        ExtractableResponse<Response> 거리_10km_50km_이내_요금 = 지하철_최단_경로_조회(강남역.getId(), 남부터미널역.getId());
        // then
        지하철_요금_조회됨(거리_10km_50km_이내_요금, 1_350);

        // when
        ExtractableResponse<Response> 거리_50km_이상_요금 = 지하철_최단_경로_조회(강남역.getId(), 정자역.getId());
        // then
        지하철_요금_조회됨(거리_50km_이상_요금, 2_250);
    }

    /**
     * 동대문역 --- *1호선(300원)* --- 동묘앞역 --- *6호선(900원)* --- 창신역
     */
    @Test
    @DisplayName("노선 추가 요금: 추가 요금이 가장 비싼 노선 비용")
    void getFareByLineBased() {
        // given
        StationResponse 동대문역 = 지하철역_등록되어_있음("동대문역").as(StationResponse.class);
        StationResponse 동묘앞역 = 지하철역_등록되어_있음("동묘앞역").as(StationResponse.class);
        StationResponse 창신역 = 지하철역_등록되어_있음("창신역").as(StationResponse.class);

        LineResponse 일호선 = 지하철_노선_등록되어_있음(new LineRequest("1호선", "bg-blue-600", 300)).as(LineResponse.class);
        LineResponse 육호선 = 지하철_노선_등록되어_있음(new LineRequest("6호선", "bg-brown-600", 900)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(일호선, 동대문역, 동묘앞역, 2);
        지하철_노선에_지하철역_등록_요청(육호선, 동묘앞역, 창신역, 2);

        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회(동대문역.getId(), 창신역.getId());

        // then
        지하철_요금_조회됨(response, 2_150);
    }

    @Test
    @DisplayName("연령별 운임비용 할인: 청소년-350원을 공제한 금액의 20%할인, 어린이-350원을 공제한 금액의 50%할인")
    void getFareByAgeBased() {
        // when
        ExtractableResponse<Response> 어린이_요금_응답 = 로그인_사용자의_지하철_최단_경로_조회(어린이_사용자.getAccessToken(), 강남역.getId(), 양재역.getId());

        // then
        지하철_요금_조회됨(어린이_요금_응답, 450);

        // when
        ExtractableResponse<Response> 청소년_요금_응답 = 로그인_사용자의_지하철_최단_경로_조회(청소년_사용자.getAccessToken(), 강남역.getId(), 양재역.getId());

        // then
        지하철_요금_조회됨(청소년_요금_응답, 720);

        // when
        ExtractableResponse<Response> 어른_요금_응답 = 로그인_사용자의_지하철_최단_경로_조회(어른_사용자.getAccessToken(), 강남역.getId(), 양재역.getId());

        // then
        지하철_요금_조회됨(어른_요금_응답, 1_250);

        // when
        ExtractableResponse<Response> 비로그인_요금_응답 = 지하철_최단_경로_조회(강남역.getId(), 양재역.getId());

        // then
        지하철_요금_조회됨(비로그인_요금_응답, 1_250);
    }
}
