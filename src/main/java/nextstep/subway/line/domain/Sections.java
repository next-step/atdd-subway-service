package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    public Sections(){
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            addSection(section);
            return;
        }

        if (isValidAndUpStationOrDownStation(section)) {
            addSection(section);
            return;
        }

        updateExitSection(section);
        addSection(section);
    }

    private boolean isValidAndUpStationOrDownStation(Section section) {
        boolean isUpStationContains = isContains(section.getUpStation());
        boolean isDownStationContains = isContains(section.getDownStation());

        validContains(isUpStationContains, isDownStationContains);
        return isUpStationContains && isDownStationContains;
    }

    private void validContains(boolean isUpStationContains, boolean isDownStationContains) {
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

    private void updateExitSection(Section newSection) {
        Optional<Section> optionalSection = this.sections.stream()
                .filter(section -> section.isContains(newSection.getUpStation()) || section.isContains(newSection.getDownStation()))
                .findFirst();

        Section exitSection = optionalSection.get();
        validDistance(newSection.getDistance(), exitSection.getDistance());

        this.sections = this.sections.stream()
                .filter(section -> section.equals(exitSection))
                .map(section -> {section.updateSection(newSection); return section;})
                .collect(Collectors.toList());
    }

    private void validDistance(int newDistance, int exitDistance) {
        if (exitDistance <= newDistance) {
            throw new IllegalArgumentException("신규 구간 입력 시 기존 구간보다 길이가 작아야합니다.");
        }
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
                .filter(station -> isNotContainsDownStation(station))
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
}
