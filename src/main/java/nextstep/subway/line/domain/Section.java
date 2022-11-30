package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.stream.Stream;

import static nextstep.subway.exception.ErrorMessage.SAME_SUBWAY_SECTION_ERROR;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_section_line"))
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(name = "fk_section_up_station"))
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(name = "fk_section_down_station"))
    private Station downStation;

    @Embedded
    private Distance distance;

    protected Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Section(Station upStation, Station downStation, int distance) {
        validateSection(upStation, downStation);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    private static void validateSection(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(SAME_SUBWAY_SECTION_ERROR.getMessage());
        }
    }

    public void changeLine(Line line) {
        this.line = line;
    }

    public Stream<Station> streamOfStation() {
        return Stream.of(upStation, downStation);
    }

    public void connectUpStationToDownStation(Section addSection) {
        distance.minus(addSection.getDistance().value());
        this.upStation = addSection.downStation;
    }

    public void connectDownStationToUpStation(Section addSection) {
        distance.minus(addSection.getDistance().value());
        this.downStation = addSection.upStation;
    }

    public void disconnectDownSection(Section downSection) {
        this.downStation = downSection.downStation;
        this.distance.plus(downSection.distance.value());
    }

    public boolean hasUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean hasDownStation(Station station) {
        return downStation.equals(station);
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
}
