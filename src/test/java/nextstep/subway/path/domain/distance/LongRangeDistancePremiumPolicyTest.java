package nextstep.subway.path.domain.distance;

import nextstep.subway.wrapped.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}