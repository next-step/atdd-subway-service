package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFare;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathMap;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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
    @Mock
    private SectionRepository sectionRepository;

    private Station 선릉역;
    private Station 정자역;
    private Station 수원역;
    private Line 분당선;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station(1L, "선릉역");
        정자역 = new Station(2L, "정자역");
        수원역 = new Station(3L, "수원역");
        분당선 = new Line("분당선", "bg-yellow-400", 선릉역, 정자역, 40, 1000);
        분당선.addLineStation(new Section(분당선, 정자역, 수원역, 15));
    }

    @Test
    void findPath_sameLine() {
        // given
        when(lineRepository.findAll()).thenReturn(Collections.singletonList(분당선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(선릉역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(수원역));
        when(sectionRepository.findAll()).thenReturn(Arrays.asList(new Section(분당선, 선릉역, 정자역, 40), new Section(분당선, 정자역, 수원역, 15)));

        // when
        StationService stationService = new StationService(stationRepository);
        LineService lineService = new LineService(lineRepository, stationService);
        PathService pathService = new PathService(new PathFinder(), new PathMap(), new PathFare(), stationService, lineService, sectionRepository);
        PathResponse result = pathService.findShortestPath(new LoginMember(), 1L, 3L);

        // then
        assertThat(result.toStations()).containsExactly(선릉역, 정자역, 수원역);
        assertThat(result.getDistance()).isEqualTo(55);
        assertThat(result.getFare()).isEqualTo(3150);
    }
}
