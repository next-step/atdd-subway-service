package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;

@DisplayName("경로 서비스 관련 기능")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    @Test
    void 최단_경로_조회() {
        // given
        givenStation(1L, "강남역");
        givenStation(2L, "양재역");
        givenStation(3L, "양재시민의숲");

        Station 강남역 = stationService.findStationById(1L);
        Station 양재역 = stationService.findStationById(2L);
        Station 양재시민의숲 = stationService.findStationById(3L);

        Line line = Line.of("신분당선", "red", 강남역, 양재역, 10);
        line.addSection(양재역, 양재시민의숲, 2);

        given(lineService.findLines()).willReturn(Lines.from(Collections.singletonList(line)));

        // when
        PathResponse path = pathService.findPath(PathRequest.of(강남역.getId(), 양재시민의숲.getId()));

        // then
        Assertions.assertThat(path.getStations())
                .extracting("name")
                .containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "양재시민의숲"));
        Assertions.assertThat(path.getDistance()).isEqualTo(12);
        Assertions.assertThat(path.getFare()).isEqualTo(BigDecimal.valueOf(1250));
    }

    private void givenStation(long id, String name) {
        given(stationService.findStationById(id)).willReturn(Station.of(id, name));
    }
}
