package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void 구간_길이는_0보다_커야한다() {
        Distance distance = new Distance();

        ThrowingCallable validateLength = () -> distance.validateLength(0);

        assertThatIllegalArgumentException().isThrownBy(validateLength);
    }

    @Test
    void 구간_길이는_역사이_길이보다_크면_안된다() {
        Distance distance = new Distance(10);

        ThrowingCallable validateLength = () -> distance.validateLength(11);

        assertThatIllegalArgumentException().isThrownBy(validateLength);
    }

}
