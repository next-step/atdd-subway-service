package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> elements = new ArrayList<>();

    public List<Section> getElements() {
        return elements;
    }

    public List<Station> getStationsInOrder() {
        if (elements.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Section currentSection = findFirstSection();
        stations.add(currentSection.getUpStation());
        stations.add(currentSection.getDownStation());

        Optional<Section> nextSection = findNextSectionOf(currentSection);
        while (nextSection.isPresent()) {
            stations.add(nextSection.get().getDownStation());
            nextSection = findNextSectionOf(nextSection.get());
        }

        return stations;
    }

    public Section findFirstSection() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("구간 목록이 비어 있음");
        }

        Section currentSection = elements.get(0);
        Optional<Section> prevSection = findPrevSectionOf(currentSection);
        while (prevSection.isPresent()) {
            currentSection = prevSection.get();
            prevSection = findPrevSectionOf(currentSection);
        }

        return currentSection;
    }

    private Optional<Section> findPrevSectionOf(Section currentSection) {
        return elements.stream()
                .filter(section -> section.isPrevSectionOf(currentSection))
                .findFirst();
    }

    private Optional<Section> findNextSectionOf(Section currentSection) {
        return elements.stream()
                .filter(section -> section.isNextSectionOf(currentSection))
                .findFirst();
    }

    public void add(Section section) {
        validateStations(section);

        rearrangeElementsFor(section);
        elements.add(section);
    }

    private void validateStations(Section section) {
        if (elements.isEmpty()) {
            return;
        }

        if (isDuplicated(section)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (hasValidStation(section)) {
            throw new IllegalArgumentException("일치하는 역이 없어 등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isDuplicated(Section newSection) {
        return elements.stream()
                .anyMatch(section -> section.hasExactlySameStationsAs(newSection));
    }

    private boolean hasValidStation(Section newSection) {
        return elements.stream()
                .noneMatch(section -> section.hasAtLeastOneSameStationOf(newSection));
    }

    private void rearrangeElementsFor(Section section) {
        elements.stream()
                .filter(it -> it.getUpStation() == section.getUpStation())
                .findFirst()
                .ifPresent(it -> {
                    it.updateUpStation(section.getDownStation(), section.getDistance());
                    return;
                });

        elements.stream()
                .filter(it -> it.getDownStation() == section.getDownStation())
                .findFirst()
                .ifPresent(it -> {
                    it.updateDownStation(section.getUpStation(), section.getDistance());
                    return;
                });
    }

    public void removeStation(Station station) {
        if (elements.size() <= 1) {
            throw new IllegalStateException("구간이 하나뿐일 때는 삭제할 수 없습니다.");
        }

        Optional<Section> upSection = findSectionByDownStationSameAs(station);
        Optional<Section> downSection = findSectionByUpStationSameAs(station);

        if (downSection.isPresent() && upSection.isPresent()) {
            Station newUpStation = upSection.get().getUpStation();
            Station newDownStation = downSection.get().getDownStation();
            int newDistance = downSection.get().getDistance() + upSection.get().getDistance();
            elements.add(new Section(upSection.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        downSection.ifPresent(it -> elements.remove(it));
        upSection.ifPresent(it -> elements.remove(it));
    }

    private Optional<Section> findSectionByUpStationSameAs(Station station) {
        return elements.stream()
                .filter(section -> section.hasUpStationSameAs(station))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStationSameAs(Station station) {
        return elements.stream()
                .filter(section -> section.hasDownStationSameAs(station))
                .findFirst();
    }
}
