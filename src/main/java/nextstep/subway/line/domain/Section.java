package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
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

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
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
        return distance.getDistance();
    }

    public void changeStationInfo(Section section) {
        if (upStation.equals(section.upStation)) {
            this.upStation = section.downStation;
            this.distance.subtractDistance(section.distance);
        }
        if (downStation.equals(section.downStation)) {
            this.downStation = section.upStation;
            this.distance.subtractDistance(section.distance);
        }
    }

    public boolean hasAllStations(Section section) {
        return hasUpStation(section) && hasDownStation(section);
    }

    public boolean hasAnyStations(Section section) {
        return hasUpStation(section) || hasDownStation(section);
    }

    private boolean hasUpStation(Section section) {
        return upStation.equals(section.upStation) || upStation.equals(section.downStation);
    }

    private boolean hasDownStation(Section section) {
        return downStation.equals(section.upStation) || downStation.equals(section.downStation);
    }
}
