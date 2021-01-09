package nextstep.subway.path.infra;

import nextstep.subway.common.Money;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.LineFee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultLineFeeTest {

    private LineFee lineFee;

    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineFee = new DefaultLineFee(lineRepository);
    }

    @DisplayName("지하철 요금 부과는 노선 중 가장 큰 금액 하나만 반영된다.")
    @Test
    void settle() {
        // given
        Long 일호선 = 1L;
        Long 이호선 = 2L;
        Long 삼호선 = 3L;
        when(lineRepository.findAllByIds(Arrays.asList(1L, 2L, 3L)))
                .thenReturn(Arrays.asList(
                        Line.builder().surcharge(100).build(),
                        Line.builder().surcharge(200).build(),
                        Line.builder().surcharge(300).build()
                ));

        // when
        Money settle = lineFee.settle(Arrays.asList(일호선, 이호선, 삼호선));

        // then
        assertThat(settle).isEqualTo(Money.valueOf(300));
    }

}
