package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

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

    public Section(Line line, Section upSection, Section downSection) {
        this.line = line;
        this.upStation = upSection.upStation;
        this.downStation = downSection.downStation;
        this.distance = upSection.distance + downSection.distance;
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

    public void connectDownStationWith(Section section) {
        if (this.distance <= section.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = section.downStation;
        this.distance -= section.distance;
    }

    public void connectUpStationWith(Section section) {
        if (this.distance <= section.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = section.upStation;
        this.distance -= section.distance;
    }

    public boolean matchUpOrDownStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public boolean matchUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean matchUpStation(Section section) {
        return upStation.equals(section.upStation);
    }

    public boolean matchDownStation(Station station) {
        return downStation.equals(station);
    }

    public boolean matchDownStation(Section section) {
        return downStation.equals(section.downStation);
    }
}
