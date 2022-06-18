package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {
    private static final String LESS_THEN_ALREADY_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

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

    private Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, Distance.from(distance));
    }

    public void assignLine(Line line) {
        this.line = line;
    }

    public boolean isEqualsUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isEqualsDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public Section mergeSection(Section section) {
        Distance newDistance = Distance.from(this.distance);
        newDistance.increase(section.distance);

        Section mergeSection = new Section(this.upStation, section.downStation, newDistance);
        mergeSection.assignLine(this.line);
        return mergeSection;
    }

    public void updateSection(Section addedSection) {
        if (this.upStation.equals(addedSection.upStation)) {
            updateUpStation(addedSection.downStation, addedSection.distance);
        }
        if (this.downStation.equals(addedSection.downStation)) {
            updateDownStation(addedSection.upStation, addedSection.distance);
        }
    }

    private void updateUpStation(Station station, Distance newDistance) {
        this.distance.decrease(newDistance);
        this.upStation = station;
    }

    private void updateDownStation(Station station, Distance newDistance) {
        this.distance.decrease(newDistance);
        this.downStation = station;
    }

    public Long getId() {
        return this.id;
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
