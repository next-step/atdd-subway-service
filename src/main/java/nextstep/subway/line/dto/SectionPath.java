package nextstep.subway.line.dto;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionPath {
    private Station upStation;
    private Station downStation;
    private double distance;

    private Money extraCharge;

    private SectionPath(Station upStation, Station downStation, double distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private SectionPath(Station upStation, Station downStation, double distance, Money extraCharge) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public static SectionPath from(Section section) {
        return new SectionPath(section.getUpStation(), section.getDownStation(), section.getDistanceDouble(),
                section.getExtraCharge());
    }

    public static SectionPath of(Station upStation, Station downStation, int distance, Money extraCharge) {
        return new SectionPath(upStation, downStation, distance, extraCharge);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public double getDistance() {
        return distance;
    }

    public Money getExtraCharge() {
        return extraCharge;
    }
}
