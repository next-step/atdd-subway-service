package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

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

    public Section() {
    }

    private Section(Line line, Station upStation, Station downStation, int distance) {
        validateLine(line);
        validateStations(upStation, downStation);

        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = Distance.from(distance);
    }

    public static Section of(Line line, Station upStation, Station downStation, int distance) {
        return new Section(line, upStation, downStation, distance);
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

    private void validateUpStation(Station upstation) {
        if(upstation == null) {
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
        return upStation.isSameStation(station);
    }

    public boolean isSameDownStation(Station station) {
        return downStation.isSameStation(station);
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

    public void updateUpStation(Station station, int newDistance) {
        this.upStation = station;
        this.distance = this.distance.subtract(Distance.from(newDistance));
    }

    public void updateDownStation(Station station, int newDistance) {
        this.downStation = station;
        this.distance = this.distance.subtract(Distance.from(newDistance));
    }

    public void updateUpStation(Section section) {
        this.distance = this.distance.subtract(section.distance);
        this.upStation = section.downStation;
    }

    public void updateDownStation(Section section) {
        this.distance = this.distance.subtract(section.distance);
        this.downStation = section.upStation;
    }

    public List<Station> stations() {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);
        stations.add(downStation);
        return stations;
    }
}
