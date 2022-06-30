package nextstep.subway.fare.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {
    private Fare fare;
    private Fare surcharge;

    @BeforeEach
    void setUp() {
        fare = new Fare(1250);
        surcharge = new Fare(100);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1})
    void 요금은_0원_이상이어야_한다(int fare) {
        // when & then
        assertThatThrownBy(() ->
                new Fare(fare)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요금은 0원 이상이어야 합니다.");
    }

    @Test
    void 요금을_더한다() {
        // when
        Fare result = fare.add(surcharge);

        // then
        assertThat(result.value()).isEqualTo(1350);
    }
}
