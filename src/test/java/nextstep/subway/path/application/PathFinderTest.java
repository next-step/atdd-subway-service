package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResult;
import org.junit.jupiter.api.Test;

import static nextstep.subway.path.fixture.PathFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {

    @Test
    void findShortestPath_최단_경로를_조회한다() {
        PathFinder pathFinder = new PathFinder(new DijkstraGraph());
        PathResult response = pathFinder.findShortestPath(구간, 교대역, 양재역);
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(20),
                () -> assertThat(response.getStations()).containsExactly(교대역, 선릉역, 양재역));
    }
}