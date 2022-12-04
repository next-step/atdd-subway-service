package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("PathService 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private static final long SOURCE = 1L;
    private static final long TARGET = 2L;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private PathService pathService;

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "bg-green-600", 0);
        신분당선 = new Line("신분당선", "bg-red-600", 0);
        삼호선 = new Line("3호선", "bg-orange-600", 0);

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
    }

    @Test
    void 출발역과_도착역으로_최단_거리를_검색() {
        PathRequest pathRequest = new PathRequest(SOURCE, TARGET);
        Section 강남역_양재역 = new Section(신분당선, 강남역, 양재역, 10);
        Section 교대역_강남역 = new Section(이호선, 교대역, 강남역, 10);
        Section 교대역_남부터미널역 = new Section(삼호선, 교대역, 남부터미널역, 3);
        Section 남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 2);

        when(stationRepository.findById(SOURCE)).thenReturn(Optional.of(양재역));
        when(stationRepository.findById(TARGET)).thenReturn(Optional.of(교대역));
        when(sectionRepository.findAll()).thenReturn(Arrays.asList(
           강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));

        assertThat(pathService.getShortestPath(pathRequest)).satisfies(path -> {
            assertThat(path.getStations().stream().map(StationResponse::getName).collect(toList()))
                    .containsExactly("양재역", "남부터미널역", "교대역");
            assertEquals(5, path.getDistance());
        });
    }
}
