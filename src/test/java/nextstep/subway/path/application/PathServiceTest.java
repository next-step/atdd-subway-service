package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationRepository stationRepository;

    @Test
    void findPath_sameLine() {
        // given
        when(stationRepository.findAll()).thenReturn(Arrays.asList(new Station("선릉역"), new Station("정자역"), new Station("수원역")));

        // when
        StationService stationService = new StationService(stationRepository);
        PathService pathService = new PathService(stationService);
        PathResponse result = pathService.findPath(1L, 3L);

        // then
        assertThat(result.getStations()).hasSize(3);
        assertThat(result.getDistance()).isEqualTo(50);
    }
}