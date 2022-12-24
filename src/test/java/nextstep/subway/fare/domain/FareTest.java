package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 요금 도메인 단위테스트")
class FareTest {
    private Fare 기본요금;

    @BeforeEach
    void setUp() {
        기본요금 = Fare.from(1250);
    }

    @Test
    @DisplayName("지하철 요금은에 추가요금을 합산한다")
    void sumFare() {
        Fare 추가요금 = Fare.from(200);
        Fare 지하철요금 = 기본요금.plus(추가요금);
        assertThat(지하철요금.value()).isEqualTo(1450);
    }

    @Test
    @DisplayName("지하철 요금은은 음수가 될 수 없다")
    void negativeFare() {
        assertThatIllegalArgumentException().isThrownBy(() -> Fare.from(-1300));
    }
}
