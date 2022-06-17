package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private PathService pathService;

    @Test
    void findPath_sameLine() {
        // given
        List<Station> stations = Arrays.asList(new Station("선릉역"), new Station("정자역"), new Station("수원역"));
        List<StationResponse> responses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        when(pathService.findPath(1L, 3L)).thenReturn(new PathResponse(responses, 50));

        // when
        PathResponse result = pathService.findPath(1L, 3L);
        List<String> names = result.getStations().stream().map(StationResponse::getName).collect(Collectors.toList());

        // then
        assertThat(names).containsExactly("선릉역", "정자역", "수원역");
        assertThat(result.getDistance()).isEqualTo(50);
    }
}