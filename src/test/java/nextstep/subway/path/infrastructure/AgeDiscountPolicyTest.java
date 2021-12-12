package nextstep.subway.path.infrastructure;

import static nextstep.subway.line.infrastructure.fare.policycondition.DefaultDistanceFareCondition.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.policy.BaseFarePolicy;
import nextstep.subway.line.infrastructure.fare.policy.AgeDiscountPolicy;
import nextstep.subway.path.step.PathFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AgeDiscountPolicyTest extends PathFixtures {

    private final BaseFarePolicy policy = new AgeDiscountPolicy();

    @Nested
    @DisplayName("getCalculateFare 메소드는")
    class Describe_getCalculateFare {

        @Nested
        @DisplayName("6살 이상 13살 미만 어린이일 경우")
        class Context_age_6_to_13 {

            @Test
            @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50% 할인")
            void it_child_50_percent_rate_discount() {
                // when
                Fare fare = 경로조회_됨(교대, 남부터미널, 10);
                Money money = policy.getCalculateFare(fare, DEFAULT_FARE);

                // then
                assertThat(money).isEqualTo(Money.won(450));
            }
        }

        @Nested
        @DisplayName("13살 이상 19살 미만 어린이일 경우")
        class Context_age_13_to_19 {

            @Test
            @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20% 할인")
            void it_child_50_percent_rate_discount() {
                // when
                Fare fare = 경로조회_됨(교대, 남부터미널, 15);
                Money money = policy.getCalculateFare(fare, DEFAULT_FARE);

                // then
                assertThat(money).isEqualTo(Money.won(720));
            }
        }

        @Nested
        @DisplayName("20살 이상 성인 일 경우")
        class Context_age_20_over {

            @Test
            @DisplayName("성인은 할인률이 없음")
            void it_child_50_percent_rate_discount() {
                // when
                Fare fare = 경로조회_됨(강남, 남부터미널, 20);
                Money money = policy.getCalculateFare(fare, DEFAULT_FARE);

                // then
                assertThat(money).isEqualTo(DEFAULT_FARE);
            }
        }
    }
}
