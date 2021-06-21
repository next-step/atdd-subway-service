package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionDistanceTest {
    private SectionDistance distance;

    @BeforeEach
    private void setUp() {
        distance = new SectionDistance(10);
    }

    @DisplayName("거리 변경")
    @Test
    public void 거리_변경_확인() throws Exception {
        //when
        distance.updateDistance(3);

        //then
        assertThat(distance.getDistance()).isEqualTo(7);
    }

    @DisplayName("거리 변경 예외")
    @ParameterizedTest
    @ValueSource(ints = {10, 11, 0, -1})
    public void 거리_변경_예외(int newDistance) throws Exception {
        //when
        //then
        assertThatThrownBy(() -> distance.updateDistance(newDistance)).isInstanceOf(IllegalArgumentException.class);
    }
}
