package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int MINIMUM_SECTION_SIZE = 2;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    public void addSection(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        validateSection(newSection);
        sections.forEach(section -> section.update(newSection));
        sections.add(newSection);
    }

    private void validateSection(Section newSection) {
        if (hasUpStationAndDownStation(newSection)) {
            throw new IllegalArgumentException("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없습니다.");
        }
        if (hasNotUpStationAndDownStation(newSection)) {
            throw new IllegalArgumentException("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없습니다.");
        }
    }

    private boolean hasUpStationAndDownStation(Section newSection) {
        return has(newSection.getUpStation()) && has(newSection.getDownStation());
    }

    private boolean hasNotUpStationAndDownStation(Section newSection) {
        return hasNot(newSection.getUpStation()) && hasNot(newSection.getDownStation());
    }

    public List<Station> findAllStations() {
        return sections.stream()
                .map(Section::findStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public void remove(Station station) {
        validateStation(station);
        Optional<Section> upStationOptional = findUpStation(station);
        Optional<Section> downStationOptional = findDownStation(station);

        if (upStationOptional.isPresent() && downStationOptional.isPresent()) {
            addRearrangedSection(downStationOptional.get(), upStationOptional.get());
        }

        upStationOptional.ifPresent(section -> this.sections.remove(section));
        downStationOptional.ifPresent(section -> this.sections.remove(section));
    }

    private Optional<Section> findDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsDownStation(station))
                .findFirst();
    }

    private Optional<Section> findUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualsUpStation(station))
                .findFirst();
    }

    private void validateStation(Station station) {
        if (hasNot(station)) {
            throw new IllegalArgumentException("노선에 등록되지 않은 역은 제거할 수 없습니다.");
        }
        if (isLessThan()) {
            throw new IllegalArgumentException("구간이 하나인 경우, 역을 제거할 수 없습니다.");
        }
    }

    private boolean isLessThan() {
        return sections.size() < MINIMUM_SECTION_SIZE;
    }

    private boolean hasNot(Station station) {
        List<Station> allStations = findAllStations();
        return !allStations.contains(station);
    }

    private boolean has(Station station) {
        List<Station> allStations = findAllStations();
        return allStations.contains(station);
    }

    private void addRearrangedSection(Section upSection, Section downSection) {
        sections.add(upSection.rearrange(downSection));
    }

    public List<Station> findOrderedAllStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }
        return generateOrderedStations();
    }

    private List<Station> generateOrderedStations() {
        List<Station> stations = new ArrayList<>();
        Optional<Station> firstUpStationOptional = findFirstUpStation();

        while (firstUpStationOptional.isPresent()) {
            Station station = firstUpStationOptional.get();
            stations.add(station);
            firstUpStationOptional = findUpStation(station)
                    .map(Section::getDownStation);
        }

        return stations;
    }

    private Optional<Station> findFirstUpStation() {
        List<Station> downStations = findDownStations();
        return sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .map(Section::getUpStation)
                .findFirst();
    }

    private List<Station> findDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public int getSize() {
        return sections.size();
    }
}
