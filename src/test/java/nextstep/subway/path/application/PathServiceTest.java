package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private Station 선릉역;
    private Station 정자역;
    private Station 수원역;
    private Line 분당선;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station(1L, "선릉역");
        정자역 = new Station(2L, "정자역");
        수원역 = new Station(3L, "수원역");
        분당선 = new Line("분당선", "bg-yellow-400");
        분당선.addLineStation(new Section(분당선, 선릉역, 정자역, 40));
        분당선.addLineStation(new Section(분당선, 정자역, 수원역, 10));
    }

    @Test
    void findPath_sameLine() {
        // given
        when(lineRepository.findAll()).thenReturn(Collections.singletonList(분당선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(선릉역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(수원역));

        // when
        PathFinder parPathFinder = new PathFinder(lineRepository, stationRepository);
        PathService pathService = new PathService(parPathFinder);
        PathResponse result = pathService.findPath(1L, 3L);

        // then
        assertThat(result.getStations()).containsExactly(선릉역, 정자역, 수원역);
        assertThat(result.getDistance()).isEqualTo(50);
    }
}