package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AgeChargeRuleTest {

    @DisplayName("어린이,청소년을 제외한 나이에 맞게 규칙이 분배되는지 검증")
    @Test
    void ruleByAge() {
        AgeChargeRule base1 = AgeChargeRule.valueOf(0);
        AgeChargeRule base2 = AgeChargeRule.valueOf(19);

        assertAll(
                () -> assertThat(base1).isEqualTo(AgeChargeRule.DEFAULT),
                () -> assertThat(base2).isEqualTo(AgeChargeRule.DEFAULT)
        );
    }

    @DisplayName("청소년에 맞게 규칙이 분배되는지 검증")
    @Test
    void youthRule() {
        AgeChargeRule youth1 = AgeChargeRule.valueOf(13);
        AgeChargeRule youth2 = AgeChargeRule.valueOf(18);

        assertAll(
                () -> assertThat(youth1).isEqualTo(AgeChargeRule.YOUTH),
                () -> assertThat(youth2).isEqualTo(AgeChargeRule.YOUTH)
        );
    }

    @DisplayName("어린이에 맞게 규칙이 분배되는지 검증")
    @Test
    void childRule() {
        AgeChargeRule child1 = AgeChargeRule.valueOf(6);
        AgeChargeRule child2 = AgeChargeRule.valueOf(12);

        assertAll(
                () -> assertThat(child1).isEqualTo(AgeChargeRule.CHILD),
                () -> assertThat(child2).isEqualTo(AgeChargeRule.CHILD)
        );
    }

    @DisplayName("할인이 안되는 어른 금액이 잘 나오는지 검증")
    @Test
    void basicFare() {
        long adultFare = AgeChargeRule.calculateFare(1250L, 19);

        assertThat(adultFare).isEqualTo(1250L);
    }

    @DisplayName("어린이 할인이 적용된 계산된 금액이 잘 나오는지 검증")
    @Test
    void childFare() {
        long childFare = AgeChargeRule.calculateFare(1250L, 6);

        assertThat(childFare).isEqualTo(450L);
    }

    @DisplayName("청소년 할인이 적용된 계산된 금액이 잘 나오는지 검증")
    @Test
    void youthFare() {
        long youthFare = AgeChargeRule.calculateFare(1250L, 17);

        assertThat(youthFare).isEqualTo(720L);
    }
}
