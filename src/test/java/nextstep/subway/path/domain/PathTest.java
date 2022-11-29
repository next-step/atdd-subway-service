package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    @Test
    @DisplayName("빈 지하철역 목록으로 경로를 생성할 수 없다.")
    void createPathEmptyStations() {
        // when & then
        assertThatThrownBy(() -> Path.of(Stations.from(Collections.emptyList()), Distance.from(10)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("노선에는 최소 2개의 지하철역이 포함되어야 합니다.");
    }

    @Test
    @DisplayName("경로는 거리가 0일 수 없다.")
    void createPathDistanceIsNotZero() {
        // given
        List<Station> stations = Arrays.asList(Station.from("강남역"), Station.from("신논현역"));

        // when & then
        assertThatThrownBy(() -> Path.of(Stations.from(stations), Distance.from(0)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("거리는 0보다 커야합니다.");
    }
}
