package nextstep.subway.path;

import static nextstep.subway.utils.LineAcceptanceMethods.*;
import static nextstep.subway.utils.StationAcceptanceMethods.*;
import static nextstep.subway.utils.PathAcceptanceMethods.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private LineResponse 칠호선;

    private StationResponse 사당역;
    private StationResponse 이수역;
    private StationResponse 고속터미널역;
    private StationResponse 교대역;


    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        사당역 = 지하철역_등록되어_있음("사당역").as(StationResponse.class);
        이수역 = 지하철역_등록되어_있음("이수역").as(StationResponse.class);
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 사당역.getId(), 교대역.getId(), 15)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 고속터미널역.getId(), 교대역.getId(), 6)).as(LineResponse.class);
        사호선 = 지하철_노선_등록되어_있음(new LineRequest("사호선", "bg-blue-600", 이수역.getId(), 사당역.getId(), 4)).as(LineResponse.class);
        칠호선 = 지하철_노선_등록되어_있음(new LineRequest("칠호선", "bg-khaki-600", 이수역.getId(), 고속터미널역.getId(), 13)).as(LineResponse.class);
    }

    @DisplayName("최단 경로를 조회.")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사당역, 교대역);

        // then
        최단_경로_조회_응답됨(response);
    }

    @DisplayName("최단 경로를 조회시 역 목록 순서 및 거리 확인.")
    @Test
    void findShortestPathWithStations() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사당역, 고속터미널역);

        // then
        최단_경로_역_목록_순서(response, Arrays.asList(사당역, 이수역, 고속터미널역));
        최단_경로_거리_확인(response, 17);
    }

    @DisplayName("노선상에 존재하지 않는 역에 대해 최단거리 조회.")
    @Test
    void findShortestPathWithNotExistsStation() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음("서울역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사당역, 서울역);

        // then
        최단_경로_조회_실패됨(response);
    }

    @DisplayName("출발역과 종착역이 같을 때 최단거리 조회")
    @Test
    void findShortestPathWithSameStation() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사당역, 사당역);

        // then
        최단_경로_조회_실패됨(response);
    }

    @DisplayName("출발역과 종착역이 연결되어 있지 않을 때 최단거리 조회")
    @Test
    void findShortestPathWithNotConnected() {
        // given
        StationResponse 광교중앙역 = 지하철역_등록되어_있음("광교중앙역").as(StationResponse.class);
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 광교중앙역.getId(), 강남역.getId(), 20)).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(사당역, 강남역);

        // then
        최단_경로_조회_실패됨(response);
    }
}
