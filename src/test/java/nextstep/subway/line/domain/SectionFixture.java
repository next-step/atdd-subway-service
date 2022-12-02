package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixture.lineA;
import static nextstep.subway.station.StationFixture.*;

public class SectionFixture {

    public static final int DISTANCE_A_B = 3;
    public static final int DISTANCE_B_C = 2;
    public static final int DISTANCE_A_C = 5;
    public static final int DISTANCE_C_D = 2;

    public static Section sectionAB(Line line) {
        return new Section(line, stationA(), stationB(), new Distance(DISTANCE_A_B));
    }

    public static Section sectionBC(Line line) {
        return new Section(line, stationB(), stationC(), new Distance(DISTANCE_B_C));
    }

    public static Section sectionAC(Line line) {
        return new Section(line, stationA(), stationC(), new Distance(DISTANCE_A_C));
    }

    public static Section sectionCD(Line line) {
        return new Section(line, stationC(), stationD(), new Distance(DISTANCE_C_D));
    }
}
