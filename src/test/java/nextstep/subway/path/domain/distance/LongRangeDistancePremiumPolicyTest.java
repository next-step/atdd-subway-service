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

class LongRangeDistancePremiumPolicyTest {
    @Test
    @DisplayName("거리에 따라 지원여부가 틀리다 50Km")
    void 거리에_따라_지원여부가_틀리다_50Km() {
        assertThat(new LongRangeDistancePremiumPolicy().isSupport(new Distance(60)))
                .isTrue();
        assertThat(new LongRangeDistancePremiumPolicy().isSupport(new Distance(50)))
                .isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {"51, 1250", "55, 1250", "58, 1350", "66, 1450", "74, 1550", "75, 1550", "83, 1650"}, delimiter = ',')
    void calcFare(int km, int fare) {
        DistancePremiumPolicy distancePremiumPolicy = new LongRangeDistancePremiumPolicy();

        assertThat(distancePremiumPolicy.calcFare(new Distance(km), new Money(1250)))
                .isEqualTo(new Money(fare));
    }
}