package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("할인기능 테스트")
public class DiscountTest {

    @DisplayName("나이에 따른 할인가격 계산")
    @ParameterizedTest
    @MethodSource
    void sale(int payment, int age, int expected) {
        Discount discount = Discount.select(age);
        assertThat(discount.accept(payment)).isEqualTo(expected);
    }

    private static Stream<Arguments> sale() {
        return Stream.of(
                Arguments.of(1000, 19, 1000),
                Arguments.of(1000, 13, 800),
                Arguments.of(1000, 6, 500),
                Arguments.of(1000, 5, 0)
        );
    }
}
