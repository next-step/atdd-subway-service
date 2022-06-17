package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    private Station 선릉역;
    private Station 정자역;
    private Station 수원역;
    private Station 춘천역;
    private Station 강릉역;
    private Line 분당선;
    private Line 강릉선;
    private Line 당릉선;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station(1L, "선릉역");
        정자역 = new Station(2L, "정자역");
        수원역 = new Station(3L, "수원역");
        춘천역 = new Station(4L, "춘천역");
        강릉역 = new Station(5L, "강릉역");
        분당선 = new Line("분당선", "bg-yellow-400");
        강릉선 = new Line("강릉선", "bg-blue-600");
        당릉선 = new Line("당릉선", "bg-blue-600");
        분당선.addLineStation(new Section(분당선, 선릉역, 정자역, 40));
        분당선.addLineStation(new Section(분당선, 정자역, 수원역, 10));
        강릉선.addLineStation(new Section(강릉선, 정자역, 춘천역, 40));
        강릉선.addLineStation(new Section(강릉선, 춘천역, 강릉역, 20));
        당릉선.addLineStation(new Section(당릉선, 수원역, 춘천역, 10));
    }

    @Test
    void findPath_sameLine() {
        // when
        PathFinder pathFinder = new PathFinder();
        pathFinder.addLineStation(분당선);
        PathResponse result = pathFinder.findShortestPath(선릉역, 수원역);

        // then
        assertThat(result.getStations()).containsExactly(선릉역, 정자역, 수원역);
        assertThat(result.getDistance()).isEqualTo(50);
    }

    @Test
    void findPath_differentLines() {
        // when
        PathFinder pathFinder = new PathFinder();
        pathFinder.addLineStations(Arrays.asList(분당선, 강릉선, 당릉선));
        PathResponse result = pathFinder.findShortestPath(선릉역, 강릉역);

        // then
        assertThat(result.getStations()).containsExactly(선릉역, 정자역, 수원역, 춘천역, 강릉역);
        assertThat(result.getDistance()).isEqualTo(80);
    }
}