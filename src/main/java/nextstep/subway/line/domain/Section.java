package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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

    public void addLine(Line line) {
        this.line = line;
    }

    public List<Station> stations() {
        return Arrays.asList(upStation, downStation);
    }

    public boolean isNext(Section section) {
        return this.upStation.equals(section.downStation);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
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

    public boolean isEqualUpStation(Section newSection) {
        return this.upStation.equals(newSection.upStation);
    }

    public boolean isEqualDownStation(Section newSection) {
        return this.downStation.equals(newSection.downStation);
    }

    private void updateUpStation(Section newSection) {
        this.upStation = newSection.downStation;
        this.distance = this.distance.subtract(newSection.distance);
    }

    private void updateDownStation(Section newSection) {
        this.downStation = newSection.upStation;
        this.distance = this.distance.subtract(newSection.distance);
    }

    public boolean isUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

}
