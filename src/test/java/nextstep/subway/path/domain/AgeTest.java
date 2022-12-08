package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTest {

    @Test
    void isChild() {
        assertThat(new Age(6).isChild()).isTrue();
    }

    @Test
    void isTeen() {
        assertThat(new Age(16).isTeen()).isTrue();
    }
}
