package nextstep.subway.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareAgeRuleTest {

    @DisplayName("나이에 따른 요금할인 : 13~19세미만(20%), 8~13미만(50%)")
    @Test
    void discountFareByAge() {
        assertAll(
                ()-> assertThat(FareAgeRule.discountFareByAge(13,1000)).isEqualTo(520),
                ()-> assertThat(FareAgeRule.discountFareByAge(8,1000)).isEqualTo(325)
        );
    }
}