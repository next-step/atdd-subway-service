package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Objects;

public class StationPair {
    private Station fistStation;
    private Station secondStation;

    public StationPair(Station fistStation, Station secondStation) {
        this.fistStation = fistStation;
        this.secondStation = secondStation;
    }

    public Station getFistStation() {
        return fistStation;
    }

    public Station getSecondStation() {
        return secondStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationPair that = (StationPair) o;
        return Objects.equals(fistStation, that.fistStation) && Objects.equals(secondStation, that.secondStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fistStation, secondStation);
    }
}
