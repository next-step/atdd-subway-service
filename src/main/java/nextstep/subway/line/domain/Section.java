package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static nextstep.subway.line.application.exception.InvalidSectionException.SECTION_DUPLICATION;

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

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public boolean isConnectable(Section section) {
        validateDuplication(section);
        return isTerminusExtend(section) || isBetweenStations(section);
    }

    private void validateDuplication(Section section) {
        if (isDuplicate(section)) {
            throw SECTION_DUPLICATION;
        }
    }

    public Section merge(Section newSection) {
        if (isBetweenStations(newSection) && distance.divisible(newSection.getDistance())) {
            changeStationLink(newSection);
            distance = distance.minus(newSection.distance);
        }
        return newSection;
    }

    private void changeStationLink(Section newSection) {
        if (upStation.equals(newSection.upStation)) {
            upStation = newSection.downStation;
        }

        if (downStation.equals(newSection.downStation)) {
            downStation = newSection.upStation;
        }
    }

    private boolean isDuplicate(Section section) {
        return upStation.equals(section.upStation) && downStation.equals(section.downStation);
    }

    private boolean isTerminusExtend(Section section) {
        return upStation.equals(section.downStation) || downStation.equals(section.upStation);
    }

    private boolean isBetweenStations(Section section) {
        return upStation.equals(section.upStation) || downStation.equals(section.downStation);
    }

    public boolean equalUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean equalDownStation(Station station) {
        return downStation.equals(station);
    }

    public void mergeDistance(Distance deletedDistance) {
        distance = distance.plus(deletedDistance);
    }

    public void changeDownStationLink(Station downStation) {
        this.downStation = downStation;
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

    public int getWeight() {
        return distance.getDistance();
    }
}
