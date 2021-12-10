package nextstep.subway.path.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultAgeDiscountPolicyTest {
    @DisplayName("연령별 할인률을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"12:1350:850", "13:1350:1150", "18:1350:1150", "19:1350:1350"}, delimiter = ':')
    void testDiscount(int age, int fare, int expectedFare) {
        // when
        int result = new DefaultAgeDiscountPolicy(age).apply(fare);
        // then
        assertThat(result).isEqualTo(expectedFare);
    }
}
