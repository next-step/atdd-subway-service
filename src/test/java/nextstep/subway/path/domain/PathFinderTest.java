package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회 테스트")
public class PathFinderTest {

    private Station 서울역 = new Station("서울역");
    private Station 시청역 = new Station("시청역");
    private Station 종각역 = new Station("종각역");
    private Station 종로3가역 = new Station("종로3가역");
    private Station 아현역 = new Station("아현역");
    private Station 충정로역 = new Station("충정로역");
    private Station 을지로입구역 = new Station("을지로입구역");
    private Station 애오개역 = new Station("애오개역");
    private Station 서대문역 = new Station("서대문역");
    private Station 광화문역 = new Station("광화문역");

    private Line 일호선 = new Line("일호선", "blue");
    private Line 이호선 = new Line("이호선", "green");
    private Line 오호선 = new Line("오호선", "purple");

    @BeforeEach
    void setup() {
        일호선.addSection(new Section(서울역, 시청역, 10));
        일호선.addSection(new Section(시청역, 종각역, 9));
        일호선.addSection(new Section(종각역, 종로3가역, 10));
        이호선.addSection(new Section(아현역, 충정로역, 10));
        이호선.addSection(new Section(충정로역, 시청역, 10));
        이호선.addSection(new Section(시청역, 을지로입구역, 10));
        오호선.addSection(new Section(애오개역, 충정로역, 10));
        오호선.addSection(new Section(충정로역, 서대문역, 10));
        오호선.addSection(new Section(서대문역, 광화문역, 10));
        오호선.addSection(new Section(광화문역, 종로3가역, 10));
    }

    @DisplayName("최단 경로 찾기")
    @Test
    void findPathByPoints() {
        // Given
        PathFinder pathFinder = new PathFinder(new Lines(이호선, 오호선));

        // When
        List<Station> stations = pathFinder.findPath(서대문역, 시청역);

        // Then
        assertThat(stations).containsExactly(서대문역, 충정로역, 시청역);
    }

    @DisplayName("최단 경로 찾기 - 거쳐가는 지하철역 개수가 동일할 경우 가중치가 적은 경로 탐색")
    @Test
    void findPathByPointsWithDistance() {
        // Given
        PathFinder pathFinder = new PathFinder(new Lines(일호선, 이호선, 오호선));

        // When
        List<Station> stations = pathFinder.findPath(광화문역, 시청역);

        // Then
        assertThat(stations).containsExactly(광화문역, 종로3가역, 종각역, 시청역);
    }

}
