package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회 테스트")
public class PathFinderTest {

    private Station 서울역;
    private Station 시청역;
    private Station 종각역;
    private Station 종로3가역;
    private Station 아현역;
    private Station 충정로역;
    private Station 을지로입구역;
    private Station 애오개역;
    private Station 서대문역;
    private Station 광화문역;

    private Line 일호선;
    private Line 이호선;
    private Line 오호선;

    @BeforeEach
    void setup() {
        서울역 = new Station("서울역");
        시청역 = new Station("시청역");
        종각역 = new Station("종각역");
        종로3가역 = new Station("종로3가역");
        아현역 = new Station("아현역");
        충정로역 = new Station("충정로역");
        을지로입구역 = new Station("을지로입구역");
        애오개역 = new Station("애오개역");
        서대문역 = new Station("서대문역");
        광화문역 = new Station("광화문역");

        일호선 = new Line("일호선", "blue");
        일호선.addSection(new Section(서울역, 시청역, 10));
        일호선.addSection(new Section(시청역, 종각역, 9));
        일호선.addSection(new Section(종각역, 종로3가역, 10));

        이호선 = new Line("이호선", "green");
        이호선.addSection(new Section(아현역, 충정로역, 10));
        이호선.addSection(new Section(충정로역, 시청역, 10));
        이호선.addSection(new Section(시청역, 을지로입구역, 10));

        오호선 = new Line("오호선", "purple");
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
        PathResponse pathResponse = pathFinder.findPath(서대문역, 시청역);

        // Then
        지하철_최단_경로_목록_정렬됨(pathResponse.getStations(), 서대문역, 충정로역, 시청역);
    }

    @DisplayName("최단 경로 찾기 - 거쳐가는 지하철역 개수가 동일할 경우 가중치가 적은 경로 탐색")
    @Test
    void findPathByPointsWithDistance() {
        // Given
        PathFinder pathFinder = new PathFinder(new Lines(일호선, 이호선, 오호선));

        // When
        PathResponse pathResponse = pathFinder.findPath(광화문역, 시청역);

        // Then
        지하철_최단_경로_목록_정렬됨(pathResponse.getStations(), 광화문역, 종로3가역, 종각역, 시청역);
    }

    private void 지하철_최단_경로_목록_정렬됨(List<StationResponse> stationResponses, Station... expectStations) {
        List<String> stationNames = stationResponses.stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());
        List<String> expectStationNames = Arrays.stream(expectStations)
            .map(Station::getName)
            .collect(Collectors.toList());
        assertThat(stationNames).hasSameElementsAs(expectStationNames);
    }

}
