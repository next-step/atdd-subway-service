package nextstep.subway.line.dto;

import nextstep.subway.station.domain.Station;

public class SectionPath {
    private Station upStation;
    private Station downStation;
    private int distance;

    public SectionPath(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionPath of(Station upStation, Station downStation, int distance) {
        return new SectionPath(upStation, downStation, distance);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
