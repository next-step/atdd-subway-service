package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StationPositionTest {

    @DisplayName("구간에서 역의 위치가 반대인지 확인")
    @Test
    void 반대_확인() {
        StationPosition position = StationPosition.UP_STATION;

        assertAll(
                () -> assertThat(position.isOpposite(StationPosition.DOWN_STATION)).isTrue(),
                () -> assertThat(position.isOpposite(StationPosition.UP_STATION)).isFalse()
        );
    }
}
