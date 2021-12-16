package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
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
        /**
         * 교대역    --- *2호선* ---   강남역
         * |                        |
         * *3호선*                   *신분당선*
         * |                        |
         * 남부터미널역  --- *3호선* ---   양재
         */
        final Station 강남역 = new Station("강남역");
        final Station 양재역 = new Station("양재역");
        final Station 교대역 = new Station("교대역");
        final Station 남부터미널역 = new Station("남부터미널역");
        final Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        final Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        final Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(new Section(교대역, 남부터미널역, 3));

        final PathRequest pathRequest = new PathRequest(1L, 2L);
        when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.getStationById(pathRequest.getSource())).thenReturn(강남역);
        when(stationService.getStationById(pathRequest.getTarget())).thenReturn(양재역);

        // when
        final PathResponse actual = pathService.findPath(null, pathRequest);

        // then
        final List<String> actualStationNames = actual.getStations()
            .stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());
        assertAll(
            () -> assertThat(actualStationNames).containsExactly(강남역.getName(), 양재역.getName()),
            () -> assertThat(actual.getDistance()).isEqualTo(10)
        );
    }
}
