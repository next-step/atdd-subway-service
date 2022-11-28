package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
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

    private Section(Builder builder) {
        this.line = builder.line;
        this.upStation = builder.upStation;
        this.downStation = builder.downStation;
        this.distance = Distance.from(builder.distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Builder().line(line)
            .upStation(upStation)
            .downStation(downStation)
            .distance(distance)
            .build();
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

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }

    public void updateStation(Section section) {
        if (isUpStation(section.upStation)) {
            updateUpStation(section);
        }
        if (isDownStation(section.downStation)) {
            updateDownStation(section);
        }
    }

    private void updateUpStation(Section section) {
        upStation = section.downStation;
        distance = distance.substract(section.distance);
    }

    private void updateDownStation(Section section) {
        downStation = section.upStation;
        distance = distance.substract(section.distance);
    }

    public boolean isUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return downStation.equals(station);
    }

    public Distance addDistance(Section section) {
        return this.distance.add(section.getDistance());
    }

    public void belong(Line line) {
        this.line = line;
    }

    public static class Builder {
        private Line line;
        private Station upStation;
        private Station downStation;
        private int distance;

        public Builder() {}

        public Builder line(Line line) {
            this.line = line;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Section build() {
            return new Section(this);
        }
    }
}
