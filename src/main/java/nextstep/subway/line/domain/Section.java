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

    public Section(Line line, Station upStation, Station downStation, int distance) {
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
        return distance.value();
    }

    public boolean equalsUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean equalsDownStation(Station station) {
        return downStation.equals(station);
    }

    public void updateUpStation(Station station, int newDistance) {
        this.upStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public boolean equalsSection(Long departureId, Long destinationId) {
        return equalsUpAndDownStationId(departureId, destinationId)
                || equalsUpAndDownStationId(destinationId, departureId);
    }

    private boolean equalsUpAndDownStationId(Long upStationId, Long downStationId) {
        return upStation.equalsId(upStationId) && downStation.equalsId(downStationId);
    }


    public Section merge(Section downSection) {
        if (!line.equals(downSection.line)) {
            throw new BadRequestException("다른 노선의 구간은 합칠 수 없습니다.");
        }

        if (!this.downStation.equals(downSection.upStation)) {
            throw new BadRequestException("합칠 수 없는 구간입니다.");
        }

        Distance newDistance = this.distance.plus(downSection.getDistance());
        return new Section(line, this.upStation, downSection.downStation, newDistance.value());
    }
}
