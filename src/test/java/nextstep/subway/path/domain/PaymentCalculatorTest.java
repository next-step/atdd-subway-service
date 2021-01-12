package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 추가 기능 테스트")
public class PaymentCalculatorTest extends PathDomainBase {

    @DisplayName("요금 계산 기능")
    @ParameterizedTest
    @MethodSource
    void calculate(List<Line> lines, int distance, int expected) {
        int payment = PaymentCalculator.calculatePayment(lines, distance);
        assertThat(payment).isEqualTo(expected);
    }

    private static Stream<Arguments> calculate() {
        return Stream.of(
                Arguments.of(Arrays.asList(이호선), 10, 1_250),
                Arguments.of(Arrays.asList(이호선, 구호선), 10, 2_150),
                Arguments.of(Arrays.asList(이호선, 일호선), 10, 1_750),
                Arguments.of(Arrays.asList(이호선, 구호선, 일호선), 10, 2_150),
                Arguments.of(Arrays.asList(이호선), 50, 2_050),
                Arguments.of(Arrays.asList(이호선), 90, 2_250),
                Arguments.of(Arrays.asList(이호선, 구호선, 일호선), 50, 2_950),
                Arguments.of(Arrays.asList(이호선, 구호선, 일호선), 90, 3_150)
                );
    }
}
