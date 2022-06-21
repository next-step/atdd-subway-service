package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;
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

    public Section(Station upStation, Station downStation, Distance distance) {
        this(null, upStation, downStation, distance);
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

    public void updateUpStation(Section section) {
        if (this.distance.toInt() <= section.getDistance().toInt()) {
            throw new BadRequestException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = section.getUpStation();
        this.distance.minus(section.getDistance().toInt());
    }

    public void updateDownStation(Section section) {
        if (this.distance.toInt() <= section.getDistance().toInt()) {
            throw new BadRequestException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = section.getUpStation();
        this.distance.minus(section.getDistance().toInt());
    }

    public void toLine(Line line) {
        this.line = line;
    }
}
