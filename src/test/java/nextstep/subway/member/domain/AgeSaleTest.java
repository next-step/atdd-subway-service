package nextstep.subway.member.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AgeSaleTest {
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void 어린이_할인을_받을_수_있는_나이는_6세_이상_13세_미만이다(int age) {
        assertThat(AgeSale.of(age)).isEqualTo(Optional.of(AgeSale.CHILDREN));
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void 청소년_할인을_받을_수_있는_나이는_13세_이상_19세_미만이다(int age) {
        assertThat(AgeSale.of(age)).isEqualTo(Optional.of(AgeSale.YOUTH));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 19})
    void 할인을_받을_수_없는_나이는_6세_미만_19세_이상이다(int age) {
        assertThat(AgeSale.of(age)).isEmpty();
    }
}