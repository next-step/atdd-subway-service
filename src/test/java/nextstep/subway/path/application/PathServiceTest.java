package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static nextstep.subway.path.acceptance.PathAcceptanceMethod.*;

@DisplayName("경로 조회 서비스 레이어 테스트")
@ExtendWith(SpringExtension.class)
class PathServiceTest {
    @MockBean
    private LineService lineService;
    @MockBean
    private StationService stationService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    public void setUp() {
        강남역 = Station.from("강남역");
        양재역 = Station.from("양재역");
        교대역 = Station.from("교대역");
        남부터미널역 = Station.from("남부터미널역");

        신분당선 = new Line.Builder("신분당선", "bg-red-600")
                .upStation(강남역)
                .downStation(양재역)
                .distance(10)
                .build();
        이호선 = new Line.Builder("이호선", "bg-green-600")
                .upStation(교대역)
                .downStation(강남역)
                .distance(10)
                .build();
        삼호선 = new Line.Builder("삼호선", "bg-orange-600")
                .upStation(교대역)
                .downStation(양재역)
                .distance(5)
                .build();

        Section section = Section.of(교대역, 남부터미널역, 3);
        삼호선.addSection(section);
    }

    /**
     * 교대역    --- *2호선*(10) ---  강남역
     * |                            |
     * *3호선*(3)                   *신분당선*(10)
     * |                            |
     * 남부터미널역 --- *3호선*(2) --- 양재
     */
    @DisplayName("최단 경로 조회 기능 테스트")
    @Test
    void findShortestPath() {
        // given
        PathService pathService = new PathService(lineService, stationService);

        // when
        List<StationResponse> responses = pathService.findShortestPath(남부터미널역.getId(), 강남역.getId());

        // then
        assertAll(
                () -> assertThat(responses).hasSize(3),
                () -> assertThat(getStationIds(responses)).containsExactly(남부터미널역.getId(), 양재역.getId(), 강남역.getId())
        );
    }
}