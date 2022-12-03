package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_section_to_line"))
    private Line line;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(name = "fk_section_to_upstation"), nullable = false)
    private Station upStation;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(name = "fk_section_to_downstation"), nullable = false)
    private Station downStation;
    @Embedded
    private Distance distance;

    public Section() {}

    private Section(Line line, Station upStation, Station downStation, int intDistance) {
        validateLine(line);
        validateStations(upStation, downStation);
        Distance distance = Distance.from(intDistance);
        validateSectionDistance(distance);

        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Line line, Station upStation, Station downStation, int intDistance) {
        return new Section(line, upStation, downStation, intDistance);
    }

    private void validateLine(Line line) {
        if(line == null) {
            throw new IllegalArgumentException(ErrorCode.노선_정보가_없음.getErrorMessage());
        }
    }

    private void validateStations(Station upStation, Station downStation) {
        validateUpStation(upStation);
        validateDownStation(downStation);
        if(upStation.equals(downStation)) {
            throw new IllegalArgumentException(ErrorCode.구간의_상행역과_하행역이_동일할_수_없음.getErrorMessage());
        }
    }

    private void validateSectionDistance(Distance distance) {
        if(distance.isZero()) {
            throw new IllegalArgumentException(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
        }
    }

    private void validateUpStation(Station upStation) {
        if(upStation == null) {
            throw new IllegalArgumentException(ErrorCode.상행종착역은_비어있을_수_없음.getErrorMessage());
        }
    }

    private void validateDownStation(Station downStation) {
        if(downStation == null) {
            throw new IllegalArgumentException(ErrorCode.하행종착역은_비어있을_수_없음.getErrorMessage());
        }
    }

    public boolean hasSameLine(Section section) {
        return this.line.equals(section.line);
    }

    public boolean isSameUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isSameDownStation(Station station) {
        return downStation.equals(station);
    }

    public void updateUpStation(Section section) {
        this.distance = this.distance.subtract(section.distance);
        validateSectionDistance(this.distance);
        this.upStation = section.downStation;
    }

    public void updateDownStation(Section section) {
        this.distance = this.distance.subtract(section.distance);
        validateSectionDistance(this.distance);
        this.downStation = section.upStation;
    }

    public Distance findAddDistance(Section section) {
        return this.distance.add(section.distance);
    }

    public List<Station> stations() {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);
        stations.add(downStation);
        return stations;
    }

    public boolean isSameSection(Section section) {
        return isSameUpStation(section.upStation) && isSameDownStation(section.downStation) && hasSameLine(section);
    }

    public boolean isContainStation(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public boolean isContainStationsInAnyOrder(Station upStation, Station downStation) {
        return isContainStation(upStation) && isContainStation(downStation);
    }

    public int distanceValue() {
        return distance.value();
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
