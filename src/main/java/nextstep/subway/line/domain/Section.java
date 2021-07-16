package nextstep.subway.line.domain;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.exception.UnmergeableSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static java.lang.String.format;

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

    public Section(Long id, Line line, Station upStation, Station downStation, Distance distance) {
        this.id = id;
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
        return distance;
    }

    public void mergeSection(Section downSection) {
        validateMergingSection(downSection);

        this.downStation = downSection.getDownStation();
        this.distance = this.distance.add(downSection.distance);
    }

    public Fare getLineFare() {
        return this.line.getFare();
    }

    public void updateUpStation(Station station, Distance newDistance) {
        validateLessDistance(newDistance);
        this.upStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        validateLessDistance(newDistance);
        this.downStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public boolean isIncludeStation(Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public void connectSectionBetween(Section section) {
        replaceUpStationIfSameUpStation(section);
        replaceDownStationIfSameDownStation(section);
        this.distance = this.distance.minus(section.distance);
    }

    public boolean isSameStationWithUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isSameStationWithDownStation(Station station) {
        return this.downStation.equals(station);
    }

    private void replaceUpStationIfSameUpStation(Section section) {
        if (this.upStation.equals(section.getUpStation())) {
            this.upStation = section.getDownStation();
        }
    }

    private void replaceDownStationIfSameDownStation(Section section) {
        if (this.downStation.equals(section.getDownStation())) {
            this.downStation = section.getUpStation();
        }
    }

    private void validateLessDistance(Distance newDistance) {
        if (this.distance.isLessThan(newDistance)) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    private void validateMergingSection(Section downSection) {
        if (!this.downStation.equals(downSection.getUpStation())
                || this.upStation.equals(downSection.getDownStation())) {
            throw new UnmergeableSectionException(format("%s-%s 구간과 %s-%s구간은 합칠 수 없습니다.",
                    this.upStation, this.downStation,
                    downSection.upStation, downSection.downStation));
        }
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", line=" + line +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
