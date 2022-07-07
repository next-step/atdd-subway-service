package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("거리별 요금 정책")
class DistanceFarePolicyTest {

    @DisplayName("거리별 요금 정책에 따른 추가운임 계산")
    @Nested
    class 거리별_요금_정책을_바탕으로_추가운임_계산 {
        @DisplayName("10Km 이내는 추가요금이 없다.")
        @Test
        void 기본운임_계산() {
            assertThat(DistanceFarePolicy.calculateTotalExcessFare(Distance.from(9))).isEqualTo(0);
        }

        @DisplayName("10Km 초과 시 5Km마다 100원씩 추가운임 부과할 수 있다.")
        @Test
        void 이용거리_초과_10KM_50KM까지의_계산() {
            assertThat(DistanceFarePolicy.calculateTotalExcessFare(Distance.from(11))).isEqualTo(100);
        }

        // 800 + 200 = 1000
        @DisplayName("이용 거리 50Km 초과 시 8Km마다 100원씩 추가운임 부과할 수 있다.")
        @Test
        void 이용거리_50KM_초과_계산() {
            assertThat(DistanceFarePolicy.calculateTotalExcessFare(Distance.from(59))).isEqualTo(1000);
        }
    }
}
