package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class SectionNew {
    private LineNew line;
    private Station upStation;
    private Station downStation;

    private Distance distance;

    protected SectionNew() {
    }

    public SectionNew(LineNew line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public LineNew getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.get();
    }

    public void updateUpStation(Station station, int newDistance) {
        this.distance.calculateDistanceForAdd(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, int newDistance) {
        this.distance.calculateDistanceForAdd(newDistance);
        this.downStation = station;
    }
}
