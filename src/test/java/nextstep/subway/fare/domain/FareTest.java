package nextstep.subway.fare.domain;

import nextstep.subway.fare.application.InvalidFareException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @Test
    void 요금_생성() {
        Fare fare = new Fare(1_000);

        assertThat(fare).isNotNull();
    }

    @Test
    void 요금_생성_실패() {
        assertThatThrownBy(
                () -> new Fare(-100)
        ).isInstanceOf(InvalidFareException.class)
                .hasMessageContaining("요금은 음수일 수 없습니다.");
    }

}
