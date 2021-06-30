package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathFinderTest {

    private static Line upperCaseLine;
    private static Line lowerCaseLine;
    private static Line aA_Line, bB_Line, cC_Line;

    private static Station A, B, C;
    private static Station a, b, c;
    private static Station X, Y;

    /**
     * A - B - C
     * |   |   |
     * a - b - c
     */
    @BeforeAll
    public static void setup() {
        A = createStation("A");    B = createStation("B");    C = createStation("C");
        a = createStation("a");    b = createStation("b");    c = createStation("c");
        X = createStation("X");    Y = createStation("Y");

        upperCaseLine = appendSectionByLine("upperCaseLine",
            createSection(A, B, 100),
            createSection(B, C, 100)
        );

        lowerCaseLine = appendSectionByLine("lowerCaseLine",
            createSection(a, b, 1),
            createSection(b, c, 1)
        );

        aA_Line = appendSectionByLine("lineA",
            createSection(a, A, 2)
        );

        bB_Line = appendSectionByLine("lineB",
            createSection(b, B, 2)
        );

        cC_Line = appendSectionByLine("lineC",
            createSection(c, C, 2)
        );
    }

    @Test
    @DisplayName("하나의 일직선인 노선의 경로를 조회한다.")
    public void explore1() {
        // given
        PathFinder pathFinder = initPathFinder(upperCaseLine);

        // when
        Path path = pathFinder.getPath(A, C);

        // then
        assertAll(
            () -> assertThat(path.size()).isEqualTo(3),
            () -> assertThat(path.getPaths()).containsExactly(A, B, C),
            () -> assertThat(path.getSource()).isEqualTo(A),
            () -> assertThat(path.getTrget()).isEqualTo(C),
            () -> assertThat(path.getDistance()).isEqualTo(200)
        );
    }

    @Test
    @DisplayName("다수의 노선에서 경로를 조회한다.")
    public void explore2() {
        // given
        PathFinder pathFinder = initPathFinder(upperCaseLine, lowerCaseLine, aA_Line, bB_Line, cC_Line);

        // when
        Path path = pathFinder.getPath(A, C);

        // then
        assertAll(
            () -> assertThat(path.size()).isEqualTo(5),
            () -> assertThat(path.getPaths()).containsExactly(A, a, b, c, C),
            () -> assertThat(path.getSource()).isEqualTo(A),
            () -> assertThat(path.getTrget()).isEqualTo(C),
            () -> assertThat(path.getDistance()).isEqualTo(6)
        );
    }

    @Test
    @DisplayName("경로 조회시, 출발역과 도착역이 같은 경우 예외를 발생한다.")
    public void exception1() {
        // given
        PathFinder pathFinder = initPathFinder(upperCaseLine);

        // when
        assertThatThrownBy(() -> pathFinder.getPath(A, A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("경로 조회시, 출발역과 도착역이 연결이 되어 있지 않은 경우 예외를 발생한다.")
    public void exception2() {
        // given
        PathFinder pathFinder = initPathFinder(upperCaseLine, lowerCaseLine);

        // when
        assertThatThrownBy(() -> pathFinder.getPath(A, a))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("경로 조회시, 존재하지 않은 출발역이나 도착역을 조회 할 경우 예외를 발생한다.")
    public void exception3() {
        // given
        PathFinder pathFinder = initPathFinder(upperCaseLine);

        // when
        assertAll(
            () -> assertThatThrownBy(() -> pathFinder.getPath(A, X)).isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> pathFinder.getPath(X, A)).isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> pathFinder.getPath(X, Y)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    private PathFinder initPathFinder(Line ...line) {
        Lines lines = new Lines(Arrays.asList(line));
        return new PathFinder(lines);
    }

    private static Section createSection(final Station upStation, final Station downStation, final int distance) {
        return Section.builder().id(TestUtils.getRandomId())
                .upStation(upStation).downStation(downStation)
                .distance(distance)
                .build();
    }

    private static Station createStation(final String name) {
        return new Station(TestUtils.getRandomId(), name);
    }

    private static Line appendSectionByLine(final String lineName, final Section ...items) {
        Line line =  new Line(lineName, "");

        for(Section section : items) {
            line.addSection(section);
        }

        return line;
    }

}
