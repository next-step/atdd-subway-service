package nextstep.subway.line.acceptance;

import nextstep.subway.exception.Message;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @DisplayName("거리 생성시 최소 길이를 넘지 못하면 예외발생")
    @Test
    void 거리_생성_예외상황() {
        //when+then
        assertThatThrownBy(() -> Distance.ofValue(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_DISTANCE_TOO_SHORT_TO_BE_CREATED.showText());
    }
}
