package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

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

    public Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void checkDistanceValidation(int oldDistance) {
        if (this.distance >= oldDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.distance = oldDistance - this.distance;
    }

    public boolean isSameUpStationOfSection(Section newSection) {
        if (newSection.isSameUpStation(this.upStation)) {
            newSection.checkDistanceValidation(this.distance);
            this.upStation = newSection.getDownStation();
        }
        return false;
    }

    public boolean isSameUpStation(Station oldUpStation) {
        return this.upStation.equals(oldUpStation);
    }

    public boolean isSameDownStationOfSection(Section newSection) {
        if (newSection.isSameDownStation(this.downStation)) {
            newSection.checkDistanceValidation(this.distance);
            this.downStation = newSection.getUpStation();
        }
        return false;
    }

    public boolean isSameDownStation(Station downStation) {
        return this.downStation.equals(downStation);
    }

    public void isSameSection(Section newSection) {
        if (newSection.isSameUpStation(this.upStation) && newSection.isSameDownStation(this.downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    public boolean isUpFinalSection(Section newSection) {
        return newSection.isSameDownStation(this.upStation);
    }

    public boolean isDownFinalSection(Section newSection) {
        return newSection.isSameUpStation(this.downStation);
    }

    public boolean getSectionSameDownStation(Long stationId) {
        return this.downStation.isThisStation(stationId);
    }

    public boolean getSectionSameUpStation(Long stationId) {
        return this.upStation.isThisStation(stationId);
    }

    public void connectNewSection(Section downSection) {
        this.downStation = downSection.getDownStation();
        this.distance += downSection.getDistance();
    }

    public boolean isExistUpSection(Station upStation) {
        return this.downStation.equals(upStation);
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

    public void setLine(Line line) {
        this.line = line;
    }
}
