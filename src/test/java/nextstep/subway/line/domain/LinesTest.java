package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static nextstep.subway.utils.DomainInitUtils.*;
import static nextstep.subway.utils.DomainInitUtils.createSection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LinesTest {
    private static Station A, B, a, b;
    private static Station X, Y, x, y;

    private static Line upperCaseABLine;
    private static Line lowerCaseABLine;
    private static Line upperCaseXYLine;
    private static Line lowerCaseXYLine;

    private static Lines lines;

    @BeforeAll
    public static void setup() {
        A = createStation("A");    B = createStation("B"); a = createStation("a");    b = createStation("b");
        X = createStation("X");    Y = createStation("Y"); x = createStation("x");    y = createStation("y");

        upperCaseABLine = appendSectionByLine("upperCaseLine", 500,
                createSection(A, B, 100)
        );

        upperCaseXYLine = appendSectionByLine("lineA", 1000,
                createSection(X, Y, 2)
        );

        lowerCaseABLine = appendSectionByLine("lowerCaseLine", 5,
                createSection(a, b, 1)
        );

        lowerCaseXYLine = appendSectionByLine("lineB", 10,
                createSection(x, y, 2)
        );

        lines = new Lines(Arrays.asList(upperCaseABLine, upperCaseXYLine, lowerCaseABLine, lowerCaseXYLine));
    }

    @DisplayName("노선중에 가장 비싼 요금을 구한다.")
    @Test
    public void getMostExpensiveChargeTest() {
        assertAll(
            () -> assertThat(lines.getMostExpensiveCharge(Arrays.asList(X, A, x, a))).isEqualTo(1000),
            () -> assertThat(lines.getMostExpensiveCharge(Arrays.asList(A, x, a))).isEqualTo(500),
            () -> assertThat(lines.getMostExpensiveCharge(Arrays.asList(x, a))).isEqualTo(10),
            () -> assertThat(lines.getMostExpensiveCharge(Arrays.asList(a))).isEqualTo(5)
        );
    }

}