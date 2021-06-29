package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MidRangeDistancePremiumPolicyTest {
    @ParameterizedTest
    @CsvSource({"10, false", "20, true"})
    @DisplayName("거리에 따라 지원여부가 다르다")
    void 거리에_따라_지원여부가_다르다(int km, boolean isSupport) {
        // given
        DistancePremiumPolicy distancePremiumPolicy = new MidRangeDistancePremiumPolicy();
        Distance distance = new Distance(km);

        // when
        boolean support = distancePremiumPolicy.isSupport(distance);

        // then
        assertThat(support)
                .isEqualTo(isSupport);
    }

    @ParameterizedTest
    @CsvSource(value = {"11, 1250", "15, 1350", "21, 1450", "49, 1950", "50, 2050", "55, 2050"}, delimiter = ',')
    @DisplayName("운임을 계산한다")
    void 운임을_계산한다(int km, int except) {
        // given
        DistancePremiumPolicy distancePremiumPolicy = new MidRangeDistancePremiumPolicy();
        Distance distance = new Distance(km);
        Money defaultMoney = new Money(1250);

        // when
        Money result = distancePremiumPolicy.calcFare(distance, defaultMoney);

        // then
        assertThat(result)
                .isEqualTo(new Money(except));
    }
}