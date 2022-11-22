package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int ONLY_ONE_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void addSection(Section newSection) {
        validateDuplicateSection(newSection);
        validateNotContainAnySection(newSection);
        validateLine(newSection);

        Optional<Section> upStationSection = findSectionByUpStation(newSection.getUpStation());
        Optional<Section> downStationSection = findSectionByDownStation(newSection.getDownStation());
        upStationSection.ifPresent(section -> section.updateUpStation(newSection));
        downStationSection.ifPresent(section -> section.updateDownStation(newSection));
        sections.add(newSection);
    }

    private void validateDuplicateSection(Section section) {
        if(isAllContainStations(section)) {
            throw new IllegalArgumentException(ErrorCode.이미_존재하는_구간.getErrorMessage());
        }
    }

    private boolean isAllContainStations(Section section) {
        return findStations().containsAll(section.stations());
    }

    private void validateNotContainAnySection(Section section) {
        if(isNotContainAnyStation(section)) {
            throw new IllegalArgumentException(ErrorCode.구간의_상행역과_하행역이_모두_노선에_포함되지_않음.getErrorMessage());
        }
    }

    private boolean isNotContainAnyStation(Section section) {
        return findStations().stream()
                .noneMatch(section::isContainStation);
    }

    private void validateLine(Section section) {
        if(isNotEqualLine(section)) {
            throw new IllegalArgumentException(ErrorCode.구간의_노선이_기존_구간들과_상이함.getErrorMessage());
        }
    }

    private boolean isNotEqualLine(Section section) {
        if(sections.isEmpty()) {
            return false;
        }
        return !sections.get(0).hasSameLine(section);
    }

    public void removeStationInLine(Station deleteStation) {
        validateIfOnlyOneSection();
        Optional<Section> upStationSection = findSectionByUpStation(deleteStation);
        Optional<Section> downStationSection = findSectionByDownStation(deleteStation);
        validateNotContainAnySection(upStationSection.isPresent(), downStationSection.isPresent());

        if(isInTheMiddleOfLine(upStationSection.isPresent(), downStationSection.isPresent())) {
            addCombineSection(downStationSection.get(), upStationSection.get());
        }
        upStationSection.ifPresent(this::deleteSection);
        downStationSection.ifPresent(this::deleteSection);
    }

    private void validateIfOnlyOneSection() {
        if(sections.size() == ONLY_ONE_SECTION) {
            throw new IllegalArgumentException(ErrorCode.노선에_속한_구간이_하나이면_제거_불가.getErrorMessage());
        }
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(station))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isSameDownStation(station))
                .findFirst();
    }

    private void validateNotContainAnySection(boolean hasUpStationSection, boolean hasDownStationSection) {
        if(hasNotBothUpAndDownStationSection(hasUpStationSection, hasDownStationSection)) {
            throw new IllegalArgumentException(ErrorCode.노선_내_존재하지_않는_역.getErrorMessage());
        }
    }

    private boolean hasNotBothUpAndDownStationSection(boolean hasUpStationSection, boolean hasDownStationSection) {
        return !hasUpStationSection && !hasDownStationSection;
    }

    private boolean isInTheMiddleOfLine(boolean hasUpStationSection, boolean hasDownStationSection) {
        return hasUpStationSection && hasDownStationSection;
    }

    private void addCombineSection(Section upSection, Section downSection) {
        Distance distance = upSection.findAddDistance(downSection);
        Section section = Section.of(upSection.getLine(), upSection.getUpStation(), downSection.getDownStation(), distance.value());
        sections.add(section);
    }

    private void deleteSection(Section deleteSection) {
        sections.removeIf(section -> section.isSameSection(deleteSection));
    }

    public List<Station> findStations() {
        return sections.stream()
                .map(Section::stations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Station> findInOrderStations() {
        Map<Station, Station> stations = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        return sortStations(findLineUpStation(stations), stations);
    }

    private Station findLineUpStation(Map<Station, Station> stations) {
        return stations.keySet()
                .stream()
                .filter(upStation -> !stations.containsValue(upStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.상행종착역은_비어있을_수_없음.getErrorMessage()));
    }

    private List<Station> sortStations(Station lineUpStation, Map<Station, Station> stations) {
        List<Station> sortStations = new ArrayList<>();
        sortStations.add(lineUpStation);
        Station currentStation = lineUpStation;
        while(stations.get(currentStation) != null) {
            currentStation = stations.get(currentStation);
            sortStations.add(currentStation);
        }
        return sortStations;
    }
}
