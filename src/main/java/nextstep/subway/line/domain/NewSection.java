package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class NewSection {
    private Station upStation;
    private Station downStation;
    private int distance;

    public NewSection(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section toSection(Line line) {
        return new Section(line, upStation, downStation, distance);
    }
}
