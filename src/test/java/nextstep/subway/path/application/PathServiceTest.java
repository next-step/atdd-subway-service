package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.FareCalculateResolver;
import nextstep.subway.path.domain.FareDiscountResolver;
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
    private FareCalculateResolver fareCalculateResolver;
    @Mock
    private FareDiscountResolver fareDiscountResolver;
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
        Fare fare = new Fare(1000);
        given(fareCalculateResolver.resolve(anyLong())).willReturn(fare);
        given(fareDiscountResolver.resolve(any(), any())).willReturn(fare);

        //when
        PathResponse pathResponse = pathService.get(new LoginMember(0L, "email", 30), 1, 2);

        //then
        assertThat(pathResponse.getDistance()).isZero();
        assertThat(pathResponse.getStations()).isEmpty();
        assertThat(pathResponse.getFare()).isEqualTo(1010);
    }
}
