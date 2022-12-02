package nextstep.subway.station;

import nextstep.subway.station.domain.Station;

public class StationFixture {

    public static final String STATION_A = "A";
    public static final String STATION_B = "B";
    public static final String STATION_C = "C";
    public static final String STATION_D = "D";

    public static Station stationA() {
        return new Station(STATION_A);
    }

    public static Station stationB() {
        return new Station(STATION_B);
    }

    public static Station stationC() {
        return new Station(STATION_C);
    }

    public static Station stationD() {
        return new Station(STATION_D);
    }
}

