package nextstep.subway.path.application;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationRepository stationRepository;

    private Station 선릉역;
    private Station 정자역;
    private Station 수원역;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station("선릉역");
        정자역 = new Station("정자역");
        수원역 = new Station("수원역");
    }

    @Test
    void findPath_sameLine() {
        // given
        when(stationRepository.findAll()).thenReturn(Arrays.asList(선릉역, 정자역, 수원역));

        // when
        StationService stationService = new StationService(stationRepository);
        PathService pathService = new PathService(stationService);
        PathResponse result = pathService.findPath(1L, 3L);

        // then
        assertThat(result.getStations()).containsExactly(선릉역, 정자역, 수원역);
        assertThat(result.getDistance()).isEqualTo(50);
    }
}