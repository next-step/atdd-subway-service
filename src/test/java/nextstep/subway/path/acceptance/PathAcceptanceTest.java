package nextstep.subway.path.acceptance;

import static nextstep.subway.line.acceptance.LineAcceptanceTestHelper.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestHelper.*;
import static nextstep.subway.path.acceptance.PathAcceptanceTestHelper.*;
import static nextstep.subway.station.StationAcceptanceTestHelper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 역삼역;
    private StationResponse 매봉역;
    private StationResponse 양재시민의숲역;
    private StationResponse 화곡역;
    private StationResponse 우장산역;

    /** (distance)
     *
     *      교대역    --- *2호선* (10) ---   강남역   --- *2호선* (50)  ---  역삼역
     *      |                               |
     *   *3호선* (3)                      *신분당선*  (5)
     *      |                               |
     *      남부터미널역--- *3호선* (2) --- 양재역    --- *3호선*  (20) ---  매봉역
     *                                      |
     *                                   *신분당선* (3)
     *                                      |
     *                                   양재시민의숲역
     *
     *
     *     화곡역 --- *5호선* (10) --- 우장산역
     *
     * 추가 요금
     *  - 신분당선 : 0원
     *  - 2호선 : 900원
     *  - 3호선 : 300원
     *  - 5호선 : 0원
     *
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        매봉역 = 지하철역_등록되어_있음("매봉역").as(StationResponse.class);
        양재시민의숲역 = 지하철역_등록되어_있음("양재시민의숲역").as(StationResponse.class);

        화곡역 = 지하철역_등록되어_있음("화곡역").as(StationResponse.class);
        우장산역 = 지하철역_등록되어_있음("우장산역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 5, 0);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 900);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 300);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-red-600", 화곡역, 우장산역, 5, 0);

        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 50);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 양재역, 매봉역, 20);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 양재시민의숲역, 3);

    }

    @DisplayName("지하철 최단 경로 조회")
    @Test
    void findPaths() {
        // when
        // v 강남역 > 양재역 > 남부터미널역 : 12
        //   강남역 > 교대역 > 남부터미널역 : 13
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역.getId(), 남부터미널역.getId());

        // then
        최단_경로_조회_응답됨(최단_경로_조회_응답);
        최단_경로_거리_일치됨(최단_경로_조회_응답, 12);
        최단_경로_목록_일치됨(최단_경로_조회_응답, 강남역.getId(), 양재역.getId(), 남부터미널역.getId());
    }

    @DisplayName("지하철 최단 경로 조회 - 기본요금")
    @Test
    void findPathsMinimumFare() {
        // when
        // v 강남역 > 양재역 > 양재시민의숲역 : 8
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역.getId(), 양재시민의숲역.getId());

        // then
        최단_경로_조회_응답됨(최단_경로_조회_응답);
        최단_경로_거리_일치됨(최단_경로_조회_응답, 8);
        최단_경로_목록_일치됨(최단_경로_조회_응답, 강남역.getId(), 양재역.getId(), 양재시민의숲역.getId());
        최단_경로_요금_일치됨(최단_경로_조회_응답, 1250);
    }

    @DisplayName("지하철 최단 경로 조회 - 거리 10Km 초과 50km 이하")
    @Test
    void findPathsAddFare_TenToFifty() {
        // when
        // v 남부터미널역 > 양재역 > 매봉역 : 22
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(남부터미널역.getId(), 매봉역.getId());

        // then
        최단_경로_조회_응답됨(최단_경로_조회_응답);
        최단_경로_거리_일치됨(최단_경로_조회_응답, 12);
        최단_경로_목록_일치됨(최단_경로_조회_응답, 남부터미널역.getId(), 양재역.getId(), 매봉역.getId());
        최단_경로_요금_일치됨(최단_경로_조회_응답, 1450);
    }


    @DisplayName("지하철 최단 경로 조회 - 거리 50km 초과")
    @Test
    void findPathsAddFare_MoreThanFifty() {
        // when
        // v 교대역 > 강남역 > 역삼역 : 60
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(교대역.getId(), 역삼역.getId());

        // then
        최단_경로_조회_응답됨(최단_경로_조회_응답);
        최단_경로_거리_일치됨(최단_경로_조회_응답, 12);
        최단_경로_목록_일치됨(최단_경로_조회_응답, 교대역.getId(), 강남역.getId(), 역삼역.getId());
        최단_경로_요금_일치됨(최단_경로_조회_응답, 2150);
    }


    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void invalidSameStartEndPoint() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역.getId(), 강남역.getId());

        // then
        최단_경로_조회_응답됨(최단_경로_조회_응답);
        최단_경로_거리_일치됨(최단_경로_조회_응답, 12);
        최단_경로_조회_실패됨(최단_경로_조회_응답);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void invalidNotFoundPath() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역.getId(), 화곡역.getId());

        // then
        최단_경로_조회_실패됨(최단_경로_조회_응답);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void invalidNotFoundStation() {
        // when
        ExtractableResponse<Response> 최단_경로_조회_응답 = 최단_경로_조회_요청(강남역.getId(), 화곡역.getId());

        // then
        최단_경로_조회_실패됨(최단_경로_조회_응답);
    }

}

