package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    public static final int SECTIONS_MINIMUM_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        validateSectionsSize(sections);
        this.sections = new ArrayList<>(sections);
    }

    public void add(Section section) {
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

    public void removeStation(Line line, Station station) {
        validateRemovableSize();
        mergeSections(line, station);
        this.sections.stream()
                .filter(section -> section.isMatchUpStation(station) || section.isMatchDownStation(station))
                .collect(Collectors.toList())
                .forEach(sections::remove);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    private Station findUpStation() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        return this.sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .map(Section::getUpStation)
                .orElse(null);
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
                .filter(it -> it.getDownStation().equals(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
        this.sections.add(section);
    }

    private void insertSectionBefore(Section section) {
        this.sections.stream()
                .filter(section::isMatchUpStationToUpStationBy)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        this.sections.add(section);
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

    private void mergeSections(Line line, Station station) {
        Optional<Section> upLineStation = this.sections.stream()
                .filter(it -> it.isMatchUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = this.sections.stream()
                .filter(it -> it.isMatchDownStation(station))
                .findFirst();
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }
    }

    private void validateRemovableSize() {
        if (this.sections.size() <= SECTIONS_MINIMUM_SIZE) {
            throw new IllegalStateException("지울 수 있는 구간이 없습니다.");
        }
    }
}
