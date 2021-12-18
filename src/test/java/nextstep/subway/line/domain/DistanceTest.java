package nextstep.subway.line.domain;

import nextstep.subway.line.exception.OutOfDistanceRangeException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @DisplayName("거리는 0이상이어야 합니다.")
    @Test
    void create() {
        ThrowableAssert.ThrowingCallable throwingCallable = () -> new Distance(-1);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(OutOfDistanceRangeException.class);
    }

}
