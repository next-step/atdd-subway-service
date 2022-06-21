package nextstep.subway.path.domain;

import nextstep.subway.path.dto.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YouthFareDiscounterTest {
    private FareDiscounter discounter;

    @BeforeEach
    void setUp() {
        this.discounter = new YouthFareDiscounter();

    }

    @Test
    @DisplayName("운임에서 350원을 공제한 금액의 20% 할인")
    void discount() {
        assertThat(discounter.discount(new Fare(1350))).isEqualTo(new Fare(800));
    }

}
