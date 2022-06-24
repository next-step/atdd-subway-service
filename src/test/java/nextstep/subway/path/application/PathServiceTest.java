package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("PathService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private Station 청담역;
    private Station 뚝섬유원지역;
    private Station 건대입구역;
    private Station 구의역;
    private Station 신사역;
    private Line 이호선;
    private Line 칠호선;

    private PathService pathService;

    @Mock
    LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;

    @Mock
    SectionRepository sectionRepository;

    /**
     *  청담역 -- 7호선(20) -- 뚝섬유원지역 --7호선(5)-- 건대입구역
     *                                                  |
     *                                               2호선(10)
     *                                                  |
     *                                               구의역
     *   신사역
     */
    
    @BeforeEach
    void init() {
        pathService = new PathService(new StationService(stationRepository), lineRepository, sectionRepository);

        청담역 = new Station(1L, "청담역");
        뚝섬유원지역 = new Station(2L, "뚝섬유원지역");
        건대입구역 = new Station(3L, "건대입구역");
        구의역 = new Station(4L, "구의역");
        신사역 = new Station(5L, "신사역");

        칠호선 = new Line("칠호선", "yellow", 청담역, 뚝섬유원지역, 20, 200);
        칠호선.addLineStation(뚝섬유원지역, 건대입구역, 5);
        
        이호선 = new Line("이호선", "green", 건대입구역, 구의역, 10, 0);

        when(lineRepository.findAll()).thenReturn(Arrays.asList(칠호선, 이호선));
    }

    @Test
    @DisplayName("청담역-구의역 조회 시 가장 최단경로를 찾는다 (Happy Path)")
    void getShortestPath() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(청담역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(구의역));
        when(sectionRepository.findAll()).thenReturn(Arrays.asList(new Section(칠호선, 청담역, 뚝섬유원지역, 20),
                                                                    new Section(칠호선, 뚝섬유원지역, 건대입구역, 5),
                                                                    new Section(이호선, 건대입구역, 구의역, 10)));

        //given
        PathResponse response = pathService.getShortestPath(청담역.getId(), 구의역.getId(), 15);

        //then
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(35),
                () -> assertThat(response.getStations()).containsExactly(
                        StationResponse.of(청담역),
                        StationResponse.of(뚝섬유원지역),
                        StationResponse.of(건대입구역),
                        StationResponse.of(구의역)),
                () -> assertThat(response.getFare()).isEqualTo(1440)
                );
    }

    @Test
    void 출발역과_도착역이_같으면_조회할_수_없다() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(청담역));

        //then
        assertThatThrownBy(() -> pathService.getShortestPath(청담역.getId(), 청담역.getId(), 10))
                .isInstanceOf(PathException.class)
                .hasMessageContaining(PathException.SAME_SOURCE_TARGET_STATION_MSG);
    }

    @Test
    void 출발역이나_도착역이_존재하지않으면_조회할_수_없다() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(청담역));
        when(stationRepository.findById(5L)).thenReturn(Optional.of(신사역));

        //then
        assertThatThrownBy(() -> pathService.getShortestPath(청담역.getId(), 신사역.getId(), 10))
                .isInstanceOf(PathException.class)
                .hasMessageContaining(PathException.NO_CONTAIN_STATION_MSG);
    }
}