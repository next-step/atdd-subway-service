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

    public void updateUpStation(Station station, int newDistance) {
        this.upStation = station;
        this.distance.subtract(new Distance(newDistance));
    }

    public void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        this.distance.subtract(new Distance(newDistance));
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


}
