package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixture.stationA;
import static nextstep.subway.station.StationFixture.stationB;

public class LineFixture {

    public static Line lineA() {
        return new Line("A", "red", stationA(), stationB(), 5);
    }
}
