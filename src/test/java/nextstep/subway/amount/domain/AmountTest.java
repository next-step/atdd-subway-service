package nextstep.subway.amount.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AmountTest {

    @Test
    void 동등성() {
        assertAll(
            () -> assertThat(Amount.from(10L)).isEqualTo(Amount.from(10L)),
            () -> assertThat(Amount.from(10L)).isNotEqualTo(Amount.from(20L))
        );
    }
}
