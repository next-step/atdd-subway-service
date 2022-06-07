package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.ImpossibleDeleteException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    public static final int MIN = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections valueOf(List<Section> sections) {
        return new Sections(sections);
    }

    public void addSection(Section newSection) {
        if (!isSectionsEmpty()) {
            validateNotAddedSection(newSection);
            validateContainsUpStationOrDownStation(newSection);
            update(newSection);
        }
        sections.add(newSection);
    }

    private boolean isSectionsEmpty() {
        return sections.isEmpty();
    }

    private void validateNotAddedSection(Section section) {
        if (containsUpStationAndDownStation(section)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private boolean containsUpStationAndDownStation(Section section) {
        return isUpStationExisted(section) && isDownStationExisted(section);
    }

    private void validateContainsUpStationOrDownStation(Section section) {
        if (containsNoneOfUpStationAndDownStation(section)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean containsNoneOfUpStationAndDownStation(Section section) {
        return !isUpStationExisted(section) && !isDownStationExisted(section);
    }

    private boolean isUpStationExisted(Section section) {
        return orderedStations().stream().anyMatch(station -> station.equals(section.upStation()));
    }

    private boolean isDownStationExisted(Section section) {
        return orderedStations().stream().anyMatch(station -> station.equals(section.downStation()));
    }

    public List<Station> orderedStations() {
        Map<Station, Station> section = sections.stream()
                .collect(Collectors.toMap(Section::upStation, Section::downStation));
        Station downStation = findUpStation();
        List<Station> stations = new ArrayList<>();
        stations.add(downStation);
        while (section.containsKey(downStation)) {
            stations.add(section.get(downStation));
            downStation = section.get(downStation);
        }
        return stations;
    }

    private void update(Section newSection) {
        sections.forEach(section -> section.update(newSection));
    }

    public void deleteSection(Station station) {
        validateNumberOfSections();
        if (isEqualToFirstUpStation(station)) {
            sections.remove(findSectionWithSameUpStation(station));
            return;
        }
        if (isEqualToLastDownStation(station)) {
            sections.remove(findSectionWithSameDownStation(station));
            return;
        }
        Section nextSection = findSectionWithSameUpStation(station);
        Section prevSection = findSectionWithSameDownStation(station);
        sections.remove(nextSection);
        sections.remove(prevSection);
        sections.add(mergeSection(prevSection, nextSection));
    }

    private void validateNumberOfSections() {
        if (!isSizeMoreThanMin()) {
            throw new ImpossibleDeleteException("제거 가능한 구간이 없습니다.");
        }
    }

    private boolean isSizeMoreThanMin() {
        return sections.size() > MIN;
    }

    private boolean isEqualToFirstUpStation(Station station) {
        return station.equals(findUpStation());
    }

    private Station findUpStation() {
        Set<Station> upStations = getUpStations();
        Set<Station> downStations = getDownStations();
        return upStations.stream()
                .filter(upStation -> !downStations.contains(upStation))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("상행 종점역을 찾을 수 없습니다."));
    }

    private boolean isEqualToLastDownStation(Station station) {
        return station.equals(findDownStation());
    }

    private Station findDownStation() {
        Set<Station> upStations = getUpStations();
        Set<Station> downStations = getDownStations();
        return downStations.stream()
                .filter(downStation -> !upStations.contains(downStation))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("하행 종점역을 찾을 수 없습니다."));
    }

    private Set<Station> getUpStations() {
        return sections.stream()
                .map(Section::upStation)
                .collect(Collectors.toSet());
    }

    private Set<Station> getDownStations() {
        return sections.stream()
                .map(Section::downStation)
                .collect(Collectors.toSet());
    }

    private Section findSectionWithSameUpStation(Station station) {
        return sections.stream()
                .filter(section -> station.equals(section.upStation()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("일치하는 상행역이 없습니다."));
    }

    private Section findSectionWithSameDownStation(Station station) {
        return sections.stream()
                .filter(section -> station.equals(section.downStation()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("일치하는 하행역이 없습니다."));
    }

    private Section mergeSection(Section prevSection, Section nextSection) {
        prevSection.distance().plus(nextSection.distance());
        return Section.builder(prevSection.line(), prevSection.upStation(), nextSection.downStation(),
                        prevSection.distance())
                .build();
    }

    public Distance distance() {
        return Distance.valueOf(sections.stream()
                .mapToInt(section -> section.distance().distance())
                .reduce(0, Integer::sum));
    }
}
