package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int SECTION_ONE = 1;
    private static final String SECTION_DUPLICATION_ERROR = "상행선과 하행선이 모두 존재합니다.";
    private static final String SECTION_NOT_EXIST_ERROR = "상행선과 하행선이 모두 존재하지 않습니다.";
    private static final String SECTION_ONE_ERROR = "노선의 구간이 하나면 제거할 수 없습니다.";
    private static final String SECTION_NOT_EXIST_STATION_ERROR = "노선에 등록되지 않은 역은 삭제할 수 없습니다.";

    private static final String UP_STATION_NOT_NULL_ERROR = "상행 종착역이 존재하지 않습니다.";

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        validateDuplicate(section);
        validateNotExist(section);
        sections.forEach(s -> s.updateStation(section));
        sections.add(section);
    }

    private void validateDuplicate(Section section) {
        if (getStations().containsAll(section.getStations())) {
            throw new IllegalArgumentException(SECTION_DUPLICATION_ERROR);
        }
    }

    private void validateNotExist(Section section) {
        if (isNotExist(section)) {
            throw new IllegalArgumentException(SECTION_NOT_EXIST_ERROR);
        }
    }

    private boolean isNotExist(Section section) {
        List<Station> stations = getStations();
        return section.getStations()
            .stream()
            .noneMatch(stations::contains);
    }

    public void remove(Station station) {
        validateOneSection();
        validateNotExistStation(station);

        Optional<Section> upStationSection = findUpStationSection(station);
        Optional<Section> downStationSection = findDownStationSection(station);

        if(upStationSection.isPresent() && downStationSection.isPresent()) {
            addConnectSection(downStationSection.get(), upStationSection.get());
        }
        upStationSection.ifPresent(sections::remove);
        downStationSection.ifPresent(sections::remove);
    }

    private void validateOneSection() {
        if (sections.size() == SECTION_ONE) {
            throw new IllegalArgumentException(SECTION_ONE_ERROR);
        }
    }

    private void validateNotExistStation(Station station) {
        if (!getStations().contains(station)) {
            throw new IllegalArgumentException(SECTION_NOT_EXIST_STATION_ERROR);
        }
    }

    private void addConnectSection(Section upSection, Section downSection) {
        Distance distance = upSection.addDistance(downSection);
        Section section = Section.of(upSection.getLine(), upSection.getUpStation(), downSection.getDownStation(), distance.value());
        sections.add(section);
    }

    private Optional<Section> findUpStationSection(Station station) {
        return sections.stream()
            .filter(section -> section.isUpStation(station))
            .findFirst();
    }

    private Optional<Section> findDownStationSection(Station station) {
        return sections.stream()
            .filter(section -> section.isDownStation(station))
            .findFirst();
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Station, Station> stations = sectionsToMap();
        return sortStations(stations, findUpStation(stations));
    }

    private Map<Station, Station> sectionsToMap() {
        return sections.stream()
            .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
    }

    private Station findUpStation(Map<Station, Station> stations) {
        return stations.keySet()
            .stream()
            .filter(upStation -> !stations.containsValue(upStation))
            .findFirst()
            .orElseThrow(() -> new NoResultException(UP_STATION_NOT_NULL_ERROR));
    }

    private List<Station> sortStations(Map<Station, Station> stations, Station upStation) {
        List<Station> sortedStations = new ArrayList<>();

        Station currentStation = upStation;
        while (currentStation != null) {
            sortedStations.add(currentStation);
            currentStation = stations.get(currentStation);
        }

        return sortedStations;
    }

    public List<Section> getSections() {
        return sections;
    }
}
