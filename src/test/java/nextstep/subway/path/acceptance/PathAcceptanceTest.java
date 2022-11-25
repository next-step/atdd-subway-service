package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptance;
import nextstep.subway.line.acceptance.SectionAcceptance;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.acceptance.StationAcceptance;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptance.create_station("강남역").as(StationResponse.class);
        양재역 = StationAcceptance.create_station("양재역").as(StationResponse.class);
        교대역 = StationAcceptance.create_station("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptance.create_station("남부터미널역").as(StationResponse.class);
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 같은 출발역과 도착역의 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("같은 출발역과 도착역을 입력하여 최단 거리를 검색한다.")
    @Test
    void findShortestPathWithSameSourceAndTarget() {
        // given
        LineAcceptance.create_line("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);

        // when
        ExtractableResponse<Response> response = PathAcceptance.shortest_path_found(교대역.getId(), 교대역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 연결되어 있지 않은 출발역과 도착역의 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("연결되어 있지 않은 출발역과 도착역을 입력하여 최단 거리를 검색한다.")
    @Test
    void findShortestPathWithNotConnectedSourceAndTarget() {
        // given
        LineAcceptance.create_line("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);
        LineAcceptance.create_line("삼호선", "bg-orange-600", 남부터미널역.getId(), 양재역.getId(), 5);

        // when
        ExtractableResponse<Response> response = PathAcceptance.shortest_path_found(교대역.getId(), 양재역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 어떤 구간에도 포함되어 있지 않은 출발역이나 도착역으로 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("어떤 구간에도 포함되어 있지 않은 출발역이나 도착역으로 최단 거리를 검색한다.")
    @Test
    void findShortestPathWithNoLinesContainingStation() {
        // given
        LineAcceptance.create_line("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);

        // when
        ExtractableResponse<Response> response = PathAcceptance.shortest_path_found(교대역.getId(), 양재역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 존재하지 않는 출발역이나 도착역으로 최단 거리를 검색하면
     * Then 최단 거리를 검색할 수 없다
     */
    @DisplayName("존재하지 않는 출발역이나 도착역으로 최단 거리를 검색한다.")
    @Test
    void findShortestPathWithNotExistStation() {
        // given
        LineAcceptance.create_line("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);

        // when
        ExtractableResponse<Response> response = PathAcceptance.shortest_path_found(-1L, 교대역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 출발역과 도착역을 입력하면
     * Then 최단 거리를 검색할 수 있다
     */
    @DisplayName("출발역과 도착역을 입력하여 최단 거리를 검색한다.")
    @Test
    void findShortestPath() {
        // given
        LineAcceptance.create_line("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        LineAcceptance.create_line("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);
        삼호선 = LineAcceptance.create_line("삼호선", "bg-orange-600", 교대역.getId(),
                양재역.getId(), 5).as(LineResponse.class);
        SectionAcceptance.update_section(삼호선.getId(), 교대역.getId(), 남부터미널역.getId(), 3);

        // when
        PathResponse pathResponse = PathAcceptance.shortest_path_found(양재역.getId(), 교대역.getId()).as(PathResponse.class);

        // then
        assertThat(pathResponse).satisfies(path -> {
            assertThat(path.getStations().stream().map(StationResponse::getName).collect(toList()))
                    .containsExactly("양재역", "남부터미널역", "교대역");
            assertEquals(5, path.getDistance());
        });
    }
}
