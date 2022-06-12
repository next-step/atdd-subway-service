package nextstep.subway.path.domain;

import nextstep.subway.exception.InvalidPathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {
    private Station 종합운동장;
    private Station 잠실새내;
    private Station 잠실;
    private Station 석촌;
    private Station 천호;
    private Station 하남시청;

    private Line 이호선;
    private Line 오호선;
    private Line 팔호선;

    @DisplayName("최단 경로 객체 생성")
    @Test
    void create() {
        // when
        PathFinder pathFinder = new PathFinder(createLines());

        // then
        assertThat(pathFinder).isNotNull();
    }

    @DisplayName("최단 경로에 포함된 지하철 역 리스트 조회")
    @Test
    void findShortestPath() {
        // given
        PathFinder pathFinder = new PathFinder(createLines());

        // when
        List<Station> stations = pathFinder.findShortestStationList(종합운동장, 석촌);

        // then
        assertThat(stations).hasSize(4);
        assertThat(stations).containsExactly(종합운동장, 잠실새내, 잠실, 석촌);
    }

    @DisplayName("최단 경로 길이 구하기")
    @Test
    void findShortestPathLength() {
        // given
        PathFinder pathFinder = new PathFinder(createLines());

        // when
        double length = pathFinder.findShortestPathLength(종합운동장, 석촌);

        // then
        assertThat(length).isEqualTo(30);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void findShortestPath_exception01() {
        // given
        PathFinder pathFinder = new PathFinder(createLines());

        // when, then
        assertThatThrownBy(() -> {
            pathFinder.findShortestStationList(종합운동장, 종합운동장);
        }).isInstanceOf(InvalidPathException.class);
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우")
    @Test
    void findShortestPath_exception02() {
        // given
        PathFinder pathFinder = new PathFinder(createLines());

        // when, then
        assertThatThrownBy(() -> {
            pathFinder.findShortestStationList(종합운동장, 하남시청);
        }).isInstanceOf(InvalidPathException.class);
    }

    private List<Line> createLines() {
        종합운동장 = new Station(1L, "종합운동장");
        잠실새내 = new Station(2L, "잠실새내");
        잠실 = new Station(3L, "잠실");
        석촌 = new Station(4L, "석촌");
        천호 = new Station(5L, "천호");
        하남시청 = new Station(5L, "하남시청");

        이호선 = new Line("2호선", "green", 종합운동장, 잠실새내, 10);
        이호선.addSection(new Section(잠실새내, 잠실, 10));

        오호선 = new Line("5호선", "purple", 천호, 하남시청, 100);

        팔호선 = new Line("8호선", "pink", 잠실, 석촌, 10);

        return Arrays.asList(이호선, 오호선, 팔호선);
    }
}
