package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AgeChargeRuleTest {

    @DisplayName("어린이 요금 할인 성공")
    @Test
    void 어린이_요금_할인() {
        assertAll(() -> {
            assertThat(AgeChargeRule.calculateChargeByAge(1250, 6)).isEqualTo(450);
            assertThat(AgeChargeRule.calculateChargeByAge(1350, 7)).isEqualTo(500);
            assertThat(AgeChargeRule.calculateChargeByAge(1450, 8)).isEqualTo(550);
            assertThat(AgeChargeRule.calculateChargeByAge(1550, 9)).isEqualTo(600);
            assertThat(AgeChargeRule.calculateChargeByAge(1650, 10)).isEqualTo(650);
            assertThat(AgeChargeRule.calculateChargeByAge(1750, 11)).isEqualTo(700);
            assertThat(AgeChargeRule.calculateChargeByAge(1850, 12)).isEqualTo(750);
        });
    }

    @DisplayName("청소년 요금 할인 성공")
    @Test
    void 청소년_요금_할인() {
        assertAll(() -> {
            assertThat(AgeChargeRule.calculateChargeByAge(1250, 13)).isEqualTo(720);
            assertThat(AgeChargeRule.calculateChargeByAge(1350, 14)).isEqualTo(800);
            assertThat(AgeChargeRule.calculateChargeByAge(1450, 15)).isEqualTo(880);
            assertThat(AgeChargeRule.calculateChargeByAge(1550, 16)).isEqualTo(960);
            assertThat(AgeChargeRule.calculateChargeByAge(1650, 17)).isEqualTo(1040);
            assertThat(AgeChargeRule.calculateChargeByAge(1750, 18)).isEqualTo(1120);
        });
    }

    @DisplayName("어린이 혹은 청소년이 아니면 할인 없음")
    @Test
    void 어린이_청소년_아닐_경우() {
        assertAll(() -> {
            assertThat(AgeChargeRule.calculateChargeByAge(1450, 19)).isEqualTo(1450);
            assertThat(AgeChargeRule.calculateChargeByAge(1550, 20)).isEqualTo(1550);
            assertThat(AgeChargeRule.calculateChargeByAge(1650, 0)).isEqualTo(1650);
            assertThat(AgeChargeRule.calculateChargeByAge(1750, 1)).isEqualTo(1750);
        });
    }
}
