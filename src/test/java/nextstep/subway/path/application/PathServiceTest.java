package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.utils.FareCalculator;
import nextstep.subway.path.utils.JGraphTPathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    List<Line> lines = new ArrayList();
    private Station 강남역;
    private Station 교대역;
    private Station 양재역;
    private Station 남부터미널역;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        교대역 = new Station(2L, "교대역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        Line 신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10, new BigDecimal(0));
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10, new BigDecimal(0));
        Line 삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 5, new BigDecimal(0));
        삼호선.addSection(new Section(교대역, 남부터미널역, 3));

        lines.add(신분당선);
        lines.add(이호선);
        lines.add(삼호선);
    }

    @DisplayName("최단거리를 구한다")
    @Test
    void findPathTest() {
        when(lineRepository.findAll()).thenReturn(lines);

        PathService pathService = new PathService(stationRepository, lineRepository, new JGraphTPathFinder(), new FareCalculator());
        PathResponse pathResponse = pathService.findPath(new LoginMember(), 1L, 4L);

        assertThat(pathResponse.getDistance()).isEqualTo(12);
        assertThat(pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList())).containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "남부터미널역"));
    }
}
