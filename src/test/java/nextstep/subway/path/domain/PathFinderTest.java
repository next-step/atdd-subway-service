package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathFinderTest {

    @DisplayName("최적 경로를 검색한다.")
    @Test
    void findShortestPath_test() {
        //given
        Station 강남역 = new Station("강남역");
        Station 까치산역 = new Station("까치산역");
        Station 발산역 = new Station("발산역");
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 20);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 10);

        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역);
        List<Station> stations = pathFinder.findShortest(List.of(sourceLine, targetLine));

        //then
        assertThat(stations).containsExactly(
                강남역,
                까치산역,
                발산역
        );
    }

    @DisplayName("최적 경로 검색 시 출발역과 도착역은 같을 수 없다.")
    @Test
    void validateEquals_test() {
        //given
        Station 강남역 = new Station("강남역");

        //when
        assertThrows(IllegalArgumentException.class,
                () -> new PathFinder(강남역, 강남역)
        );
    }

    @DisplayName("최적 경로 검색 시 출발역과 도착역이 연결이 되어 있어야 한다.")
    @Test
    void validateConnected_test() {
        //given
        Station 강남역 = new Station("강남역");
        Station 까치산역 = new Station("까치산역");
        Station 발산역 = new Station("발산역");
        Station 김포공항역 = new Station("김포공항역");
        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 20);
        Line targetLine = new Line("5호선", "purple", 김포공항역, 발산역, 10);

        //when
        PathFinder pathFinder = new PathFinder(강남역, 발산역);

        assertThrows(IllegalArgumentException.class,
                () -> pathFinder.findShortest(List.of(sourceLine, targetLine))
        );
    }

    @DisplayName("최적 경로 검색 시 존재하지 않는 출발역이나 도착역은 조회할 수 없다.")
    @Test
    void validateNotExist_test() {
        //given
        Station 강남역 = new Station("강남역");
        Station 까치산역 = new Station("까치산역");
        Station 발산역 = new Station("발산역");
        Station 김포공항역 = new Station("김포공항역");

        Line sourceLine = new Line("2호선", "green", 강남역, 까치산역, 20);
        Line targetLine = new Line("5호선", "purple", 까치산역, 발산역, 10);

        //when
        PathFinder pathFinder = new PathFinder(강남역, 김포공항역);

        assertThrows(IllegalArgumentException.class,
                () -> pathFinder.findShortest(List.of(sourceLine, targetLine))
        );
    }
}
