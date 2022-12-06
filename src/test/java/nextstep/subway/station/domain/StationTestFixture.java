package nextstep.subway.station.domain;

public class StationTestFixture {
    public static Station station(String name) {
        return Station.from(name);
    }
}
