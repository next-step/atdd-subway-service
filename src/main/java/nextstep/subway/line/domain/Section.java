package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

    public boolean isNext(Section section) {
        return this.upStation.equals(section.downStation);
    }

    public void updateStation(Section newSection) {
        distance.validNewDistance(newSection.distance);
        if (isEqualUpStation(newSection)) {
            updateUpStation(newSection);
        }
        if (isEqualDownStation(newSection)) {
            updateDownStation(newSection);
        }
    }

    public void merge(Section nextSection) {
        this.downStation = nextSection.downStation;
        this.distance = this.distance.add(nextSection.distance);
    }

    public boolean isEqualUpStation(Section section) {
        return this.upStation.equals(section.upStation);
    }

    public boolean isEqualDownStation(Section section) {
        return this.downStation.equals(section.downStation);
    }

    private void updateUpStation(Section newSection) {
        this.upStation = newSection.downStation;
        this.distance = this.distance.subtract(newSection.distance);
    }

    private void updateDownStation(Section newSection) {
        this.downStation = newSection.upStation;
        this.distance = this.distance.subtract(newSection.distance);
    }

    public Collection<Station> findStations() {
        return Arrays.asList(upStation, downStation);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isUpStation(Station station) {
        return this.downStation.equals(station);
    }
}
