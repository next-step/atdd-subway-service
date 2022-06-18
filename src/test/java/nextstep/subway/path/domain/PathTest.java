package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class PathTest {
    @Test
    void 경로_생성_거리_예외() {
        List<Station> stations = Arrays.asList(new Station("역1"), new Station("역2"));

        assertThatThrownBy(
                () -> Path.of(stations, Distance.from(0), Fare.from(1250))
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void 경로_생성_역_중복_예외() {
        Station station = new Station("역1");

        List<Station> stations = Arrays.asList(station, station);

        assertThatThrownBy(
                () -> Path.of(stations, Distance.from(5), Fare.from(1250))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 경로_생성_역_개수_예외() {
        Station station = new Station("역1");

        List<Station> stations = Arrays.asList(station);

        assertThatThrownBy(
                () -> Path.of(stations, Distance.from(5), Fare.from(1250))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}