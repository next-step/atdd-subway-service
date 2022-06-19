package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.PathResponse;
import nextstep.subway.path.domain.PathFinder;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private PathFinder pathFinder;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private LineRepository lineRepository;
    @InjectMocks
    private PathService pathService;

    @Test
    void 최단_경로를_찾을_수_있다() {
        //given
        given(stationRepository.findById(1L)).willReturn(Optional.of(new Station("강남역")));
        given(stationRepository.findById(2L)).willReturn(Optional.of(new Station("양재역")));
        given(lineRepository.findAll()).willReturn(Collections.emptyList());
        given(pathFinder.getShortestPath(any())).willReturn(new PathResponse());

        //when
        PathResponse pathResponse = pathService.get(1, 2);

        //then
        assertThat(pathResponse.getDistance()).isZero();
        assertThat(pathResponse.getStations()).isEmpty();
    }
}
