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

    public Section() {
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

    public boolean isSameUpDownStation(Section newSection) {
        return isSameUpStation(newSection) && isSameDownStation(newSection);
    }

    public void reorganize(Section newSection) {
        if (isSameUpStation(newSection)) {
            this.upStation = newSection.downStation;
            this.distance = this.distance.subtract(newSection.distance);
        }
        if (isSameDownStation(newSection)) {
            this.downStation = newSection.upStation;
            this.distance = this.distance.subtract(newSection.distance);
        }
    }

    private boolean isSameUpStation(Section newSection) {
        return this.upStation.equals(newSection.upStation);
    }

    private boolean isSameDownStation(Section newSection) {
        return this.downStation.equals(newSection.downStation);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public boolean isUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public void merge(Section section) {
        this.downStation = section.downStation;
        this.distance = this.distance.add(section.distance);
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

    public Line getLine() {
        return line;
    }
}
