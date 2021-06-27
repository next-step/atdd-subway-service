package nextstep.subway.path.domain;

import static nextstep.subway.station.domain.StationFixtures.광화문역;
import static nextstep.subway.station.domain.StationFixtures.명동역;
import static nextstep.subway.station.domain.StationFixtures.서대문역;
import static nextstep.subway.station.domain.StationFixtures.서울역;
import static nextstep.subway.station.domain.StationFixtures.시청역;
import static nextstep.subway.station.domain.StationFixtures.아현역;
import static nextstep.subway.station.domain.StationFixtures.애오개역;
import static nextstep.subway.station.domain.StationFixtures.을지로입구역;
import static nextstep.subway.station.domain.StationFixtures.종각역;
import static nextstep.subway.station.domain.StationFixtures.종로3가역;
import static nextstep.subway.station.domain.StationFixtures.충정로역;
import static nextstep.subway.station.domain.StationFixtures.회현역;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회 테스트")
public class PathFinderTest {

    private Line 일호선;
    private Line 이호선;
    private Line 사호선;
    private Line 오호선;

    @BeforeEach
    void setup() {

        일호선 = new Line("일호선", "blue", 0);
        일호선.addSection(new Section(서울역, 시청역, 10));
        일호선.addSection(new Section(시청역, 종각역, 8));
        일호선.addSection(new Section(종각역, 종로3가역, 10));

        이호선 = new Line("이호선", "green", 0);
        이호선.addSection(new Section(아현역, 충정로역, 10));
        이호선.addSection(new Section(충정로역, 시청역, 10));
        이호선.addSection(new Section(시청역, 을지로입구역, 10));

        사호선 = new Line("이호선", "green", 0);
        사호선.addSection(new Section(서울역, 회현역, 10));
        사호선.addSection(new Section(회현역, 명동역, 10));

        오호선 = new Line("오호선", "purple", 0);
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
        ShortestPath shortestPath = pathFinder.findPath(서대문역, 시청역);

        // Then
        지하철_최단_경로_목록_정렬됨(shortestPath.getStations(), 서대문역, 충정로역, 시청역);
    }

    @DisplayName("최단 경로 찾기 - 거쳐가는 지하철역 개수가 동일할 경우 가중치가 적은 경로 탐색")
    @Test
    void findPathByPointsWithDistance() {
        // Given
        PathFinder pathFinder = new PathFinder(new Lines(일호선, 이호선, 사호선, 오호선));

        // When
        ShortestPath shortestPath = pathFinder.findPath(광화문역, 명동역);

        // Then
        지하철_최단_경로_목록_정렬됨(shortestPath.getStations(), 광화문역, 종로3가역, 종각역, 시청역, 서울역, 회현역, 명동역);
    }

    private void 지하철_최단_경로_목록_정렬됨(List<Station> stations, Station... expectStations) {
        List<Long> stationIds = stations.stream()
            .map(Station::getId)
            .collect(Collectors.toList());
        List<Long> expectStationIds = Arrays.stream(expectStations)
            .map(Station::getId)
            .collect(Collectors.toList());
        assertThat(stationIds).hasSameElementsAs(expectStationIds);
    }

}
