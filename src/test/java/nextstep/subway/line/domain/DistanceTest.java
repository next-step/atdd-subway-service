package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class DistanceTest {
    @DisplayName("1보다 작은 값을 입력한 경우 테스트")
    @Test
    void validate() {
        assertAll(
                () -> assertThatThrownBy(() -> Distance.of(0))
                        .isInstanceOf(DistanceException.class),
                () -> assertThatThrownBy(() -> Distance.of(1).minus(Distance.of(2)))
                        .isInstanceOf(DistanceException.class)
        );
    }
}
