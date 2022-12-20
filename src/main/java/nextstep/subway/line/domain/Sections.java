package nextstep.subway.line.domain;

import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section addSection) {
        validateStations(addSection);
        Optional<Section> upSection = findUpSection(addSection);
        Optional<Section> downSection = findDownSection(addSection);

        upSection.ifPresent(section -> section.updateUpStation(addSection));
        downSection.ifPresent(section -> section.updateDownStation(addSection));
        sections.add(addSection);
    }
    private Optional<Section> findUpSection(Section addSection) {
        return sections.stream().filter(addSection::isSameUpStationBySection).findFirst();
    }

    private Optional<Section> findDownSection(Section addSection) {
        return sections.stream().filter(addSection::isSameDownStationBySection).findFirst();
    }
    private void validateStations(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        boolean isExistsUpStation = containUpStation(section);
        boolean isExistsDownStation = containDownStation(section);
        if (isExistsUpStation && isExistsDownStation) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");

        }
        if (!isExistsUpStation && !isExistsDownStation) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");

        }
    }

    private boolean containUpStation(Section section) {
        return distinctStations().contains(section.getUpStation());
    }

    private boolean containDownStation(Section section) {
        return distinctStations().contains(section.getDownStation());
    }

    private List<Station> distinctStations() {
        return sections.stream()
            .flatMap(Section::streamOfStation)
            .distinct()
            .collect(Collectors.toList());
    }

    public void deleteSection(Station station) {
        validateDeleteStation(station);
        Section upSection = findSectionByDownStation(station).orElse(null);
        Section downSection = findDownStationSection(station).orElse(null);

        if (upSection == null) {
            deleteSection(downSection);
            return;
        }
        if (downSection == null) {
            deleteSection(upSection);
            return;
        }
        upSection.mergeDownSection(downSection);
        deleteSection(downSection);
    }

    private void deleteSection(Section upSection) {
        sections.remove(upSection);
    }

    private void validateDeleteStation(Station station) {
        if (sections.size() <= 1) {
            throw new IllegalArgumentException("지하철 구간이 1개인 경우 삭제할 수 없습니다.");
        }

        if (!distinctStations().contains(station)) {
            throw new IllegalArgumentException("삭제하려는 지하철 역이 올바르지 않습니다.");
        }
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
            .filter(section -> section.hasDownStation(station))
            .findFirst();
    }

    private Optional<Section> findDownStationSection(Station station) {
        return sections.stream()
            .filter(section -> section.hasUpStation(station))
            .findFirst();
    }

    public List<Station> stationsBySorted() {
        Station firstUpStation = findFirstUpStation().orElse(null);
        if (firstUpStation == null) {
            return new ArrayList<>();
        }
        return sortStations(firstUpStation, stationsMap());
    }

    private List<Station> sortStations(Station firstUpStation, Map<Station, Station> stationMap) {
        List<Station> stations = new LinkedList<>();

        stations.add(firstUpStation);
        Station upStation = firstUpStation;
        while (stationMap.get(upStation) != null) {
            upStation = stationMap.get(upStation);
            stations.add(upStation);
        }
        return stations;
    }

    private Optional<Station> findFirstUpStation() {
        Set<Station> downStations = downStationsSet();
        return sections.stream()
            .map(Section::getUpStation)
            .filter(station -> !downStations.contains(station))
            .findFirst();
    }

    private Set<Station> downStationsSet() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toSet());
    }

    private Map<Station, Station> stationsMap() {
        return sections.stream()
            .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }
}
