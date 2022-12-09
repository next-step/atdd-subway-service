package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixture.*;

public class LineFixture {

    public static final String LINE_A_NAME = "A";
    public static final String LINE_B_NAME = "B";
    public static final String LINE_A_COLOR = "red";
    public static final String LINE_B_COLOR = "blue";


    public static Line lineA() {
        return new Line(LINE_A_NAME, LINE_A_COLOR, stationA(), stationB(), 5, 0);
    }
    public static Line lineB() {
        return new Line(LINE_B_NAME, LINE_B_COLOR, stationA(), stationC(), 5, 0);
    }
}
