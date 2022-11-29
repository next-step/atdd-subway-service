package nextstep.subway.path.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceIntegrationTest.지하철역_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private StationResponse 양재역;
    private StationResponse 신사역;
    private StationResponse 잠원역;
    private StationResponse 강남역;
    private StationResponse 신논현역;
    private StationResponse 남부터미널역;
    private StationResponse 교대역;
    private StationResponse 고속터미널역;
    private LineResponse 신분당선;
    private LineResponse 삼호선;

    /**
     *  [양재] -- 강남 -- 신논현 -- 논현 --  신사 (신분당선)
     *   |                             |
     *  남부터미널 -- 교대 -- 고속터미널 -- [잠원] (3호선)
     *
     * Feature: 지하철 구간 관련 기능
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 구간 등록되어 있음
     *   Scenario: 지하철 최단 경로를 조회
     *     When 지하철의 최단 경로를 조회한다
     *     Then 최단 경로의 역 목록을 순서대로 알 수 있다.
     *     Then 최단 경로의 총 이동 거리를 알 수 있다
     */
    @Test
    @DisplayName("지하철 최단 경로 조회")
    void findShortestPath() {
        지하철역_등록();
        지하철_노선_등록();
        지하철_구간_등록되어_있음();

        PathResponse 경로_응답 = 지하철의_최단_경로_조회();
        역_목록_조회됨(경로_응답);
        총_이동거리_조회됨(경로_응답);
    }

    private void 총_이동거리_조회됨(PathResponse response) {
        assertThat(response.getDistance())
                .isEqualTo(40);
    }

    private void 역_목록_조회됨(PathResponse response) {
        assertThat(response.getStations().stream().map(StationResponse::getName))
                .containsExactly("양재역", "남부터미널역", "교대역", "고속터미널역", "잠원역");
    }

    private PathResponse 지하철의_최단_경로_조회() {
        return PathAcceptanceStep.get(양재역, 잠원역).body()
                .as(PathResponse.class);
    }

    private void 지하철_구간_등록되어_있음() {
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 신논현역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 신논현역, 신사역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 신사역, 잠원역, 10);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 교대역, 10);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 고속터미널역, 10);
        지하철_노선에_지하철역_등록_요청(삼호선, 고속터미널역, 잠원역, 10);
    }

    private void 지하철_노선_등록() {
        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "blue", 양재역.getId(), 강남역.getId(), 10))
                .body().as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("3호선", "blue", 양재역.getId(), 남부터미널역.getId(), 10))
                .body().as(LineResponse.class);
    }

    private void 지하철역_등록() {
        양재역 = 지하철역_등록되어_있음("양재역");
        강남역 = 지하철역_등록되어_있음("강남역");
        신논현역 = 지하철역_등록되어_있음("신논현역");
        신사역 = 지하철역_등록되어_있음("신사역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");
        교대역 = 지하철역_등록되어_있음("교대역");
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역");
        잠원역 = 지하철역_등록되어_있음("잠원역");
    }

}
