package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdditionalFareTest {

    @Test
    @DisplayName("초과요금 초기값을 0으로 셋팅한다.")
    void initAdditionalFare() {
        AdditionalFare 초과요금_초기값 = AdditionalFare.init();

        assertThat(초과요금_초기값.getFare())
                .isZero();
    }

    @Test
    @DisplayName("초과요금은 0보다 작은 값으로 생성할 수 없다.")
    void createAdditionalFareFailsWhenLessThanZero() {
        assertThatThrownBy(() -> AdditionalFare.from(-500))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가 요금은 0 보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("초과요금을 생성할 수 있다.")
    void createAdditionalFare() {
        int 요금 = 1000;
        AdditionalFare 초과요금 = AdditionalFare.from(요금);

        assertThat(초과요금.getFare())
                .isEqualTo(요금);
    }
}
