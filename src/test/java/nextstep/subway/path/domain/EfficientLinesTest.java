package nextstep.subway.path.domain;

import nextstep.subway.line.domain.StationPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.line.domain.LinesTest.*;
import static org.assertj.core.api.Assertions.assertThat;

class EfficientLinesTest {

    @Test
    @DisplayName("갈 수 있는길이 여러개이면 가장 짧고 싼것을 선택한다")
    void 갈_수_있는길이_여러개이면_가장_짧고_싼것을_선택한다() {
        EfficientLines lines = new EfficientLines(
                Arrays.asList(
                        FIRST_LINE,
                        SECOND_LINE,
                        THIRD_LINE,
                        FOURTH_LINE,
                        FIFTH_LINE
                )
        );

        assertThat(lines.findCheapAndShortestBy(new StationPair(second, first)))
                .isEqualTo(SECOND_LINE);
    }
}