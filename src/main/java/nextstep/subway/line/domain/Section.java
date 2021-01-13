package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;

@Entity
public class Section extends BaseEntity {
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

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.of(distance);
    }

    public void updateUpStationByNewSection(final Section newSection) {
        this.upStation = newSection.getDownStation();
        distance = distance.minus(newSection.getDistance());
    }

    public void updateDownStationByNewSection(final Section newSection) {
        this.downStation = newSection.getUpStation();
        distance = distance.minus(newSection.getDistance());
    }

    public boolean isMatchUpAndUpStation(final Section section) {
        return this.upStation == section.getUpStation();
    }

    public boolean isMatchDownAndDownStation(final Section section) {
        return this.downStation == section.getDownStation();
    }

    public boolean isMatchUpStation(final Station targetStation) {
        return this.upStation == targetStation;
    }

    public boolean isMatchDownStation(final Station targetStation) {
        return this.downStation == targetStation;
    }

    public void addToPath(final Consumer<Section> consumer) {
        consumer.accept(this);
    }

    public boolean newFindMatchingSection(final List<Station> vertexList) {
        for (int i = 1; i <= vertexList.size() - 1; i++) {
            final Station upStation = vertexList.get(i - 1);
            final Station downStation = vertexList.get(i);

            if (isMatchUpStation(upStation) && isMatchDownStation(downStation)) {
                return true;
            }
        }

        return false;
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

    public int getDistance() {
        return distance.getDistance();
    }

    public int getAdditionalFareLine() {
        return this.line.getAdditionalFare();
    }
}
