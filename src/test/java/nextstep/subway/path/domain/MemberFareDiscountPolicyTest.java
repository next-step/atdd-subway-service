package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MemberFareDiscountPolicyTest {

    @ParameterizedTest
    @MethodSource("연령별_할인_금액")
    void 연령별_할인을_적용한다(int age, int expected) {
        // given
        FarePolicy memberFareDiscountPolicy = new MemberFareDiscountPolicy(() -> 1050, age);

        // when
        int result = memberFareDiscountPolicy.fare();

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> 연령별_할인_금액() {
        return Stream.of(
                Arguments.of(
                        3, 1050
                ),
                Arguments.of(
                        6, 350
                ),
                Arguments.of(
                        13, 560
                ),
                Arguments.of(
                        19, 1050
                )
        );
    }
}
