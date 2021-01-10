package nextstep.subway.path.application;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private Station 강남역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    /**
     *              거리 10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * 거리 3                     거리 10
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     *              거리 2
     */

    @BeforeEach
    void setUp() {
        강남역 = makeMockStation(1L, "강남역");
        교대역 = makeMockStation(2L, "교대역");
        양재역 = makeMockStation(3L, "양재역");
        남부터미널역 = makeMockStation(4L, "남부터미널역");

        이호선 = makeMockLine("이호선", Arrays.asList(makeMockSection(교대역, 강남역, 10)));
        삼호선 = makeMockLine("삼호선", Arrays.asList(makeMockSection(교대역, 남부터미널역, 3),
                makeMockSection(남부터미널역, 양재역, 2)));
        신분당선 = makeMockLine("신분당선", Arrays.asList(makeMockSection(강남역, 양재역, 10)));

        given(lineRepository.findAll()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        given(stationRepository.findById(강남역.getId())).willReturn(Optional.of(강남역));
        given(stationRepository.findById(교대역.getId())).willReturn(Optional.of(교대역));
        given(stationRepository.findById(양재역.getId())).willReturn(Optional.of(양재역));
        given(stationRepository.findById(남부터미널역.getId())).willReturn(Optional.of(남부터미널역));
    }


    @DisplayName("최단 경로 조회")
    @Test
    void findShortestPath() {
        // given
        given(stationRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(강남역, 남부터미널역));

        // when
        PathRequest pathRequest = new PathRequest(강남역.getId(), 남부터미널역.getId());
        PathResponse pathResponse = pathService.findShortestPath(pathRequest);

        // then
        assertThat(pathResponse.getStations())
                .map(StationResponse::getName)
                .containsExactly("강남역", "양재역", "남부터미널역");

        assertThat(pathResponse.getDistance()).isEqualTo(12);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findNotExistStations() {
        // given
        given(stationRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        PathRequest pathRequest = new PathRequest(강남역.getId(), 남부터미널역.getId());
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는");
    }

    private Line makeMockLine(String name, List<Section> sections) {
        Line line = mock(Line.class);
        given(line.getName()).willReturn(name);
        given(line.getSections()).willReturn(sections);
        return line;
    }

    private Section makeMockSection(Station upStation, Station downStation, int distance) {
        Section section = mock(Section.class);
        given(section.getUpStation()).willReturn(upStation);
        given(section.getDownStation()).willReturn(downStation);
        given(section.getDistanceWeight()).willReturn(new Distance(distance).get());
        return section;
    }

    private Station makeMockStation(Long id, String name) {
        Station station = mock(Station.class);
        given(station.getId()).willReturn(id);
        given(station.getName()).willReturn(name);
        ReflectionTestUtils.setField(station,"id", id);
        given(station.isSameStation(anyLong())).willCallRealMethod();
        return station;
    }

}
