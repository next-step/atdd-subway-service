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

    public boolean isSameUpStation(Station station) {
        return station == upStation;
    }

    public boolean isSameDownStation(Station station) {
        return station == downStation;
    }

    public boolean hasSameStation(Station station, boolean hasUpStation) {
        if (hasUpStation) {
            return station == upStation;
        }
        return station == downStation;
    }

    public boolean isExistStation(boolean hasUpStation) {
        if (hasUpStation) {
            return upStation != null;
        }
        return downStation != null;
    }

    public void updateStationBySection(Section section, boolean hasUpStation) {
        validateDistance(section.getDistance());
        if (hasUpStation) {
            this.upStation = section.getDownStation();
            this.distance -= section.getDistance();
            return;
        }

        this.downStation = section.getUpStation();
        this.distance -= section.getDistance();
    }

    private void validateDistance(int distance) {
        if (this.distance <= distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
