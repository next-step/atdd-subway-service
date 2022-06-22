package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class SurchargeTest {
    @DisplayName("거리별 추가요금")
    @ParameterizedTest
    @CsvSource(value = {"10,0", "11,100", "16,200", "46,800", "51,900", "58,900", "59,1000"})
    void distanceSurcharge(int distance, int surcharge) {
        Surcharge actual = DistanceSurcharge.from(distance);
        Surcharge expected = new Surcharge(surcharge);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("노선별 추가요금")
    @Test
    void lineSurcharge() {
        Line line1 = new Line("신분당선", "red", 1000);
        Line line2 = new Line("분당선", "yellow", 500);
        Surcharge actual = LineSurcharge.from(Arrays.asList(line1, line2));
        Surcharge expected = new Surcharge(1000);
        assertThat(actual).isEqualTo(expected);
    }
}
