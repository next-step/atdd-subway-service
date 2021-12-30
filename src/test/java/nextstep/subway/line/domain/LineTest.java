package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    void update_노선수정() {
        final Line actual = Line.of("2호선", "green", Fare.from(900));

        final Line expected = Line.of("분당선", "yellow", Fare.from(1000));

        actual.update(expected);

        assertThat(actual).isEqualTo(expected);
    }
}
