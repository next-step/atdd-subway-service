package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.StationPair;
import nextstep.subway.station.domain.Station;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EfficientLinesTest {
    private Station first = new Station("FIRST");
    private Station second = new Station("SECOND");

    private Line FIRST_LINE = new Line("1호선", "1호선", 1000, first, second, 3);
    private Line SECOND_LINE = new Line("2호선", "2호선", 2000, first, second, 2);
    private Line THIRD_LINE = new Line("3호선", "3호선", 3000, first, second, 2);
    private Line FOURTH_LINE = new Line("4호선", "4호선", 500, first, second, 3);
    private Line FIFTH_LINE = new Line("5호선", "5호선", 7000, first, second, 3);

    private List<Line> lines = Arrays.asList(FIRST_LINE, SECOND_LINE, THIRD_LINE, FOURTH_LINE, FIFTH_LINE);

    @Test
    @DisplayName("갈 수 있는길이 여러개이면 가장 짧고 싼것을 선택한다")
    void 갈_수_있는길이_여러개이면_가장_짧고_싼것을_선택한다() {
        // given
        EfficientLines lines = new EfficientLines(this.lines);
        StationPair stationPair = new StationPair(second, first);

        // when
        Line cheapAndShortest = lines.findCheapAndShortestBy(stationPair);

        // then
        assertThat(cheapAndShortest)
                .isEqualTo(SECOND_LINE);
    }


    @Test
    @DisplayName("가장 비싼 운임을 선택한다")
    void 가장_비싼_운임을_선택한다() {
        // given
        EfficientLines lines = new EfficientLines(this.lines);

        // when
        Money expensiveFare = lines.findExpensiveFare();

        // then
        assertThat(expensiveFare)
                .isEqualTo(new Money(7000));
    }
}