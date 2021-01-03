package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 조회")
class PathFinderTest {

    private Lines lines;
    private Station 교대역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        Station 남부터미널 = new Station("남부터미널");
        Station 강남역 = new Station("강남역");

        Line 신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 5);
        Line 삼호선 = new Line("3호선", "orange", 교대역, 남부터미널, 1);
        Line 이호선 = new Line("2호선", "orange", 교대역, 강남역, 5);

        lines = new Lines(삼호선, 이호선, 신분당선);
    }

    @DisplayName("경로를 조회하고 순서가 일치하는지 확인한다.")
    @Test
    void getShortestPath() {
        PathFinder pathFinder = new PathFinder(lines.allSection());

        assertThat(pathFinder.getWeight(교대역, 양재역)).isEqualTo(10);
        assertThat(pathFinder.getPath(교대역, 양재역))
                .extracting("name")
                .containsExactly("교대역", "강남역", "양재역");
    }
}