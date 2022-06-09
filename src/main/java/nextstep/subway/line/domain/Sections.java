package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.line.utils.SectionsComparator;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addSection(Section section) {
        if (this.isEmptyByStation()) {
            sections.add(section);
            return;
        }

        validateSections(section);

        boolean isUpStationExisted = matchStation(section.getUpStation());
        boolean isDownStationExisted = matchStation(section.getDownStation());

        validateStations(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted && isDownStationExisted) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            updateUpStationOfSection(section);
            sections.add(section);
        }

        if (isDownStationExisted) {
            updateDownStationOfSection(section);
            sections.add(section);
        }
    }

    private void validateSections(Section section) {
        this.sections.forEach(it -> it.validate(section));
    }

    private void validateStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public void updateUpStationOfSection(Section section) {
        this.sections.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    public void updateDownStationOfSection(Section section) {
        this.sections.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    public int sectionsSize() {
        return sections.size();
    }

    public Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    public List<Station> findStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        sections.sort(new SectionsComparator());

        List<Station> stations = new ArrayList<>();
        sections.forEach(section -> stations.add(section.getUpStation()));
        stations.add(sections.get(sectionsSize()-1).getDownStation());

        return stations;
    }

    public boolean matchStation(Station station) {
        return findStations().stream().anyMatch(it -> it == station);
    }

    public boolean isEmptyByStation() {
        return findStations().isEmpty();
    }
}
