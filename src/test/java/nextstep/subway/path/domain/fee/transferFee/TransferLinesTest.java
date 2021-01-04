package nextstep.subway.path.domain.fee.transferFee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TransferLinesTest {
    @DisplayName("환승한 노선이 없는 경우 환승 추가금을 구할 수 있다")
    @Test
    void calculateTransferFeeWhenNotTransferredTest() {
        TransferLines transferLines = new TransferLines(new ArrayList<>());

        BigDecimal transferFee = transferLines.calculateTransferFee();

        assertThat(transferFee).isEqualTo(BigDecimal.ZERO);
    }

    @DisplayName("환승한 노선이 있는 경우 가장 비싼 환승 추가금을 환승비로 계산한다")
    @ParameterizedTest
    @MethodSource("calculateTransferFeeTestResource")
    void calculateTransferFeeTest(TransferLines transferLines, BigDecimal expected) {
        BigDecimal transferFee = transferLines.calculateTransferFee();

        assertThat(transferFee).isEqualTo(expected);
    }
    public static Stream<Arguments> calculateTransferFeeTestResource() {
        return Stream.of(
                Arguments.of(
                        new TransferLines(Arrays.asList(
                                new LineWithExtraFee(1L, BigDecimal.ZERO),
                                new LineWithExtraFee(2L, BigDecimal.ONE),
                                new LineWithExtraFee(3L, BigDecimal.TEN)
                        )),
                        BigDecimal.TEN
                ),
                Arguments.of(
                        new TransferLines(Collections.singletonList(
                                new LineWithExtraFee(1L, BigDecimal.ZERO)
                        )),
                        BigDecimal.ZERO
                ),
                Arguments.of(
                        new TransferLines(Arrays.asList(
                                new LineWithExtraFee(1L, BigDecimal.ZERO),
                                new LineWithExtraFee(2L, BigDecimal.ZERO)
                        )),
                        BigDecimal.ZERO
                )
        );
    }
}