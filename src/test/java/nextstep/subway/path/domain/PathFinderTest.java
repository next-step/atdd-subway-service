package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {
    Station 교대역 = new Station(1L, "교대역");
    Station 양재역 = new Station(2L, "양재역");
    Station 강남역 = new Station(3L, "강남역");

    Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
    Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 1);
    Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

    @Test
    void findPath() {
        // when
        PathResponse pathResponse = PathFinder.findPath(Arrays.asList(신분당선, 이호선, 삼호선), 양재역, 강남역);

        // Then
        Assertions.assertAll(
                () -> assertThat(pathResponse.getStations().stream()
                        .map(stationResponse -> stationResponse.getId())
                        .collect(Collectors.toList())).contains(양재역.getId(), 교대역.getId(), 강남역.getId()),
                () -> assertThat(pathResponse.getStations().stream()
                        .map(stationResponse -> stationResponse.getName())
                        .collect(Collectors.toList())).contains(양재역.getName(), 교대역.getName(), 강남역.getName())
        );
    }
}