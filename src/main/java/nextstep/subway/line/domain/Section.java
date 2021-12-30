package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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

    public Section(final Long id, final Line line, final Station upStation, final Station downStation, final Distance distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private Section(final Line line, final Station upStation, final Station downStation, final Distance distance) {
        this(null, line, upStation, downStation, distance);
    }

    private Section(final Station upStation, final Station downStation, final Distance distance) {
        this(null, null, upStation, downStation, distance);
    }

    public static Section of(final Line line, final Station upStation, final Station downStation, final Distance distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public static Section of(final Station upStation, final Station downStation, final Distance distance) {
        return new Section(upStation, downStation, distance);
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

    public void addLine(Line line) {
        this.line = line;
    }

    public boolean isUpStationAndTargetDownStationEquals(final Section targetSection) {
        return this.upStation.equals(targetSection.downStation);
    }

    public boolean isDownStationEquals(final Section targetSection) {
        return this.downStation.equals(targetSection.downStation);
    }

    public boolean isDownStationAndTargetUpStationEquals(final Section targetSection) {
        return this.downStation.equals(targetSection.upStation);
    }

    public boolean isUpStationEquals(final Section targetSection) {
        return this.upStation.equals(targetSection.upStation);
    }

    public void changeDownStation(final Section targetSection) {
        this.downStation = targetSection.downStation;
    }

    public void changeUpStation(final Section targetSection) {
        this.upStation = targetSection.upStation;
    }

    public boolean isDownStationEquals(final Station station) {
        return this.downStation.equals(station);
    }

    public boolean isUpStationEquals(final Station station) {
        return this.upStation.equals(station);
    }

    public void merge(final Section targetSection) {
        this.downStation = targetSection.downStation;
        this.distance = this.distance.plus(targetSection.distance);
    }

    public void minusDistance(final Section targetSection) {
        this.distance = this.distance.minus(targetSection.distance);
    }

    public boolean isSameSection(List<Station> stations) {
        if (stations.size() < 1) {
            return false;
        }
        return IntStream.rangeClosed(1, stations.size() - 1)
                .anyMatch(i -> isSameSection(stations, i));
    }

    private boolean isSameSection(List<Station> stations, int i) {
        Station findUpStation = stations.get(i - 1);
        Station findDownStation = stations.get(i);
        return this.isUpStationEquals(findUpStation) && this.isDownStationEquals(findDownStation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(line, section.line) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }
}
