package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private Lines lines;
    private Station 선릉역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    /**
     *               선릉역
     *                 |
     * 신논현역   ---   강남역   ---   양재역
     *                              |
     *                           남부터미널역
     */

    @BeforeEach
    void setUp() {
        선릉역 = Station.from("선릉역");
        남부터미널역 = Station.from("남부터미널역");
        강남역 = Station.from("강남역");
        양재역 = Station.from("양재역");
        Station 신논현역 = Station.from("신논현역");

        Section 신분당선_구간 = Section.of(강남역, 양재역, Distance.from(5));

        Line 이호선 = Line.of("이호선", "bg-green-600", Section.of(선릉역, 강남역, Distance.from(3)));
        Line 삼호선 = Line.of("삼호선", "bg-orange-600", Section.of(양재역, 남부터미널역, Distance.from(5)));
        Line 신분당선 = Line.of("신분당선", "bg-red-600", Section.of(신논현역, 양재역, Distance.from(10)));
        신분당선.addSection(신분당선_구간);

        lines = Lines.from(Arrays.asList(이호선, 삼호선, 신분당선));
    }

    @Test
    @DisplayName("최단 경로 조회하기")
    void findShortestPath() {
        PathFinder pathFinder = PathFinder.from(lines);
        Path actual = pathFinder.findShortestPath(선릉역, 남부터미널역);

        assertAll(
                () -> assertThat(actual.stations()).hasSize(4),
                () -> assertThat(actual.stations()).containsExactly(선릉역, 강남역, 양재역, 남부터미널역),
                () -> assertThat(actual.distance()).isEqualTo(13)
        );
    }
}
