package nextstep.subway.path.infrastructure;

import static nextstep.subway.line.infrastructure.fare.policycondition.DefaultDistanceFareCondition.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.policy.BaseFarePolicy;
import nextstep.subway.line.infrastructure.fare.policy.DistancePolicy;
import nextstep.subway.path.step.PathFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DistancePolicy 클래스")
public class DistancePolicyTest extends PathFixtures {

    private final BaseFarePolicy policy = new DistancePolicy();

    @Nested
    @DisplayName("getCalculateFare 메소드는")
    class Describe_getCalculateFare {

        @Nested
        @DisplayName("경로 전체거리가 10KM 이내라면")
        class distance_10km_under {

            @Test
            @DisplayName("지하철 최저 기본요금 `DEFAULT_FARE` 이 반환됨")
            void not_additional_fare() {
                // when
                Fare fare = 경로조회_됨(강남, 양재, 20);
                Money money = policy.getCalculateFare(fare, Money.ZERO);

                // then
                assertThat(money).isEqualTo(DEFAULT_FARE);
            }
        }

        @Nested
        @DisplayName("경로 전체거리가 10KM 초과 50km 이하라면")
        class distance_10km_to_50km {

            @Test
            @DisplayName("5km 마다 100원 요금이 부과됨")
            void it_5km_add_fare_100() {
                // when
                Fare fare = 경로조회_됨(강남, 남부터미널, 20);
                Money money = policy.getCalculateFare(fare, Money.ZERO);

                // then
                assertThat(money).isEqualTo(Money.won(1350));
            }
        }

        @Nested
        @DisplayName("경로 전체거리가 50km 초과라면")
        class distance_50km_over {

            @Test
            @DisplayName("8km 마다 100원 요금이 부과됨")
            void it_8km_add_fare_100() {
                // when
                Fare fare = 경로조회_됨(강동구청, 천호, 20);
                Money money = policy.getCalculateFare(fare, Money.ZERO);

                // then
                assertThat(money).isEqualTo(Money.won(2150));
            }
        }
    }
}
