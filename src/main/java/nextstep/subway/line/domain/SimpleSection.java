package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Objects;

public class SimpleSection {
    private Station upStation;
    private Station downStation;

    public SimpleSection(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSection that = (SimpleSection) o;
        return Objects.equals(upStation, that.upStation) && Objects.equals(downStation, that.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
