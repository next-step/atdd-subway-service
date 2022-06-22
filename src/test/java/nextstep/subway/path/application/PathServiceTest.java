package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static nextstep.subway.path.acceptance.PathAcceptanceMethod.getStationIds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L, "남부터미널역");

        신분당선 = new Line.Builder("신분당선", "bg-red-600")
                .section(Section.of(강남역, 양재역, 10))
                .build();
        이호선 = new Line.Builder("이호선", "bg-green-600")
                .section(Section.of(교대역, 강남역, 10))
                .build();
        삼호선 = new Line.Builder("삼호선", "bg-orange-600")
                .section(Section.of(교대역, 양재역, 5))
                .build();

        Section section = Section.of(교대역, 남부터미널역, 3);
        삼호선.addSection(section);
    }

    /**
     * 교대역    --- *2호선*(10) ---  강남역
     * |                            |
     * *3호선*(3)                   *신분당선*(10)
     * |                            |
     * 남부터미널역 --- *3호선*(2) --- 양재역
     */
    @DisplayName("최단 경로 조회 기능 테스트")
    @Test
    void findShortestPath() {
        // given
        PathService pathService = new PathService(lineService, stationService);

        // when
        when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(남부터미널역.getId())).thenReturn(남부터미널역);
        when(stationService.findStationById(강남역.getId())).thenReturn(강남역);
        PathResponse pathResponse = pathService.findShortestPath(남부터미널역.getId(), 강남역.getId());

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(getStationIds(pathResponse.getStations()))
                        .containsExactly(남부터미널역.getId(), 양재역.getId(), 강남역.getId())
        );
    }
}