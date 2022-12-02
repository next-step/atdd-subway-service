package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {
    private final Line 이호선 = new Line("2호선", "green");
    private final Line 신분당선 = new Line("신분당선", "red");
    private final Line 삼호선 = new Line("3호선", "orange");
    private final Station 강남역 = new Station("강남역");
    private final Station 양재역 = new Station("양재역");
    private final Station 교대역 = new Station("교대역");
    private final Station 역삼역 = new Station("역삼역");
    private final Station 남부터미널역 = new Station("남부터미널역");
    private final Distance 거리_10 = new Distance(10);
    private final Distance 거리_5 = new Distance(5);

    @Test
    @DisplayName("출발역과 도착역이 같으면 예외가 발생한다.")
    void 출발역과_도착역이_같으면_예외_발생() {
        PathFinder pathFinder = new PathFinder(Collections.singletonList(new Section(이호선, 교대역, 강남역, 거리_10)));

        assertThatThrownBy(() -> pathFinder.getShortestPath(강남역, 강남역)).isInstanceOf(RuntimeException.class)
                .hasMessage("경로 조회가 불가능합니다.");
    }

    @Test
    @DisplayName("출발역이 그래프에 포함되어 있지 않으면 예외가 발생한다.")
    void 출발역이_그래프에_없으면_예외_발생() {
        PathFinder pathFinder = new PathFinder(Collections.singletonList(new Section(이호선, 강남역, 역삼역, 거리_10)));

        assertThatThrownBy(() -> pathFinder.getShortestPath(교대역, 강남역)).isInstanceOf(RuntimeException.class)
                .hasMessage("역이 그래프에 포함되지 않았습니다.");
    }

    @Test
    @DisplayName("도착역이 그래프에 포함되어 있지 않으면 예외가 발생한다.")
    void 도착역이_그래프에_없으면_예외_발생() {
        PathFinder pathFinder = new PathFinder(Collections.singletonList(new Section(이호선, 교대역, 강남역, 거리_10)));

        assertThatThrownBy(() -> pathFinder.getShortestPath(강남역, 역삼역)).isInstanceOf(RuntimeException.class)
                .hasMessage("역이 그래프에 포함되지 않았습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역의 최단 거리를 구한다.")
    void 출발역_도착역_최단_거리() {
        Section 강남역_양재역 = new Section(신분당선, 강남역, 양재역, 거리_10);
        Section 교대역_강남역 = new Section(이호선, 교대역, 강남역, 거리_10);
        Section 교대역_남부터미널역 = new Section(삼호선, 교대역, 남부터미널역, 거리_5);
        Section 남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 거리_5);

        PathFinder pathFinder = new PathFinder(Arrays.asList(
                강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));

        assertThat(pathFinder.getShortestPath(양재역, 교대역)).satisfies(path -> {
            assertThat(path.getStations()).containsExactly(양재역, 남부터미널역, 교대역);
            assertThat(path.getDistance()).isEqualTo(거리_10);
        });
    }
}
