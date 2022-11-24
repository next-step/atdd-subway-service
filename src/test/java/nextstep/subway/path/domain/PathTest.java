package nextstep.subway.path.domain;

import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PathTest {

    @DisplayName("경로 생성 시, 지하철역들이 비어있으면 생성되지 않는다.")
    @Test
    void createPathThrowErrorWhenStationsIsNull() {
        // when & then
        assertThatThrownBy(() -> Path.of(Collections.emptyList(), 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.경로는_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("경로 생성 시, 거리가 음수이면 생성되지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,0})
    void createPathThrowErrorWhenDistanceSmallerThanOne(int distance) {
        // when & then
        assertThatThrownBy(() -> Path.of(Arrays.asList(createStation("강남역"), createStation("역삼역")), distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());

    }
}
