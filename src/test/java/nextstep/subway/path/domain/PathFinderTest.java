package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {
    Station 교대역 = new Station(1L, "교대역");
    Station 양재역 = new Station(2L, "양재역");
    Station 강남역 = new Station(3L, "강남역");
    Station 고속버스터미널역 = new Station(4L, "고속버스터미널역");
    Station 수서역 = new Station(5L, "수서역");

    Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 700);
    Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 1);
    Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
    Line 육호선 = new Line("육호선", "bg-red-600", 강남역, 고속버스터미널역, 3, 400);
    Line 칠호선 = new Line("칠호선", "bg-red-600", 고속버스터미널역, 수서역, 7, 500);

    @Test
    void findPath() {
        // when
        PathFinder pathFinder = new PathFinder(new DijkstraShortestPathCalculator());
        PathResponse pathResponse = pathFinder.findPath(Arrays.asList(신분당선, 이호선, 삼호선), 양재역, 강남역);

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

    @Test
    @DisplayName("여러 라인 포함된 경로 추가 요금 조회")
    void findPathContainsLines() {
        // When
        PathFinder pathFinder = new PathFinder(new DijkstraShortestPathCalculator());
        PathResponse pathResponse = pathFinder.findPath(Arrays.asList(신분당선, 이호선, 삼호선, 육호선, 칠호선), 양재역, 수서역);

        // Then
        Assertions.assertAll(
                () -> assertThat(pathResponse.getStations().stream()
                        .map(stationResponse -> stationResponse.getId())
                        .collect(Collectors.toList())).contains(양재역.getId(), 교대역.getId(), 강남역.getId(), 고속버스터미널역.getId(), 수서역.getId()),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1750));

    }
}