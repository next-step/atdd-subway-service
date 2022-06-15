package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

public class PathTestUtils {
    public static void 경유거리_확인(Path path, int expectedDistance) {
        assertThat(path.getDistance()).isEqualTo(expectedDistance);
    }

    public static void 경유지_확인(Path path, List<Station> expectedStations) {
        List<Station> actualStations = path.getStations();
        assertThat(actualStations).isEqualTo(expectedStations);
        assertThat(actualStations).hasSize(3);
    }
}
