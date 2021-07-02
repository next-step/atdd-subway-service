package nextstep.subway.path.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareOfAgePolicyTest {

    @ParameterizedTest(name = "나이에 따른 할인율 적용 테스트, age={0}, totalFare={1}, expectedFare={2}")
    @MethodSource
    void discountTest(final int age, final int totalFare, final int expectedFare) {
        // when
        int actualFare = FareOfAgePolicy.discount(age, totalFare);

        // then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> discountTest() {
        return Stream.of(
            Arguments.of(30, 2250, 2250),
            Arguments.of(14, 2250, 1520),
            Arguments.of(7, 2250, 950)
                        );
    }

}
