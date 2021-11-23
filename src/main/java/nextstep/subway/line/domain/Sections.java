package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

@Embeddable
public class Sections {
    private static final String NOT_EXIST_FIRST_SECTION = "첫 번째 구간이 존재하지 않습니다.";
    private static final int START_INDEX = 0;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections createEmpty() {
        return new Sections(new ArrayList<>());
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void removeStation(Station station) {
        Optional<Section> upLineSection = findByUpStation(station);
        Optional<Section> downLineSection = findByDownStation(station);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            addMiddleSection(upLineSection.get(), downLineSection.get());
        }

        upLineSection.ifPresent(it -> sections.remove(it));
        downLineSection.ifPresent(it -> sections.remove(it));
    }

    public int size() {
        return sections.size();
    }

    public boolean hasStation(Station station) {
        return findAllStations().contains(station);
    }

    public Optional<Section> findByUpStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameUpStation(station));
    }

    public Optional<Section> findByDownStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameDownStation(station));
    }

    public Section findFirstSection() {
        List<Station> downStations = findDownStations();
        return StreamUtils.filterAndFindFirst(sections, section -> !downStations.contains(section.getUpStation()))
                          .orElseThrow(() -> new IllegalStateException(NOT_EXIST_FIRST_SECTION));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getSortedStations();
    }

    private void addMiddleSection(Section upLineSection, Section downLineSection) {
        Station newUpStation = downLineSection.getUpStation();
        Station newDownStation = upLineSection.getDownStation();
        Distance newDistance = Distance.merge(upLineSection.getDistance(), downLineSection.getDistance());

        sections.add(Section.of(downLineSection.getLine(), newUpStation, newDownStation, newDistance));
    }

    private List<Station> findAllStations() {
        return StreamUtils.flatMapToList(sections, Section::getStations, Collection::stream);
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return StreamUtils.filterAndFindFirst(sections, section -> section.isSameUpStation(station));
    }

    private List<Station> findUpStations() {
        return StreamUtils.mapToList(sections, Section::getUpStation);
    }

    private List<Station> findDownStations() {
        return StreamUtils.mapToList(sections, Section::getDownStation);
    }

    private List<Station> getSortedStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(findFirstSection().getUpStation());

        for (int i = START_INDEX; i < sections.size(); i++) {
            Optional<Section> sectionByUpStation = findSectionByUpStation(stations.get(i));
            sectionByUpStation.map(Section::getDownStation)
                              .ifPresent(stations::add);
        }

        return stations;
    }
}
