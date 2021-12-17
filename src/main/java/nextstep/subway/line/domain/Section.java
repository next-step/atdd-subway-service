package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;

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

    public Section(final Line line, final Station upStation, final Station downStation, final Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(final Line line, final Station upStation, final Station downStation, final Distance distance) {
        return new Section(line, upStation, downStation, distance);
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

    public boolean connectable(final Section section) {
        if (this.equals(section)) {
            return false;
        }

        long matchCount = Arrays.asList(upStation, downStation)
                .stream()
                .filter(station -> this.hasMatchedStation(station, section))
                .count();

        return matchCount == 1;
    }

    private boolean hasMatchedStation(final Station station, final Section section) {
        return !(station.equals(section.getUpStation()) && station.equals(section.getDownStation()))
                && (station.equals(section.getUpStation()) || station.equals(section.getDownStation()));
    }

    public void subtractDistance(final Section section) {
        if (this.upStation.equals(section.getUpStation())) {
            this.upStation = section.getDownStation();
            this.distance = this.distance.subtract(section.getDistance());
        }

        if (this.downStation.equals(section.getDownStation())) {
            this.downStation = section.getUpStation();
            this.distance = this.distance.subtract(section.getDistance());
        }
    }

    public boolean hasStation(final Station station) {
        return this.hasUpStation(station) || this.hasDownStation(station);
    }

    public boolean hasUpStation(final Station station) {
        return this.upStation.equals(station);
    }

    public boolean hasDownStation(final Station station) {
        return this.downStation.equals(station);
    }

    public void merge(final Section mergeSection) {
        this.downStation = mergeSection.getDownStation();
        this.distance = this.distance.add(mergeSection.getDistance());
    }

}
