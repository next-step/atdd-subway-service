package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {

    public static final DummySection DUMMY_SECTION = new DummySection();

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

    private Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.of(distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
    }

    public Section newOfMerge(Section section) {
        distance.plus(section.distance);
        return new Section(line, upStation, section.downStation, distance.getDistance());
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance.getDistance();
    }

    public void relocationUpStation(Section newSection) {
        this.distance.minus(newSection.getDistance());
        this.upStation = newSection.downStation;
    }

    public boolean isSameUpStationAndDownStation(Section section) {
        return isSameUpStation(section.upStation) && isSameDownStation(section.downStation);
    }

    public boolean isSameUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return downStation.equals(station);
    }

    public boolean isDummy() {
        return false;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    private static class DummySection extends Section {

        @Override
        public boolean isDummy() {
            return true;
        }
    }
}
