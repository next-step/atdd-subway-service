package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.stream.Stream;

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
            throw new IllegalArgumentException("상행선과 하행선이 동일할 수 없습니다.");
        }
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

    public void changeLine(Line line) {
        this.line = line;
    }

    public boolean isSameUpStationBySection(Section section) {
        return upStation.isSameStation(section.getUpStation());
    }

    public boolean isSameDownStationBySection(Section section) {
        return downStation.isSameStation(section.getDownStation());
    }

    public void updateUpStation(Section newSection) {
        validCheckIsOverDistance(newSection.distance.value());
        this.distance = this.distance.subtract(newSection.distance);
        this.upStation = newSection.downStation;
    }

    public void updateDownStation(Section newSection) {
        validCheckIsOverDistance(newSection.distance.value());
        this.distance = this.distance.subtract(newSection.distance);
        this.downStation = newSection.upStation;
    }

    private void validCheckIsOverDistance(int checkTargetDistance) {
        if (distance.isOverDistance(checkTargetDistance)) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public Stream<Station> streamOfStation() {
        return Stream.of(upStation, downStation);
    }

    public void connectUpStationToDownStation(Section addSection) {
        distance.subtract(addSection.getDistance());
        this.upStation = addSection.downStation;
    }

    public void connectDownStationToUpStation(Section addSection) {
        distance.subtract(addSection.getDistance());
        this.downStation = addSection.upStation;
    }

    public void mergeDownSection(Section downSection) {
        this.downStation = downSection.downStation;
        this.distance.add(downSection.distance);
    }

    public boolean hasUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean hasDownStation(Station station) {
        return downStation.equals(station);
    }

}
