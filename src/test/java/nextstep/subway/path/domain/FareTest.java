package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    void create_기본요금에서_추가요금을_더한다() {
        assertThat(Fare.of(700).getValue()).isEqualTo(1950);
    }
}