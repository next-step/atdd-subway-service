package nextstep.subway.line.domain;

import java.util.Objects;
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

    private Section(Builder builder) {
        this.id = builder.id;
        this.line = builder.line;
        this.upStation = builder.upStation;
        this.downStation = builder.downStation;
        this.distance = builder.distance;
    }

    public static Section from(Section section) {
        if (Objects.isNull(section)) {
            return null;
        }
        return new Builder(section.upStation, section.downStation, section.distance)
                .line(section.line)
                .build();
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Builder(upStation, downStation, distance)
                .line(line)
                .build();
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

    public void update(Section section) {
        if (upStation == section.upStation) {
            updateUpStation(section.downStation, section.distance);
        }

        if (downStation == section.downStation) {
            updateDownStation(section.upStation, section.distance);
        }
    }

    private void updateUpStation(Station station, Distance newDistance) {
        validateDistance(newDistance);

        upStation = station;
        distance = distance.minus(newDistance);
    }

    private void validateDistance(Distance newDistance) {
        if (!distance.isGreaterThan(newDistance)) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    private void updateDownStation(Station station, Distance newDistance) {
        validateDistance(newDistance);

        downStation = station;
        distance = distance.minus(newDistance);
    }

    public boolean hasSameUpOrDownStation(Section section) {
        return hasUpStation(section.upStation) || hasDownStation(section.downStation);
    }

    public boolean hasUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean hasDownStation(Station station) {
        return downStation.equals(station);
    }

    public Section merge(Section section) {
        Distance newDistance = distance.add(section.distance);

        Station newUpStation = upStation;
        Station newDownStation = downStation;
        if (hasUpStation(section.downStation)) {
            newUpStation = section.upStation;
        }
        if (hasDownStation(section.upStation)) {
            newDownStation = section.downStation;
        }
        return new Builder(newUpStation, newDownStation, newDistance)
                .line(line)
                .build();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(line, section.line)
                && Objects.equals(upStation, section.upStation) && Objects.equals(downStation,
                section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }

    public static class Builder {
        private final Station upStation;
        private final Station downStation;
        private final Distance distance;

        private Long id;
        private Line line;

        public Builder(Station upStation, Station downStation, int distance) {
            this(upStation, downStation, Distance.from(distance));
        }

        public Builder(Station upStation, Station downStation, Distance distance) {
            this.upStation = upStation;
            this.downStation = downStation;
            this.distance = distance;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder line(Line line) {
            this.line = line;
            return this;
        }

        public Section build() {
            return new Section(this);
        }
    }
}
