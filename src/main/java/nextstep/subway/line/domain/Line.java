package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.ExistedSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.list();
    }

    private Station findDownStation(Station downStation) {
        return this.sections.findDownStation(downStation);
    }

    private Station findUpStation() {
        return this.sections.findUpStation();
    }

    private boolean matchAnyStation(List<Station> stations, Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private boolean matchNoneStation(List<Station> stations, Station station) {
        return !matchAnyStation(stations, station);
    }

    public void addLineStation(Station upStation, Station downStation, Integer distance) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = matchAnyStation(stations, upStation);
        boolean isDownStationExisted = matchAnyStation(stations, downStation);


        if (!stations.isEmpty() && matchNoneStation(stations, upStation) &&
                matchNoneStation(stations, downStation)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new ExistedSectionException(upStation, downStation);
        }

        if(isUpStationExisted) {
            this.sections.findLineStation(it -> it.getUpStation() == upStation)
                    .ifPresent(it -> it.updateUpStation(downStation, distance));
            this.sections.add(new Section(this, upStation, downStation, distance));
            return;
        }

        this.sections.findLineStation(it -> it.getDownStation() == downStation)
            .ifPresent(it -> it.updateDownStation(upStation, distance));
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        if (this.getSections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = this.findUpStation();
        Station finalDownStation = null;

        while(downStation != finalDownStation) {
            stations.add(downStation);
            finalDownStation = downStation;
            downStation = findDownStation(finalDownStation);
        }

        return stations;
    }

    public void removeSection(Station station) {
        this.sections.remove(station);
    }
}
