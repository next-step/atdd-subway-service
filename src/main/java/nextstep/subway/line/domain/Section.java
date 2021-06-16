package nextstep.subway.line.domain;

import nextstep.subway.line.exception.IllegalSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private int distance;

    private static final String EXCEPTION_FOR_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.upStation;
        Station newDownStation = upLineStation.downStation;
        int newDistance = upLineStation.distance + downLineStation.distance;
        return new Section(upLineStation.line, newUpStation, newDownStation, newDistance);
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
        return distance;
    }

    public void updateUpStation(Section section) {
        if (this.distance <= section.distance) {
            throw new IllegalSectionException(EXCEPTION_FOR_DISTANCE);
        }
        this.upStation = section.downStation;
        this.distance -= section.distance;
    }

    public void updateDownStation(Section section) {
        if (this.distance <= section.distance) {
            throw new IllegalSectionException(EXCEPTION_FOR_DISTANCE);
        }
        this.downStation = section.upStation;
        this.distance -= section.distance;
    }

    public boolean hasSameDownStation(Section section) {
        return downStation.equals(section.downStation);
    }

    public boolean hasSameUpStation(Section section) {
        return upStation.equals(section.upStation);
    }

    public boolean isEqaulsUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isEqualsDownStation(Station station) {
        return downStation.equals(station);
    }

    public List<Station> findStations(Sections sections) {
        List<Station> stations = new ArrayList<>();
        stations.add(downStation);
        Optional<Section> nextSection = sections.findSectionInUpStation(this);
        while (nextSection.isPresent()) {
            stations.add(nextSection.get().downStation);
            nextSection = sections.findSectionInUpStation(nextSection.get());
        }
        return stations;
    }

    public List<Station> findAllStations(List<Station> otherStations) {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);
        stations.addAll(otherStations);
        return stations;
    }

    public boolean isFirstSection(List<Section> sections) {
        return sections.stream().noneMatch(section -> section.isBeforeSectionThan(this));
    }

    private boolean isBeforeSectionThan(Section section) {
        return downStation.equals(section.upStation);
    }

    public boolean isAfterSectionThan(Section newSection) {
        return upStation.equals(newSection.downStation);
    }
}
