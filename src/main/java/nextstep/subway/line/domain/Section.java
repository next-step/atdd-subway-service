package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.station.domain.Station;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section createMergeSection(Line line, Section upLineSection, Section downLineSection) {
        return new Section(line, upLineSection.upStation, downLineSection.downStation,
                upLineSection.distance + downLineSection.distance);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
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

    public void updateUpStationAndDistanceFromDownStation(Section section) {
        validateUpdateDistance(section);
        this.upStation = section.downStation;
        this.distance -= section.distance;
    }

    public void updateDownStationAndDistanceFromUpStation(Section section) {
        validateUpdateDistance(section);
        this.downStation = section.upStation;
        this.distance -= section.distance;
    }

    public boolean isMatchDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isMatchUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isMatchUpStationToUpStationBy(Section section) {
        return this.upStation.equals(section.upStation);
    }

    public boolean isMatchDownStationToDownStationBy(Section section) {
        return this.downStation.equals(section.downStation);
    }

    public boolean hasContainBy(Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    private void validateUpdateDistance(Section section) {
        if (this.distance <= section.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
