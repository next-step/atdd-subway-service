package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class DomainFixtureFactory {
    public static Station createStation(String name) {
        return Station.builder(name)
                .build();
    }

    public static Station createStation(long id, String name) {
        return Station.builder(name)
                .id(id)
                .build();
    }

    public static Section createSection(long id, Line line, Station upStation, Station downStation, Distance distance) {
        return Section.builder(line, upStation, downStation, distance)
                .id(id)
                .build();
    }

    public static Section createSection(Line line, Station upStation, Station downStation, Distance distance) {
        return Section.builder(line, upStation, downStation, distance)
                .build();
    }
}
