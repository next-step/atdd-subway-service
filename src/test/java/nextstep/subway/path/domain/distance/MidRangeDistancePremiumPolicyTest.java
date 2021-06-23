package nextstep.subway.path.domain.distance;

import nextstep.subway.wrapped.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}