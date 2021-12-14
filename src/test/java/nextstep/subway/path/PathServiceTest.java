package nextstep.subway.path;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.*;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("mockito의 MockitoExtension을 활용한 가짜 협력 객체 사용하여 경로조회 테스트한다.")
    @Test
    void findShortestPath() {
        // given
        LineRepository lineRepository = mock(LineRepository.class);
        StationService stationService = mock(StationService.class);

        when(stationService.findStationById(1L)).thenReturn(Station.from("교대역"));
        when(stationService.findStationById(2L)).thenReturn(Station.from("양재역"));
        when(lineRepository.findAll())
                .thenReturn(Lists.newArrayList(
                        Line.of("이호선", "blue", Sections.from(
                                Section.of(Station.from("교대역"), Station.from("강남역"), Distance.of(10)))
                        ),
                        Line.of("삼호선", "blue", Sections.from(Lists.newArrayList(
                                Section.of(Station.from("교대역"), Station.from("남부터미널역"), Distance.of(5)),
                                Section.of(Station.from("남부터미널역"), Station.from("양재역"), Distance.of(3))))
                        ),
                        Line.of("신분당선", "blue", Sections.from(
                                        Section.of(Station.from("강남역"), Station.from("양재역"), Distance.of(10)))
                        )
                ));
        PathService pathService = new PathService(lineRepository, stationService);

        // when
        PathResponse responses = pathService.findShortestPath(1L, 2L);

        // then
        assertAll(
                () -> assertThat(responses.getStations()).hasSize(3),
                () -> assertThat(responses.getDistance()).isEqualTo(8.0),
                () -> assertThat(responses.getStations()).containsExactly(
                        StationResponse.of(Station.from("교대역")), StationResponse.of(Station.from("남부터미널역")),
                        StationResponse.of(Station.from("양재역")))
        );
    }
}
