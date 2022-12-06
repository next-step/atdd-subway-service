package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionPath {
    private Station upStation;
    private Station downStation;
    private int distance;

    private SectionPath(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionPath from(Section section) {
        return new SectionPath(section.getUpStation(), section.getDownStation(), section.getDistanceInt());
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
