package nextstep.subway.path.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.fare.policy.BaseFarePolicy;
import nextstep.subway.line.domain.fare.policy.LineAdditionalFarePolicy;
import nextstep.subway.path.step.PathFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LineAdditionalFarePolicy 클래스")
public class LineAdditionalFarePolicyTest extends PathFixtures {

    private final BaseFarePolicy policy = new LineAdditionalFarePolicy();

    @Nested
    @DisplayName("getCalculateFare 메소드는")
    class Describe_getCalculateFare {

        @Nested
        @DisplayName("경로 노선에 추가요금이 없다면")
        class line_additional_fare_not_zero {

            @Test
            @DisplayName("`Money.ZERO` 반환됨")
            void not_additional_fare() {
                // when
                Fare fare = 경로조회_됨(강남, 양재, 20);
                Money money = policy.getCalculateFare(fare, Money.ZERO);

                // then
                assertThat(money).isEqualTo(Money.ZERO);
            }
        }

        @Nested
        @DisplayName("경로 노선에 추가요금이 있다면")
        class line_additional_fare {

            @Test
            @DisplayName("가장 높은 노선의 추가요금이 반환됨")
            void max_additional_fare() {
                // when
                Fare fare = 경로조회_됨(강남, 남부터미널, 20);
                Money money = policy.getCalculateFare(fare, Money.ZERO);

                // then
                assertThat(money).isEqualTo(Money.won(900));
            }
        }
    }
}
