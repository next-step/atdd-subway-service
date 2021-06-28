package nextstep.subway.path.domain;

import nextstep.subway.exception.NoRouteException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.wrapped.Distance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DijkstraShortestDistanceTest {
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;
    private Station 정자역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        정자역 = new Station("정자역");
    }

    @Test
    @DisplayName("없는 역이면 IllegalArgumentException이 발생한다")
    void 없는_역이면_IllegalArgumentException이_발생한다() {
        // given
        Line 신분당선 = new Line("신분당", "RED", 0, 양재역, 정자역, 3);
        Line 분당선 = new Line("분당", "YELLO", 0, 광교역, 양재역, 3);

        신분당선.addSection(new Section(광교역, 정자역, new Distance(1)));
        분당선.addSection(new Section(양재역, 정자역, new Distance(1)));

        List<Line> lines = Arrays.asList(신분당선, 분당선);

        // when & then

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new DijkstraShortestDistance(lines, 강남역, 양재역).shortestDistance());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new DijkstraShortestDistance(lines, 강남역, 양재역).shortestRoute());
    }

    @Test
    @DisplayName("연결되지 않은 역이면 NoRouteException이 발생한다")
    void 연결되지_않은_역이면_IllegalArgumentException이_발생한다() {
        // given
        Line 신분당선 = new Line("신분당", "RED", 0, 강남역, 정자역, 3);
        Line 분당선 = new Line("분당", "YELLO", 0, 광교역, 양재역, 3);

        List<Line> lines = Arrays.asList(신분당선, 분당선);

        // when & then
        assertThatExceptionOfType(NoRouteException.class)
                .isThrownBy(() -> new DijkstraShortestDistance(lines, 강남역, 양재역).shortestDistance());
        assertThatExceptionOfType(NoRouteException.class)
                .isThrownBy(() -> new DijkstraShortestDistance(lines, 강남역, 양재역).shortestRoute());
    }

    @Test
    @DisplayName("최단거리를 구해올 수 있다")
    void 최단거리를_구해올_수_있다() {
        // given
        Line 신분당선 = new Line("신분당", "RED", 0, 강남역, 양재역, 3);
        Line 이호선 = new Line("이호선", "YELLO", 0, 강남역, 정자역, 7);
        Line 삼호선 = new Line("삼호선", "ORANGE", 0, 강남역, 광교역, 2);

        신분당선.addSection(new Section(신분당선, 양재역, 정자역, 2));
        이호선.addSection(new Section(이호선, 양재역, 정자역, 5));
        삼호선.addSection(new Section(삼호선, 광교역, 정자역, 1));

        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);

        // when
        DijkstraShortestDistance dijkstraShortestDistance = new DijkstraShortestDistance(lines, 강남역, 정자역);

        List<Station> shortestStations = dijkstraShortestDistance.shortestRoute().toCollection();
        Distance shortestDistance = dijkstraShortestDistance.shortestDistance();

        // then
        assertThat(shortestStations)
                .containsExactly(강남역, 광교역, 정자역);
        assertThat(shortestDistance)
                .isEqualTo(new Distance(3));
    }

    @Test
    @DisplayName("환승을 하여 최단거리를 구할 수 있다")
    void 환승을_하여_최단거리를_구할_수_있다() {
        // given
        Line 신분당선 = new Line("신분당", "RED", 0, 강남역, 양재역, 1);
        Line 이호선 = new Line("이호선", "YELLO", 0, 양재역, 정자역, 5);
        Line 삼호선 = new Line("삼호선", "ORANGE", 0, 강남역, 광교역, 10);

        이호선.addSection(new Section(이호선, 정자역, 광교역, 2));

        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);

        // when
        DijkstraShortestDistance dijkstraShortestDistance = new DijkstraShortestDistance(lines, 강남역, 광교역);

        List<Station> shortestStations = dijkstraShortestDistance.shortestRoute().toCollection();
        Distance shortestDistance = dijkstraShortestDistance.shortestDistance();

        // then
        assertThat(shortestStations)
                .containsExactly(강남역, 양재역, 정자역, 광교역);
        assertThat(shortestDistance)
                .isEqualTo(new Distance(8));
    }
}