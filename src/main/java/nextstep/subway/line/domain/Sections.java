package nextstep.subway.line.domain;

import nextstep.subway.error.SubwayInternalException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MINIMUM_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected static Sections empty() {
        return new Sections();
    }

    public List<Station> getSortedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Station> stations = new LinkedHashSet<>();
        Section currentSection = findFirstSection();
        Section endSection = findLastSection();

        while (!currentSection.equals(endSection)) {
            addStations(stations, currentSection);
            currentSection = findNextSection(currentSection);
        }
        addStations(stations, endSection);

        return new ArrayList<>(stations);
    }

    private void addStations(Set<Station> stations, Section currentSection) {
        stations.add(currentSection.getUpStation());
        stations.add(currentSection.getDownStation());
    }

    private Section findFirstSection() {
        Set<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toCollection(HashSet::new));

        return sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private Section findLastSection() {
        Set<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toCollection(HashSet::new));

        return sections.stream()
                .filter(section -> !upStations.contains(section.getDownStation()))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalException("구간이 존재하지 않습니다."));
    }

    private Section findNextSection(Section currentSection) {
        return sections.stream()
                .filter(section -> section.isLinkStation(currentSection))
                .findFirst()
                .orElseThrow(() -> new SubwayInternalException("구간이 존재하지 않습니다."));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        validationAlreadyAdded(newSection);
        validationNotAdded(newSection);

        updateStation(newSection);
        sections.add(newSection);
    }

    private void updateStation(Section newSection) {
        if (isUpStationExisted(newSection)) {
            sections.stream()
                    .filter(section -> section.equalsUpStation(newSection.getUpStation()))
                    .findFirst()
                    .ifPresent(section -> section.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
        }
        if (isDownStationExisted(newSection)) {
            sections.stream()
                    .filter(section -> section.equalsDownStation(newSection.getDownStation()))
                    .findFirst()
                    .ifPresent(section -> section.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
        }
    }

    private boolean isDownStationExisted(Section newSection) {
        return getSortedStations().stream().anyMatch(station -> station.equals(newSection.getDownStation()));
    }

    private boolean isUpStationExisted(Section newSection) {
        return getSortedStations().stream().anyMatch(station -> station.equals(newSection.getUpStation()));
    }

    private void validationAlreadyAdded(Section newSection) {
        if (isUpStationExisted(newSection) && isDownStationExisted(newSection)) {
            throw new SubwayInternalException("이미 등록된 구간 입니다.");
        }
    }

    private void validationNotAdded(Section newSection) {
        boolean duplicateUpStation = getSortedStations().stream()
                .noneMatch(station -> station.equals(newSection.getUpStation()));
        boolean duplicateDownStation = getSortedStations().stream()
                .noneMatch(station -> station.equals(newSection.getDownStation()));
        if (duplicateUpStation && duplicateDownStation) {
            throw new SubwayInternalException("등록할 수 없는 구간 입니다.");
        }
    }

    public void remove(Station targetStation) {
        validationSize();

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.equalsUpStation(targetStation))
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.equalsDownStation(targetStation))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addSection(upLineStation.get(), downLineStation.get());
        }
        upLineStation.ifPresent(section -> sections.remove(section));
        downLineStation.ifPresent(section -> sections.remove(section));
    }

    private void addSection(Section upSection, Section downSection) {
        Line newLine = upSection.getLine();
        Station newUpStation = downSection.getUpStation();
        Station newDownStation = upSection.getDownStation();
        int newDistance = upSection.getDistance() + downSection.getDistance();
        sections.add(new Section(newLine, newUpStation, newDownStation, newDistance));
    }

    private void validationSize() {
        if (sections.size() <= MINIMUM_SIZE) {
            throw new SubwayInternalException("구간이 최소 하나는 있어야 합니다.");
        }
    }
}
