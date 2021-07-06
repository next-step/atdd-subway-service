package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    @DisplayName("최적 경로를 검색한다.")
    @Test
    void findShortestPath() {
        //given
        Station 강남역 = new Station("강남역");
        Station 까치산역 = new Station("까치산역");
        Station 발산역 = new Station("발산역");
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 20);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 10);

        List<Station> stations = PathFinder.findShortest(강남역, 발산역, List.of(sourceLine, targetLine));

        assertThat(stations).containsExactly(
                강남역,
                까치산역,
                발산역
        );
    }
}
