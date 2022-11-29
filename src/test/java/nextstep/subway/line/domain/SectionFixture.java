package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixture.lineA;
import static nextstep.subway.station.StationFixture.*;

public class SectionFixture {

    public static final int DISTANCE_A_B = 3;
    public static final int DISTANCE_B_C = 2;
    public static final int DISTANCE_A_C = 5;
    public static final int DISTANCE_C_D = 2;

    public static Section sectionAB() {
        return new Section(lineA(), stationA(), stationB(), DISTANCE_A_B);
    }

    public static Section sectionBC() {
        return new Section(lineA(), stationB(), stationC(), DISTANCE_B_C);
    }

    public static Section sectionAC() {
        return new Section(lineA(), stationA(), stationC(), DISTANCE_A_C);
    }

    public static Section sectionCD() {
        return new Section(lineA(), stationC(), stationD(), DISTANCE_C_D);
    }
}
