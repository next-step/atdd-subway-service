package nextstep.subway.line.domain;

import nextstep.subway.exception.LineHasNotExistShortestException;
import nextstep.subway.line.domain.wrapped.Distance;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DijkstraShortestDistanceTest {
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 광교역 = new Station("광교역");
    private Station 정자역 = new Station("정자역");

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        정자역 = new Station("정자역");
    }

    @Test
    @DisplayName("없는 역이면 LineHasNotExistShortestException이 발생한다")
    void 없는_역이면_LineHasNotExistShortestException이_발생한다() {
        Line 신분당선 = new Line("신분당", "RED", 양재역, 정자역, 3);
        Line 분당선 = new Line("분당", "YELLO", 광교역, 양재역, 3);

        신분당선.addSection(new Section(광교역, 정자역, new Distance(1)));
        분당선.addSection(new Section(양재역, 정자역, new Distance(1)));

        List<Line> lines = Arrays.asList(신분당선, 분당선);

        assertThatExceptionOfType(LineHasNotExistShortestException.class)
                .isThrownBy(() -> new DijkstraShortestDistance(lines, 강남역, 양재역).shortestDistance());
        assertThatExceptionOfType(LineHasNotExistShortestException.class)
                .isThrownBy(() -> new DijkstraShortestDistance(lines, 강남역, 양재역).shortestRoute());
    }

    @Test
    @DisplayName("최단거리를 구해올 수 있다")
    void 최단거리를_구해올_수_있다() {
        Line 신분당선 = new Line("신분당", "RED", 강남역, 양재역, 3);
        Line 이호선 = new Line("이호선", "YELLO", 강남역, 정자역, 7);
        Line 삼호선 = new Line("삼호선", "ORANGE", 강남역, 광교역, 2);

        신분당선.addSection(new Section(신분당선, 양재역, 정자역, 2));
        이호선.addSection(new Section(이호선, 양재역, 정자역, 5));
        삼호선.addSection(new Section(삼호선, 광교역, 정자역, 1));

        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);

        DijkstraShortestDistance dijkstraShortestDistance = new DijkstraShortestDistance(lines, 강남역, 정자역);

        assertThat(dijkstraShortestDistance.shortestRoute().toCollection())
                .containsExactly(강남역, 광교역, 정자역);
        assertThat(dijkstraShortestDistance.shortestDistance())
                .isEqualTo(new Distance(3));
    }
}