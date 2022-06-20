package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(){
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            addSection(section);
            return;
        }

        boolean isUpStationContains = isContains(section.getUpStation());
        boolean isDownStationContains = isContains(section.getDownStation());
        validation(isUpStationContains, isDownStationContains);

        if (isUpStationContains) {
            calculateDistanceUpSection(section);
        }

        if (isDownStationContains) {
            calculateDistanceDownSection(section);
        }

        this.sections.add(section);
    }

    private void validation(boolean isUpStationContains, boolean isDownStationContains) {
        if (isUpStationContains && isDownStationContains) {
            throw new IllegalArgumentException("상행역과 하행역이 모두 등록되어 있으면 추가할 수 없습니다.");
        }

        if (!isUpStationContains && !isDownStationContains) {
            throw new IllegalArgumentException("상행역과 하행역 중 하나는 포함되어야 합니다.");
        }
    }

    private boolean isContains(Station station) {
        return this.sections.stream()
                .anyMatch(section -> section.isContains(station));
    }

    private void calculateDistanceUpSection(Section newSection) {
        sections.stream()
                .filter(it -> it.getUpStation().equals(newSection.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
    }

    private void calculateDistanceDownSection(Section newSection) {
        sections.stream()
                .filter(it -> it.getDownStation().equals(newSection.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
    }

    private void addSection(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        return createStations();
    }

    private List<Station> createStations() {
        List<Station> stations = new ArrayList<>();

        Section section = getFirstSection();
        stations.add(section.getUpStation());
        stations.add(section.getDownStation());

        findStations(stations, section);
        return stations;
    }

    private Section getFirstSection() {
        Station station = findUpStation();

        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("노선의 역을 찾을 수 없습니다."));
    }

    private Station findUpStation() {
        List<Station> upStations = this.sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        return upStations.stream()
                .filter(this::isNotContainsDownStation)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("노선의 역을 찾을 수 없습니다."));
    }

    private boolean isNotContainsDownStation(Station station) {
        return sections.stream()
                .noneMatch(section -> section.getDownStation().equals(station));
    }

    private void findStations(List<Station> stations, Section section) {
        Optional<Section> optionalSection = findNextDownSection(section);

        while (optionalSection.isPresent()) {
            stations.add(optionalSection.get().getDownStation());
            optionalSection = findNextDownSection(optionalSection.get());
        }
    }

    private Optional<Section> findNextDownSection(Section preSection) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(preSection.getDownStation()))
                .findFirst();
    }

    public void delete(Station station) {
        validSectionsSizeCheck();
        Section upSection = sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst()
                .orElse(Section.empty());

        Section downSection = sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst()
                .orElse(Section.empty());

        if (isValidAndEndStation(upSection, downSection)) {
            createSection(upSection, downSection);
        }

        removeSection(upSection, downSection);
    }

    private void validSectionsSizeCheck() {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("구간이 하나인 노선인 경우 구간 삭제 할 수 없습니다.");
        }
    }

    private boolean isValidAndEndStation(Section upSection, Section downSection) {
        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new IllegalArgumentException("노선에 등록된 역이 아닙니다.");
        }

        return !upSection.isEmpty() && !downSection.isEmpty();
    }

    private void createSection(Section upSection, Section downSection) {
        addSection(new Section(upSection.getLine(), upSection.getUpStation(), downSection.getDownStation(), upSection.getDistance()+downSection.getDistance()));
    }

    private void removeSection(Section upSection, Section downSection) {
        if (upSection != null) {
            sections.remove(upSection);
        }

        if (downSection != null) {
            sections.remove(downSection);
        }
    }
}
