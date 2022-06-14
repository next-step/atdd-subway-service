package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTest.*;
import static nextstep.subway.path.acceptance.PathAcceptanceTest.최단경로_조회;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

public class PathAcceptanceTest2 extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;


    /**
     * 교대역    --- *2호선*(10) ---   강남역
     * |                             |
     * *3호선*(3)                   *신분당선*(10)
     * |                             |
     * 남부터미널역  --- *3호선*(2) ---   양재
     *
     * 방이역    --- *5호선*(10) ---   오금역
     */
    // Background
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given: 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        // and: 지하철 노선 등록되어 있음
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        // and: 지하철 노선에 추가로 구간 등록
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    // Scenario
    @DisplayName("지하철 최단 경로를 조회한다.")
    @Test
    void scenario1() {
        // when: 최단 경로 조회 요청
        ExtractableResponse<Response> 조회된_최단_경로 = 최단경로_조회(교대역, 양재역);

        // then: 최단 경로가 조회됨
        최단경로_지하철역_순서_정렬됨(조회된_최단_경로, Arrays.asList(교대역, 남부터미널역, 양재역));
        최단경로_길이확인(조회된_최단_경로, 5);

        // when: 최단 경로를 가지는 신규 노선 구간 등록 되어있음
        지하철_노선_등록되어_있음(new LineRequest("최단경로노선", "bg-black-600", 교대역.getId(), 양재역.getId(), 2)).as(LineResponse.class);

        // and: 최단 경로 조회 요청
        ExtractableResponse<Response> 조회된_신규_최단_경로 = 최단경로_조회(교대역, 양재역);

        // then: 최단 경로가 조회됨
        최단경로_지하철역_순서_정렬됨(조회된_신규_최단_경로, Arrays.asList(교대역, 양재역));
        최단경로_길이확인(조회된_신규_최단_경로, 2);

        // when: 새로운 노선 등록됨
        StationResponse 방이역 = 지하철역_등록되어_있음("방이역").as(StationResponse.class);
        StationResponse 오금역 = 지하철역_등록되어_있음("오금역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("5호선", "bg-보라색-600", 방이역.getId(), 오금역.getId(), 10)).as(LineResponse.class);

        // and: 연결된 구간으로 갈 수 없는 목적지 최단 경로 조회
        ExtractableResponse<Response> 목적지_역으로_이동_불가능한_최단_경로_조회됨 = 최단경로_조회(교대역, 오금역);

        // then: 최단 경로 조회에 실패
        최단경로_조회_실패(목적지_역으로_이동_불가능한_최단_경로_조회됨);

        // when: 존재하지 않는 역을 목적지로 최단 경로 조회
        ExtractableResponse<Response> 존재하지_않는_목적지_역으로_최단_경로_조회됨 = 최단경로_조회(교대역,  new StationResponse(10000L, "존재하지 않는 역"));

        // then: 최단 경로 조회에 실패
        최단경로_조회_실패(존재하지_않는_목적지_역으로_최단_경로_조회됨);
    }
}
