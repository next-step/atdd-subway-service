package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Collection;
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

    protected Section() {
    }

    public Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void addLine(Line line) {
        this.line = line;
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

    public List<Station> stations() {
        return Arrays.asList(upStation, downStation);
    }

    public int getDistance() {
        return distance.value();
    }

    public void updateUpStation(Station station, Distance newDistance) {
        this.upStation = station;
        this.distance.validNewDistance(newDistance);
        this.distance.subtract(newDistance);
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.downStation = station;
        this.distance.validNewDistance(newDistance);
        this.distance.subtract(newDistance);
    }

    public boolean isNext(Section section) {
        return this.upStation.equals(section.downStation);
    }

    public void update(Section newSection) {
        distance.validNewDistance(newSection.distance);
        if (isEqualUpStation(newSection.upStation)) {
            updateUpStation(newSection);
        }
        if (isEqualDownStation(newSection.downStation)) {
            updateDownStation(newSection);
        }
    }

    public Section merge(Section nextSection) {
        Distance newDistance = distance.add(nextSection.distance);
        Section section = new Section(upStation, nextSection.getDownStation(), newDistance);
        section.addLine(line);
        return section;
    }

    public boolean isEqualUpStation(Station station) {
        return upStation.equals(station);
    }

    private void updateUpStation(Section newSection) {
        upStation = newSection.upStation;
        distance.subtract(newSection.distance);
    }

    public boolean isEqualDownStation(Station station) {
        return downStation.equals(station);
    }

    private void updateDownStation(Section newSection) {
        downStation = newSection.upStation;
        distance.subtract(newSection.distance);
    }

    public Collection<Station> findStations() {
        return Arrays.asList(upStation, downStation);
    }
}
