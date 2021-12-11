package nextstep.subway.line.domain;

import static nextstep.subway.common.Message.MESSAGE_SECTION_DISTANCE_NOT_LESS_THAN_ZERO;

import nextstep.subway.common.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DistanceTest {
    @Test
    void 거리는_0보다_작을_수_없다() {
        // then
        Assertions.assertThatThrownBy(() -> {
                Distance distance = new Distance(-1);
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith(
                Message.format(MESSAGE_SECTION_DISTANCE_NOT_LESS_THAN_ZERO, -1));
    }
}
