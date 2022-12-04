package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.exception.ErrorMessage.FARE_CANNOT_BE_ZERO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("요금 관련 테스트")
class FareTest {

    @DisplayName("요금 계산 중 0원 이하의 요금이 나올수는 없다.")
    @Test
    void no_zero_fare_calculate() {
        // given
        Fare fare = Fare.basicFare();
        // when && then
        assertThatThrownBy(() -> fare.plus(-1250))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FARE_CANNOT_BE_ZERO.getMessage());
    }

}
