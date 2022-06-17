package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    private Station 선릉역;
    private Station 정자역;
    private Station 수원역;
    private Line 분당선;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station(1L, "선릉역");
        정자역 = new Station(2L, "정자역");
        수원역 = new Station(3L, "수원역");
        분당선 = new Line("분당선", "bg-yellow-400");
        분당선.addLineStation(new Section(분당선, 선릉역, 정자역, 40));
        분당선.addLineStation(new Section(분당선, 정자역, 수원역, 10));
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
}