package nextstep.subway.component;

import nextstep.subway.component.domain.SubwayPath;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    @Test
    void shortestPath() {
        // SubwayPath를 반환한다.
        // given
        PathFinder pathFinder = new PathFinder();
        Station 광교역 = new Station(2L, "광교역");
        Station 강남역 = new Station(1L, "강남역");
        Station 교대역 = new Station(3L, "교대역");

        Line 신분당선 = new Line(1L, "신분당선");
        Line 삼호선 = new Line(2L, "삼호선");
        신분당선.addSection(new Section(7L, 강남역, 광교역, 10));
        삼호선.addSection(new Section(8L, 교대역, 강남역, 5));
        List<Line> lines = Arrays.asList(신분당선, 삼호선);

        // when
        // jgrapht 라이브러리를 이용하여 최단 거리 경로를 계산한다.
        SubwayPath subwayPath = pathFinder.shortestPath(lines, 광교역, 교대역);

        // then
        assertThat(subwayPath.calcTotalDistance()).isGreaterThan(0);
        assertThat(subwayPath.getStations().size()).isGreaterThan(0);


    }
}
