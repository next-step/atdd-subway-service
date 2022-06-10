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

    public static Section emptyOf(Line line) {
        return new Section(line, null, null, 0);
    }

    public static Section mergeOf(Section downSection, Section upSection) {
        return new Section(downSection.getLine(), upSection.getUpStation(),
            downSection.getDownStation(),
            downSection.getDistance().plus(upSection.getDistance()));
    }

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void updateUpStation(Station station, Distance newDistance) {
        this.distance.minus(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.distance.minus(newDistance);
        this.downStation = station;
    }

    public void insert(Section appendSection) {
        if (appendSection.getUpStation().equals(this.upStation)) {
            updateUpStation(appendSection.getDownStation(), appendSection.getDistance());
            return;
        }
        if (appendSection.getDownStation().equals(this.downStation)) {
            updateDownStation(appendSection.getUpStation(), appendSection.getDistance());
            return;
        }
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

    public Distance getDistance() {
        return distance;
    }
}
