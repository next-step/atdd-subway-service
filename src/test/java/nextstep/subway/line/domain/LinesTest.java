package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LinesTest {
    public static final Station first = new Station("FIRST");
    public static final Station second = new Station("SECOND");

    public static final Line FIRST_LINE = new Line("1호선", "1호선", 1000, first, second, 3);
    public static final Line SECOND_LINE = new Line("2호선", "2호선", 2000, first, second, 2);
    public static final Line THIRD_LINE = new Line("3호선", "3호선", 3000, first, second, 2);
    public static final Line FOURTH_LINE = new Line("4호선", "4호선", 500, first, second, 3);
    public static final Line FIFTH_LINE = new Line("5호선", "5호선", 7000, first, second, 3);

    @Test
    @DisplayName("가장 비싼 운임을 선택한다")
    void 가장_비싼_운임을_선택한다() {
        Lines lines = new Lines(
                Arrays.asList(
                        FIRST_LINE,
                        SECOND_LINE,
                        THIRD_LINE,
                        FOURTH_LINE,
                        FIFTH_LINE
                )
        );

        assertThat(lines.findExpensiveFare())
                .isEqualTo(new Money(7000));
    }
}