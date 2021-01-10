package nextstep.subway.path.infra;

import nextstep.subway.common.Money;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.DefaultLineFee;
import nextstep.subway.path.domain.LineFee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultLineFeeTest {

    private LineFee lineFee;

    @BeforeEach
    void setUp() {
        lineFee = new DefaultLineFee();
    }

    @DisplayName("지하철 요금 부과는 노선 중 가장 큰 금액 하나만 반영된다.")
    @Test
    void settle() {
        // given
        List<Line> lines = Arrays.asList(
                Line.builder().surcharge(100).build(),
                Line.builder().surcharge(200).build(),
                Line.builder().surcharge(300).build()
        );

        // when
        Money settle = lineFee.settle(lines);

        // then
        assertThat(settle).isEqualTo(Money.valueOf(300));
    }

}
