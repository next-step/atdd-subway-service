package nextstep.subway.path;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationTestUtils;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.path.PathTestUtils.*;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 경강선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 세종대왕릉역;
    private StationResponse 여주역;

    /**
     * 교대역    --- *2호선* ---   강남역      세종대왕릉역
     * |                          |            |
     * *3호선*                   *신분당선*     *경강선*
     * |                          |            |
     * 남부터미널역  --- *3호선* ---  양재        여주역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationTestUtils.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationTestUtils.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationTestUtils.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationTestUtils.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        세종대왕릉역 = StationTestUtils.지하철역_등록되어_있음("세종대왕릉역").as(StationResponse.class);
        여주역 = StationTestUtils.지하철역_등록되어_있음("여주역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 6);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 4);
        경강선 = 지하철_노선_등록되어_있음("경강선", "bg-red-600", 세종대왕릉역, 여주역, 12);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPath() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 양재역);

        // then
        최단경로_조회_성공(response);
        최단경로_역순서_확인(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        경로_길이_확인(response, 4);
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 같은 경우)")
    @Test
    void findPathFail_equalStation() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 교대역);

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 연결이 되어 있지 않은 경우)")
    @Test
    void findPathFail_notConnected() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 세종대왕릉역);

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("최단 거리 경로 조회 실패 (존재하지 않은 출발역이나 도착역을 조회 할 경우)")
    @Test
    void findPathFail_notExist() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, new StationResponse());

        //then
        최단경로_조회_실패(response);
    }
}
