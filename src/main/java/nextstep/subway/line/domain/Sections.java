package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final int FIRST_SECTION_INDEX = 0;
    private static final int SECTIONS_MINIMUM_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        validateSectionsSize(sections);
        this.sections = new ArrayList<>(sections);
    }

    public void add(final Section section) {
        List<Station> stations = getStations();
        validateExistsStations(stations, section);
        validateNotExistsAllStations(stations, section);
        if (stations.isEmpty()) {
            this.sections.add(section);
            return;
        }
        addSectionForConnectedStation(stations, section);
    }

    public List<Station> getStations() {
        Map<Station, Station> upDownStations = this.sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        Station downStation = findUpStation();
        List<Station> stations = new ArrayList<>(Arrays.asList(downStation));
        while (upDownStations.containsKey(downStation)) {
            downStation = upDownStations.get(downStation);
            stations.add(downStation);
        }
        return stations;
    }

    public void removeStation(final Line line, final Station station) {
        validateRemovableSize();
        mergeSections(line, station);
        this.sections.stream()
                .filter(section -> section.hasContainBy(station))
                .collect(Collectors.toList())
                .forEach(sections::remove);
    }

    public List<Integer> getDistances() {
        return getSortSections().stream()
                .map(Section::getDistance)
                .collect(Collectors.toList());
    }

    private List<Section> getSortSections() {
        Map<Station, Section> collect = this.sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, section -> section));
        Station nextUpStation = findUpStation();
        List<Section> sortSections = new ArrayList<>();
        while (collect.containsKey(nextUpStation)) {
            sortSections.add(collect.get(nextUpStation));
            nextUpStation = collect.get(nextUpStation).getDownStation();
        }
        return sortSections;
    }

    private Station findUpStation() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        return this.sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .map(Section::getUpStation)
                .orElse(this.sections.get(FIRST_SECTION_INDEX).getUpStation());
    }

    private void addSectionForConnectedStation(List<Station> stations, Section section) {
        if (stations.stream().anyMatch(section::isMatchUpStation)) {
            insertSectionBefore(section);
            return;
        }
        insertSectionAfter(section);
    }

    private void insertSectionAfter(Section section) {
        this.sections.stream()
                .filter(section::isMatchDownStationToDownStationBy)
                .findFirst()
                .ifPresent(it -> it.updateDownStationAndDistanceFromUpStation(section));
        this.sections.add(section);
    }

    private void insertSectionBefore(Section section) {
        this.sections.stream()
                .filter(section::isMatchUpStationToUpStationBy)
                .findFirst()
                .ifPresent(it -> it.updateUpStationAndDistanceFromDownStation(section));
        this.sections.add(section);
    }

    private void mergeSections(Line line, Station station) {
        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.isMatchDownStation(station))
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.isMatchUpStation(station))
                .findFirst();
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            this.sections.add(Section.createMergeSection(line, upLineStation.get(), downLineStation.get()));
        }
    }

    private void validateNotExistsAllStations(List<Station> stations, Section section) {
        if (!stations.isEmpty() && stations.stream().noneMatch(section::isMatchUpStation) &&
                stations.stream().noneMatch(section::isMatchDownStation)) {
            throw new IllegalStateException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateExistsStations(List<Station> stations, Section section) {
        if (stations.stream().anyMatch(section::isMatchUpStation) &&
                stations.stream().anyMatch(section::isMatchDownStation)) {
            throw new IllegalStateException("이미 등록된 구간 입니다.");
        }
    }

    private void validateSectionsSize(List<Section> sections) {
        if (sections.size() < SECTIONS_MINIMUM_SIZE) {
            throw new IllegalArgumentException("구간 목록은 개수는 1 이상 이어야 합니다.");
        }
    }

    private void validateRemovableSize() {
        if (this.sections.size() <= SECTIONS_MINIMUM_SIZE) {
            throw new IllegalStateException("지울 수 있는 구간이 없습니다.");
        }
    }

    public Stream<Section> getStream() {
        return this.sections.stream();
    }
}
