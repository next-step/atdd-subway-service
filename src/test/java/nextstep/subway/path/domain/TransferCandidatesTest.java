package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TransferCandidatesTest {
    @DisplayName("주어진 후보들 중 가장 요금이 적은 노선으로 환승 노선 확정지을 수 있다.")
    @ParameterizedTest
    @MethodSource("confirmTransferLineTestResource")
    void confirmTransferLineTest(TransferCandidates transferCandidates, LineWithExtraFee expected) {
        LineWithExtraFee transferLine = transferCandidates.confirmTransferLine();

        assertThat(transferLine).isEqualTo(expected);
    }
    public static Stream<Arguments> confirmTransferLineTestResource() {
        return Stream.of(
                Arguments.of(
                        new TransferCandidates(Arrays.asList(
                                new LineWithExtraFee(1L, BigDecimal.TEN),
                                new LineWithExtraFee(2L, BigDecimal.TEN),
                                new LineWithExtraFee(3L, BigDecimal.TEN)
                        )),
                        new LineWithExtraFee(1L, BigDecimal.TEN)
                ),
                Arguments.of(
                        new TransferCandidates(Arrays.asList(
                                new LineWithExtraFee(1L, BigDecimal.ZERO),
                                new LineWithExtraFee(2L, BigDecimal.TEN),
                                new LineWithExtraFee(3L, BigDecimal.TEN)
                        )),
                        new LineWithExtraFee(1L, BigDecimal.ZERO)
                ),
                Arguments.of(
                        new TransferCandidates(Arrays.asList(
                                new LineWithExtraFee(3L, BigDecimal.TEN)
                        )),
                        new LineWithExtraFee(3L, BigDecimal.TEN)
                )
        );
    }
}