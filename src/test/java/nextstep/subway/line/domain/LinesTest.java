package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import nextstep.subway.amount.domain.Amount;

class LinesTest {

    @Test
    void maxAmount() {
        Lines lines = Lines.from(Arrays.asList(givenLine(100L), givenLine(200L), givenLine(300L)));
        assertThat(lines.maxAmount()).isEqualTo(Amount.from(300L));
    }

    private Line givenLine(long amount) {
        Line line = Mockito.mock(Line.class);
        given(line.getAmount()).willReturn(Amount.from(amount));
        return line;
    }
}
