package nextstep.subway.path;

import com.google.common.collect.Lists;
import nextstep.subway.line.LineTestFixture;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTest {
    private final Station upStationFirstLine = new Station("소사역");
    private final Station downStationFirstLine = new Station("온수역");
    private final Station addStationFirstLine = new Station("역곡역");
    private final Station upStationSecondLine = new Station("부평역");
    private final Station downStationSecondLine = new Station("소사역");
    private final Station upStationThirdLine = new Station("부평역");
    private final Station downStationThirdLine = new Station("인천역");
    private Line firstLine;

    @BeforeEach
    void setUp() {
        /**
         * 4L    --- *2호선* ---    1L
         * |                        |
         * *3호선*                   *1호선*
         * |                        |
         * 3L  --- *1호선* ---      2L
         */

        firstLine = LineTestFixture.노선을_생성한다("1호선", "red", upStationFirstLine, downStationFirstLine, 10);

    }

    @Test
    @DisplayName("노선이 1개일때 지하철 최단 경로를 조회한다.")
    void findPathTestOneLine() {
        firstLine.addStation(downStationFirstLine, addStationFirstLine, 5);
        PathFinder pathFinder = new PathFinder(firstLine);
        Path path = pathFinder.findPath(upStationFirstLine, addStationFirstLine);
        assertThat(path.getStations()).containsExactly(upStationFirstLine, downStationFirstLine, addStationFirstLine);
    }


    @Test
    @DisplayName("노선이 여러개일때 지하철 최단 경로를 조회한다.")
    void findPathTestManyLine() {
        firstLine.addStation(downStationFirstLine, addStationFirstLine, 5);
        Line secondLine = LineTestFixture.노선을_생성한다("2호선", "red", upStationSecondLine, downStationSecondLine, 10);
        Line thirdLine = LineTestFixture.노선을_생성한다("3호선", "red", upStationThirdLine, downStationThirdLine, 20);
        List<Line> lines = Lists.newArrayList(firstLine, secondLine, thirdLine);
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPath(upStationSecondLine, downStationFirstLine);
        assertThat(path.getStations()).containsExactly(upStationSecondLine, upStationFirstLine, downStationFirstLine);
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 실패한다.")
    void sameSourceAndTarget() {
        PathFinder pathFinder = new PathFinder(firstLine);
        assertThatThrownBy(() -> pathFinder.findPath(upStationFirstLine, upStationFirstLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역과 역 사이의 거리는 0보다 커야합니다");
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 실패한다.")
    void notConnectedStation() {
        Line thirdLine = LineTestFixture.노선을_생성한다("3호선", "red", upStationThirdLine, downStationThirdLine, 20);
        PathFinder pathFinder = new PathFinder(Lists.newArrayList(firstLine, thirdLine));
        assertThatThrownBy(() -> pathFinder.findPath(upStationThirdLine, downStationFirstLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("경로를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 출발역이나 도착역을 조회하면 실패한다.")
    void notExistStaion() {
        PathFinder pathFinder = new PathFinder(firstLine);
        assertThatThrownBy(() -> pathFinder.findPath(upStationThirdLine, downStationFirstLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역 또는 도착역이 노선에 존재하지 않습니다");
    }
}