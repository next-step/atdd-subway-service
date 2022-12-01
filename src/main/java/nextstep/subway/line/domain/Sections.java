package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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
        while (hasPrev(nowStation)) {
            nowStation = prev(nowStation);
        }
        return nowStation;
    }

    private boolean hasPrev(Station station) {
        return findHasDownStation(station).isPresent();
    }

    private Station prev(Station station) {
        Section section = findHasDownStation(station).orElseThrow(NoSuchElementException::new);
        return section.getUpStation();
    }

    private Stations collectStations(Station nowStation) {
        Stations stations = Stations.empty();
        stations.add(nowStation);

        while (hasNext(nowStation)) {
            nowStation = next(nowStation);
            stations.add(nowStation);
        }
        return stations;
    }

    private boolean hasNext(Station station) {
        return findHasUpStation(station).isPresent();
    }

    private Station next(Station station) {
        Section section = findHasUpStation(station).orElseThrow(NoSuchElementException::new);
        return section.getDownStation();
    }

    public Optional<Section> findHasDownStation(Station station) {
        return this.sections.stream().filter(section -> section.hasDownStation(station)).findFirst();
    }

    public Optional<Section> findHasUpStation(Station station) {
        return this.sections.stream().filter(section -> section.hasUpStation(station)).findFirst();
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

    public void removeStation(Station station) {
        SectionsValidator.validateRemoveStation(this);

        Section upLineSection = findHasDownStation(station).orElse(null);
        Section downLineSection = findHasUpStation(station).orElse(null);

        remove(upLineSection);
        remove(downLineSection);

        addMergeSection(upLineSection, downLineSection);
    }

    private void remove(Section upLineStation) {
        if (Objects.nonNull(upLineStation)) {
            sections.remove(upLineStation);
        }
    }

    private void addMergeSection(Section upSection, Section downSection) {
        if (Objects.isNull(upSection)) {
            return;
        }
        if (Objects.isNull(downSection)) {
            return;
        }
        sections.add(upSection.merge(downSection));
    }

    public int size() {
        return sections.size();
    }

    public List<Section> getList() {
        return Collections.unmodifiableList(this.sections);
    }
}
