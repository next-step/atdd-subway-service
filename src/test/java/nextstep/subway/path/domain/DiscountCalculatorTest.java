package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DiscountCalculatorTest {
    @Test
    @DisplayName("비회원 할인 요금을 계산한다")
    void 요금_할인_비회원() {
        assertThat(DiscountCalculator.getFare(null, 1350)).isEqualTo(1350);
    }

    @Test
    @DisplayName("성인 할인 요금을 계산한다")
    void 요금_할인_성인() {
        Member 성인 = new Member("email@email.com", "password", 20);
        assertThat(DiscountCalculator.getFare(성인, 1350)).isEqualTo(1350);
    }

    @Test
    @DisplayName("청소년 할인 요금을 계산한다")
    void 요금_할인_청소년() {
        Member 청소년 = new Member("email@email.com", "password", 16);
        assertThat(DiscountCalculator.getFare(청소년, 1350)).isEqualTo(800);
    }

    @Test
    @DisplayName("어린이 할인 요금을 계산한다")
    void 요금_할인_어린이() {
        Member 어린이 = new Member("email@email.com", "password", 9);
        assertThat(DiscountCalculator.getFare(어린이, 1350)).isEqualTo(500);
    }
}
