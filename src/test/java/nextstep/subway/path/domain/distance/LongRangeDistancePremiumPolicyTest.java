package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class LongRangeDistancePremiumPolicyTest {
    @ParameterizedTest
    @CsvSource({"50, false", "60, true"})
    @DisplayName("거리에 따라 지원여부가 틀리다")
    void 거리에_따라_지원여부가_틀리다(int km, boolean isSupport) {
        // given
        DistancePremiumPolicy distancePremiumPolicy = new LongRangeDistancePremiumPolicy();
        Distance distance = new Distance(km);

        // when
        boolean support = distancePremiumPolicy.isSupport(distance);

        // then
        assertThat(support)
                .isEqualTo(isSupport);
    }

    @ParameterizedTest
    @CsvSource(value = {"51, 1250", "55, 1250", "58, 1350", "66, 1450", "74, 1550", "75, 1550", "83, 1650"}, delimiter = ',')
    @DisplayName("운임을 계산한다")
    void 운임을_계산한다(int km, int except) {
        // given
        DistancePremiumPolicy distancePremiumPolicy = new LongRangeDistancePremiumPolicy();
        Distance distance = new Distance(km);
        Money defaultMoney = new Money(1250);

        // when
        Money result = distancePremiumPolicy.calcFare(distance, defaultMoney);

        // then
        assertThat(result)
                .isEqualTo(new Money(except));
    }
}