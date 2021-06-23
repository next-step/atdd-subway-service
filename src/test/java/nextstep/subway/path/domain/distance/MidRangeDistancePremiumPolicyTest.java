package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MidRangeDistancePremiumPolicyTest {
    @Test
    @DisplayName("거리에 따라 지원여부가 틀리다 10Km")
    void 거리에_따라_지원여부가_틀리다_10Km() {
        assertThat(new MidRangeDistancePremiumPolicy().isSupport(new Distance(20)))
                .isTrue();
        assertThat(new MidRangeDistancePremiumPolicy().isSupport(new Distance(10)))
                .isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {"11, 1250", "15, 1350", "21, 1450", "49, 1950", "50, 2050", "55, 2050"}, delimiter = ',')
    void calcFare(int km, int fare) {
        DistancePremiumPolicy distancePremiumPolicy = new MidRangeDistancePremiumPolicy();

        assertThat(distancePremiumPolicy.calcFare(new Distance(km), new Money(1250)))
                .isEqualTo(new Money(fare));
    }
}