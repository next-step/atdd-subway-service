package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTypeTest {
    @DisplayName("총 이동거리에 따른 요금을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "15:1350", "50:2050", "51:2150"}, delimiter = ':')
    void calculate(int distance, int expected) {
        //given
        FareType fareType = FareType.findByDistance(distance);
        //when
        int actual = fareType.calculateByDistance(distance);
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
