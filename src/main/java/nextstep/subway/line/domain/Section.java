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

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Distance distance) {
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

    public Distance getDistance() {
        return this.distance;
    }

    public void updateUpStation(Station station, Distance newDistance) {
        validateDistance(newDistance);
        this.upStation = station;
        this.distance.minusDistance(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        validateDistance(newDistance);
        this.downStation = station;
        this.distance.minusDistance(newDistance);
    }

    public Distance plusDistance(Section otherSection){
        this.distance.plusDistance(otherSection.getDistance());
        return this.distance;
    }

    private void validateDistance(Distance newDistance){
        if (this.distance.isGreaterThan(newDistance.getDistance())) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
