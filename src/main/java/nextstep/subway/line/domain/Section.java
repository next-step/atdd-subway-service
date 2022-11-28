package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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

    protected Section() {}

    public Section(Station upStation, Station downStation, Distance distance) {
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

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }

    public void belongs(Line line) {
        this.line = line;
    }

    public void update(Section newSection) {
        if (isEqualUpStation(newSection.upStation)) {
            updateUpStation(newSection);
        }

        if (isEqualDownStation(newSection.downStation)) {
            updateDownStation(newSection);
        }
    }

    public boolean isEqualUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isEqualDownStation(Station station) {
        return downStation.equals(station);
    }

    private void updateUpStation(Section newSection) {
        upStation = newSection.downStation;
        distance = distance.subtract(newSection.distance);
    }

    private void updateDownStation(Section newSection) {
        downStation = newSection.upStation;
        distance = distance.subtract(newSection.distance);
    }

    public Section merge(Section nextSection) {
        Distance newDistance = distance.add(nextSection.distance);
        Section section = new Section(upStation, nextSection.getDownStation(), newDistance);
        section.belongs(line);
        return section;
    }
}
