package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    @Test
    void findPath() {
        // given
        final PathRequest pathRequest = new PathRequest(1L, 2L);

        // when
        final PathResponse actual = pathService.findPath(pathRequest);

        // then
        assertAll(
            () -> assertThat(actual.getStations()).isEmpty(),
            () -> assertThat(actual.getDistance()).isZero()
        );
    }
}
