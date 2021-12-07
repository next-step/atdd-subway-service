package nextstep.subway.path.policy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultFarePolicyTest {
    @DisplayName("추가요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "58:2150", "66:2250", "67:2350"}, delimiter = ':')
    void testCalculateOverFare(int distance, int expectedFare) {
        // given
        Line lineNumberOne = new Line("1호선", "파랑", new Station("안양"), new Station("명학"), 5, 100);
        Line lineNumberTwo = new Line("2호선", "초록", new Station("신도림"), new Station("대림"), 5, 200);
        Set<Line> lines = new HashSet<>(Arrays.asList(lineNumberOne, lineNumberTwo));
        // when
        int fare = new DefaultFarePolicy().calculateOverFare(lines, distance);
        // then
        assertThat(fare).isEqualTo(expectedFare + 200);
    }

    @DisplayName("연령별 할인이 적용된 추가요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"11:12:850", "11:18:1150", "11:19:1350"}, delimiter = ':')
    void testCalculateOverFareDiscount(int distance, int age, int expectedFare) {
        // given
        Line lineNumberOne = new Line("1호선", "파랑");
        Line lineNumberTwo = new Line("2호선", "초록");
        Set<Line> lines = new HashSet<>(Arrays.asList(lineNumberOne, lineNumberTwo));
        // when
        int fare = new DefaultFarePolicy().calculateOverFare(lines, distance, age);
        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
