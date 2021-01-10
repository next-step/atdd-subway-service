package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DistanceTest {
    @DisplayName("거리가 0 이하인 경우")
    @Test
    void positiveDistance() {
        assertThatExceptionOfType(InvalidDistanceException.class)
                .isThrownBy(() -> new Distance(0));
    }
}
