package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.FareResolver;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Fare;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private PathFinder pathFinder;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private FareResolver fareResolver;
    @InjectMocks
    private PathService pathService;

    @Test
    void 최단_경로를_찾을_수_있다() {
        //given
        given(stationRepository.findById(1L)).willReturn(Optional.of(new Station("강남역")));
        given(stationRepository.findById(2L)).willReturn(Optional.of(new Station("양재역")));
        given(lineRepository.findAll()).willReturn(Collections.emptyList());
        given(pathFinder.getShortestPath(any())).willReturn(new ShortestPath(Collections.emptyList(),
                new Lines(Collections.singletonList(new Line("신분당선", "", 10))), 0));
        given(fareResolver.resolve(anyLong())).willReturn(new Fare(1000));

        //when
        PathResponse pathResponse = pathService.get(1, 2);

        //then
        assertThat(pathResponse.getDistance()).isZero();
        assertThat(pathResponse.getStations()).isEmpty();
        assertThat(pathResponse.getFare()).isEqualTo(1010);
    }
}
