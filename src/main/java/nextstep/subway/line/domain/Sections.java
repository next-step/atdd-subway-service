package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final String ERROR_MESSAGE_DUPLICATE_UP_DOWN_STATION = "이미 등록된 구간 입니다.";
    private static final String ERROR_MESSAGE_NONE_MATCH_UP_DOWN_STATION = "등록할 수 없는 구간 입니다.";
    private static final String ERROR_MESSAGE_NOT_NULL_UP_STATION = "상행종점은 필수입니다.";
    private static final String ERROR_MESSAGE_LINE_NOT_CONTAIN_STATION = "해당 역이 존재하지 않습니다.";
    private static final String ERROR_MESSAGE_EXIST_DEFAULT_SECTION_SIZE = "노선의 구간은 1개 이상 존재해야 합니다.";
    private static final int DEFAULT_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addSection(Section addSection) {
        validDuplicateSection(addSection);
        validNotContainSection(addSection);

        Optional<Section> upSection = findUpSection(addSection);
        Optional<Section> downSection = findDownSection(addSection);

        upSection.ifPresent(section -> section.updateUpStation(addSection));
        downSection.ifPresent(section -> section.updateDownStation(addSection));
        sections.add(addSection);
    }

    private void validDuplicateSection(Section compareSection) {
        if (isContainsAllStation(compareSection)) {
            throw new InvalidParameterException(ERROR_MESSAGE_DUPLICATE_UP_DOWN_STATION);
        }
    }

    private boolean isContainsAllStation(Section compareSection) {
        return sections.stream().map(Section::stations)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .containsAll(compareSection.stations());
    }

    private void validNotContainSection(Section compareSection) {
        if (sections.isEmpty()) {
            return;
        }

        if (getSortStations().stream().noneMatch(station -> compareSection.stations().contains(station))) {
            throw new InvalidParameterException(ERROR_MESSAGE_NONE_MATCH_UP_DOWN_STATION);
        }
    }

    private Optional<Section> findUpSection(Section addSection) {
        return sections.stream().filter(addSection::isSameUpStationBySection).findFirst();
    }

    private Optional<Section> findDownSection(Section addSection) {
        return sections.stream().filter(addSection::isSameDownStationBySection).findFirst();
    }

    public void removeStation(Line line, Station station) {
        Optional<Section> sectionContainUpStation = findSectionContainUpStation(station);
        Optional<Section> sectionContainDownStation = findSectionContainDownStation(station);

        validDefaultSectionSize();
        validNotContainSectionByStation(sectionContainUpStation.isPresent(), sectionContainDownStation.isPresent());

        if (isStationIntermediateSections(sectionContainUpStation.isPresent(), sectionContainDownStation.isPresent())) {
            joinSections(line, sectionContainDownStation.get(), sectionContainUpStation.get());
        }

        sectionContainUpStation.ifPresent(section -> sections.remove(section));
        sectionContainDownStation.ifPresent(section -> sections.remove(section));
    }

    private void validDefaultSectionSize() {
        if (sections.size() == DEFAULT_SECTION_SIZE) {
            throw new InvalidParameterException(ERROR_MESSAGE_EXIST_DEFAULT_SECTION_SIZE);
        }
    }

    private void validNotContainSectionByStation(boolean upStation, boolean downStation) {
        if (!upStation && !downStation) {
            throw new InvalidParameterException(ERROR_MESSAGE_LINE_NOT_CONTAIN_STATION);
        }
    }

    private boolean isStationIntermediateSections(boolean upStation, boolean downStation) {
        return upStation && downStation;
    }

    private void joinSections(Line line, Section upSection, Section downSection) {
        Section section = Section.of(upSection.getUpStation(), downSection.getDownStation(),
                upSection.addDistance(downSection));
        section.toLine(line);
        sections.add(section);
    }

    private Optional<Section> findSectionContainUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isSameUpStationByStation(station))
                .findFirst();
    }

    private Optional<Section> findSectionContainDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isSameDownStationByStation(station))
                .findFirst();
    }

    public int totalDistance() {
        return sections.stream().map(Section::getDistance).mapToInt(Distance::getDistance).sum();
    }

    public List<Station> getSortStations() {
        Map<Station, Station> stationMap = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));

        return sortStations(findTopStation(), stationMap);
    }

    private List<Station> sortStations(Station topStation, Map<Station, Station> stationMap) {
        List<Station> stations = new LinkedList<>();
        stations.add(topStation);
        Station upStation = topStation;
        while (stationMap.get(upStation) != null) {
            upStation = stationMap.get(upStation);
            stations.add(upStation);
        }
        return stations;
    }

    private Station findTopStation() {
        Set<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet());

        return sections.stream().map(Section::getUpStation)
                .filter(station -> !downStations.contains(station))
                .findAny()
                .orElseThrow(() -> new InvalidParameterException(ERROR_MESSAGE_NOT_NULL_UP_STATION));
    }
}
