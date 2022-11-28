package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public Stations toStations() {
        if (this.sections.isEmpty()) {
            return Stations.empty();
        }
        Station nowStation = findTopStation();
        return collectStations(nowStation);
    }

    private Station findTopStation() {
        Station nowStation = sections.get(0).getUpStation();
        while (nowStation.hasPrev(this)) {
            nowStation = nowStation.prev(this);
        }
        return nowStation;
    }

    private Stations collectStations(Station begin) {
        Stations stations = Stations.empty();
        stations.add(begin);

        while (begin.hasNext(this)) {
            begin = begin.next(this);
            stations.add(begin);
        }
        return stations;
    }

    public Optional<Section> findHasDownStation(Station station) {
        return this.sections.stream().filter(section -> section.hasDownStation(station)).findFirst();
    }

    public Optional<Section> findHasUpStation(Station station) {
        return this.sections.stream().filter(section -> section.hasUpStation(station)).findFirst();
    }

    public List<Section> getList() {
        return this.sections;
    }

    public void addNewSection(Section newSection) {
        Stations stations = toStations();
        SectionsValidator.validateAddNew(newSection, stations);

        findUpStationContained(newSection).ifPresent(section -> section.modifyUpStation(newSection));
        findDownStationContained(newSection).ifPresent(section -> section.modifyDownStation(newSection));
        this.sections.add(newSection);
    }

    private Optional<Section> findUpStationContained(Section newSection) {
        return this.sections.stream()
            .filter(section -> section.hasSameUpStation(newSection))
            .findFirst();
    }

    private Optional<Section> findDownStationContained(Section newSection) {
        return this.sections.stream()
            .filter(section -> section.hasSameDownStation(newSection))
            .findFirst();
    }
}
