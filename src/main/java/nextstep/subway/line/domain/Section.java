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

    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(line, upStation, downStation, new Distance(distance));
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

    protected Station getUpStation() {
        return upStation;
    }

    protected Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    protected void updateUpStation(Section section) {
        validateDistance(section);

        this.upStation = section.downStation;
        this.distance = this.distance.minus(section.distance);
    }
    protected void updateDownStation(Section section) {
        validateDistance(section);

        this.downStation = section.upStation;
        this.distance = this.distance.minus(section.distance);
    }

    public boolean isUpStationEquals(Station station) {
        return this.upStation == station;
    }

    public boolean isDownStationEquals(Station station) {
        return this.downStation == station;
    }

    public boolean isSameDownStation(Section section) {
        return this.downStation == section.downStation;
    }

    public boolean isSameUpStation(Section section) {
        return this.upStation == section.upStation;
    }

    public boolean isDownStationEqualsUpStationBy(Section section) {
        return this.downStation == section.upStation;
    }

    public boolean containsByUpStation(Section section) {
        return this.upStation == section.upStation || this.downStation == section.upStation;
    }

    public boolean containsByDownStation(Section section) {
        return this.upStation == section.downStation || this.downStation == section.downStation;
    }

    private void validateDistance(Section section) {
        if (distance.isLessThen(section.distance)) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
