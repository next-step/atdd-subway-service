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

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public boolean upStationEquals(Station station) {
        return this.upStation.equals(station);
    }

    public boolean downStationEquals(Station station) {
        return this.downStation.equals(station);
    }

    public void updateSection(Section sectionToAdd) {
        if (upStationEquals(sectionToAdd.getUpStation())) {
            validateDistance(sectionToAdd);
            upStation = sectionToAdd.getDownStation();
            distance -= sectionToAdd.getDistance();
            return;
        }
        if (downStationEquals(sectionToAdd.getDownStation())) {
            validateDistance(sectionToAdd);
            downStation = sectionToAdd.getUpStation();
            distance -= sectionToAdd.getDistance();
        }
    }

    private void validateDistance(Section sectionToAdd) {
        if (this.distance <= sectionToAdd.getDistance()) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public void mergeWith(Section section) {
        downStation = section.getDownStation();
        distance += section.getDistance();

    }
}
