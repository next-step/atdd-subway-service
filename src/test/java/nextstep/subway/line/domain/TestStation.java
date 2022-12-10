package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Objects;

public class TestStation extends Station {
    public TestStation(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestStation testStation = (TestStation) o;
        return Objects.equals(super.getName(), testStation.getName());
    }
}
