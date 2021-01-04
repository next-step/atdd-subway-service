package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidExtraFeeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExtraFeeTest {
    @DisplayName("음수로 오브젝트를 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource("createFailTestResource")
    void createFailTest(BigDecimal invalidValue) {
        assertThatThrownBy(() -> ExtraFee.of(invalidValue)).isInstanceOf(InvalidExtraFeeException.class);
    }
    public static Stream<Arguments> createFailTestResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }

    @DisplayName("인자로 null이 전달되면 0으로 초기화 된다.")
    @Test
    void defaultValueTest() {
        assertThat(ExtraFee.of(null)).isEqualTo(ExtraFee.of(BigDecimal.ZERO));
    }
}