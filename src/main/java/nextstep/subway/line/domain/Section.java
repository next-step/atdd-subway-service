package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.*;

@Entity
public class Section extends DefaultWeightedEdge {
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

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.from(distance);
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

    public int plus(Distance distance) {
        return this.distance.add(distance).value();
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance.value() <= newDistance) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_DISTANCE.getMessage());
        }
        this.upStation = station;
        this.distance = this.distance.subtract(Distance.from(newDistance));
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance.value() <= newDistance) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_DISTANCE.getMessage());
        }
        this.downStation = station;
        this.distance = this.distance.subtract(Distance.from(newDistance));
    }
}
