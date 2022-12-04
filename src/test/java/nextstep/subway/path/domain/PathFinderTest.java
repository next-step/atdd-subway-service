package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.path.domain.PathFinder.SOURCE_TARGET_NOT_SAME_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("PathFinderDomain")
class PathFinderTest {


    private Line lineA;
    private Line lineB;
    private Line lineC;
    private Line lineD;
    private Line lineE;

    private Station stationA;
    private Station stationB;
    private Station stationC;
    private Station stationD;
    private Station stationE;
    private Station stationF;

    /**
     * stationA  --- lineD    --- stationD                      stationF
     * /             거리 1        /                              /
     * *lineA* 거리 5                lineC 거리 1               *lineE* 거리 2
     * /                             /                            /
     * stationB  ---   lineB ---  stationC                      stationE
     * 거리 3
     */
    @BeforeEach
    void setUp() {
        stationA = new Station("A");
        stationB = new Station("B");
        stationC = new Station("C");
        stationD = new Station("D");
        stationE = new Station("E");
        stationF = new Station("F");

        lineA = Line.of("A", "RED");
        lineB = Line.of("B", "BLUE");
        lineC = Line.of("C", "GREEN");
        lineD = Line.of("D", "YELLOW");
        lineE = Line.of("E", "ORANGE");

        lineA.addSection(new Section(lineA, stationA, stationB, new Distance(5)));
        lineB.addSection(new Section(lineB, stationB, stationC, new Distance(3)));
        lineC.addSection(new Section(lineC, stationC, stationD, new Distance(1)));
        lineD.addSection(new Section(lineD, stationD, stationA, new Distance(1)));
        lineE.addSection(new Section(lineE, stationE, stationF, new Distance(2)));
    }

    @DisplayName("지하철 경로 조회 시 출발역과 도착역이 같을 경우 예외 발생")
    @Test
    void findPath_fail_sameStation() {

        PathFinder pathFinder = new PathFinder(Arrays.asList(lineA, lineB, lineC, lineD, lineE));

        assertThatThrownBy(() -> pathFinder.findPath(stationA, stationA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(SOURCE_TARGET_NOT_SAME_EXCEPTION_MESSAGE);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 조회할 수 없다.")
    @Test
    void findPath_fail_notConnect() {

        PathFinder pathFinder = new PathFinder(Arrays.asList(lineA, lineB, lineC, lineD, lineE));

        assertThatThrownBy(() -> pathFinder.findPath(stationA, stationE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단 경로를 조회")
    @Test
    void findPath_success() {

        PathFinder pathFinder = new PathFinder(Arrays.asList(lineA, lineB, lineC, lineD, lineE));

        assertAll(
                () -> assertThat(pathFinder.findPath(stationA, stationC).findStations()).containsExactly(stationA, stationD, stationC),
                () -> assertThat(pathFinder.findPath(stationA, stationC).findDistance()).isEqualTo(new Distance(2))
        );
    }
}
