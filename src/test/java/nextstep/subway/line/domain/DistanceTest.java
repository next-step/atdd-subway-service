package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceTest {

    @Test
    void 구간_길이는_0보다_커야한다() {
        Distance distance = new Distance();

        ThrowingCallable 구간_길이가_0일_경우 = () -> distance.validateLength(0);

        assertThatIllegalArgumentException().isThrownBy(구간_길이가_0일_경우);
    }

    @Test
    void 구간_길이는_역사이_길이보다_크면_안된다() {
        Distance distance = new Distance(10);

        ThrowingCallable 구간_길이가_역사이_길이보다_클_경우 = () -> distance.validateLength(11);

        assertThatIllegalArgumentException().isThrownBy(구간_길이가_역사이_길이보다_클_경우);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:1250", "14:1350", "15:1450", "50:2150", "57:2150", "58:2250"}, delimiter = ':')
    void 기본운임과_이용거리_초과_시_추가운임이_부과된다(int distanceParam, int expectedCost) {
        Distance distance = new Distance(distanceParam);

        int cost = distance.calculateCost();

        assertThat(cost).isEqualTo(expectedCost);
    }
}
