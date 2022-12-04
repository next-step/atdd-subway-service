package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.acceptance.StationAcceptance;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 역삼역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptance.create_station("강남역").as(StationResponse.class);
        양재역 = StationAcceptance.create_station("양재역").as(StationResponse.class);
        역삼역 = StationAcceptance.create_station("역삼역").as(StationResponse.class);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        LineResponse 이호선 = LineAcceptance.create_line("신분당선", "bg-red-600", 0, 강남역.getId(),
                양재역.getId(), 10).as(LineResponse.class);


        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(이호선.getId());
        assertEquals("신분당선", lineResponse.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("이호선", "bg-green-600", 0, 강남역.getId(),
                역삼역.getId(), 10).as(LineResponse.class);
        LineResponse 신분당선 = LineAcceptance.create_line("신분당선", "bg-red-600", 0, 강남역.getId(),
                양재역.getId(), 10).as(LineResponse.class);

        // when
        List<LineResponse> lines = LineAcceptance.line_list_was_queried();

        // then
        assertThat(lines).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse 신분당선 = LineAcceptance.create_line("신분당선", "bg-red-600", 0, 강남역.getId(),
                양재역.getId(), 10).as(LineResponse.class);

        // when
        LineResponse lineResponse = LineAcceptance.line_was_queried(신분당선.getId());

        // then
        assertEquals("신분당선", lineResponse.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse 신분당선 = LineAcceptance.create_line("신분당선", "bg-red-600", 0, 강남역.getId(),
                양재역.getId(), 10).as(LineResponse.class);

        // when
        LineAcceptance.update_line(신분당선.getId(), "다른분당선", "bg-red-600", 0);

        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(신분당선.getId());
        assertEquals("다른분당선", lineResponse.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse 신분당선 = LineAcceptance.create_line("신분당선", "bg-red-600", 0, 강남역.getId(),
                양재역.getId(), 10).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = LineAcceptance.delete_line(신분당선.getId());

        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }
}
