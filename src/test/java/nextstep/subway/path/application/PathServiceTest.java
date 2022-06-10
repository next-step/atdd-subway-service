package nextstep.subway.path.application;

import static nextstep.subway.line.domain.DomainFixtureFactory.createLine;
import static nextstep.subway.line.domain.DomainFixtureFactory.createSection;
import static nextstep.subway.line.domain.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;
    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;
    @InjectMocks
    private PathService pathService;

    @BeforeEach
    public void setUp() {
        강남역 = createStation(1L, "강남역");
        양재역 = createStation(2L, "양재역");
        교대역 = createStation(3L, "교대역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, Distance.valueOf(10));
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, Distance.valueOf(10));
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, Distance.valueOf(5));
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)));
    }

    @DisplayName("지하철 경로 최단거리 테스트")
    @Test
    void findShortestPath() {
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        ArrayList<StationResponse> stationResponses = Lists.newArrayList(StationResponse.of(양재역),
                StationResponse.of(남부터미널역), StationResponse.of(교대역));

        when(stationService.findById(2L)).thenReturn(양재역);
        when(stationService.findById(3L)).thenReturn(교대역);
        when(lineRepository.findAll()).thenReturn(lines);
        PathResponse pathResponse = pathService.findShortestPath(양재역.id(), 교대역.id());
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getStations()).containsExactlyElementsOf(stationResponses)
        );
    }
}
