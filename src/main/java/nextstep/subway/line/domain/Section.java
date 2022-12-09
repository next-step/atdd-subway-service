package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section implements Comparable<Section> {
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

    protected Section() {}

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public void updateUpStation(Section section) {
        distance.checkValidationSize(section.distance.value());
        distance.minusChangeDistance(section.distance.value());
        this.upStation = section.downStation;
    }

    public void updateDownStation(Section section) {
        distance.checkValidationSize(section.distance.value());
        distance.minusChangeDistance(section.distance.value());
        this.downStation = section.upStation;
    }

    public boolean equalsUpStation(Section section) {
        return upStation.equals(section.upStation);
    }

    public boolean equalsDownStation(Section section) {
        return downStation == section.downStation;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.value();
    }

    public Line getLine() {
        return line;
    }

    @Override
    public int compareTo(Section o) {
        return this.downStation == o.upStation ? -1 : 1;
    }

}
