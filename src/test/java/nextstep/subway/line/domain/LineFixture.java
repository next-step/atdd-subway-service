package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixture.*;

public class LineFixture {

    public static Line lineA() {
        return new Line("A", "red", stationA(), stationB(), 5);
    }

    public static Line lineB() {
        return new Line("A", "red", stationA(), stationC(), 5);
    }
}
