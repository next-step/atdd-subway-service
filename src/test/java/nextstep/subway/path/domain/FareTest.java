package nextstep.subway.path.domain;

import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {
    private static final int BASIC_PRICE = 1250;

    @DisplayName("추가 요금이 없을 때의 기본 지하철 요금은 1250원이다.")
    @Test
    void basicFare() {
        // given
        Line line = new Line("신분당선", "bg-red-600", new ExtraFare(0));
        Lines lines = new Lines(Arrays.asList(line));

        // when
        Fare result = new Fare(lines, 0, 20);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE);
    }

    @DisplayName("추가 요금이 있을경우, 지하철 요금은 기본요금과 추가요금의 합이다.")
    @Test
    void extraFare() {
        // given
        int extraFare = 1_000;
        Line line = new Line("신분당선", "bg-red-600", new ExtraFare(extraFare));
        Lines lines = new Lines(Arrays.asList(line));

        // when
        Fare result = new Fare(lines, 0, 20);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE + extraFare);
    }
}
