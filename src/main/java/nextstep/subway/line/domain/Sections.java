package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int MIN_SIZE = 1;
    private static final String STATION_PAIR_BOTH_EXISTS = "상행역(%s)과 하행역(%s)이 모두 노선에 등록되어 있으면 구간을 추가할 수 없습니다.";
    private static final String STATION_PAIR_NONE_EXISTS = "상행역(%s), 하행역(%s) 중 최소한 하나의 역이 등록되어 있어야 합니다.";
    private static final String STATION_NOT_EXISTS = "존재하지 않는 지하철 역 입니다.";
    private static final String LAST_SECTION = "구간이 하나인 노선에서 마지막 구간을 제거할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addSection(Section section) {
        if (!sections.isEmpty()) {
            validateDuplicate(section);
            validateExistence(section);
        }
        update(section);
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void validateDuplicate(Section target) {
        Optional<Section> optionalSection = sections.stream()
            .filter(element -> element.isSameStationPair(target))
            .findFirst();

        if (optionalSection.isPresent()) {
            throw new IllegalArgumentException(
                String.format(STATION_PAIR_BOTH_EXISTS, target.getUpStation(),
                    target.getDownStation()));
        }
    }

    public void validateExistence(Section target) {
        if (!isExistStation(target.getDownStation())
            && !isExistStation(target.getUpStation())) {
            throw new IllegalArgumentException(
                String.format(STATION_PAIR_NONE_EXISTS, target.getUpStation(),
                    target.getDownStation()));
        }
    }

    private boolean isExistStation(Station target) {
        Set<Station> stations = findAllStations();
        return stations.contains(target);
    }

    private Set<Station> findAllStations() {
        return sections.stream().
            flatMap(s -> s.getStationPair().stream()).
            collect(Collectors.toSet());
    }

    private void update(Section target) {
        for (Section section : sections) {
            section.update(target);
        }
    }

    public List<Station> stations() {
        if(sections.isEmpty()){
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstUpStation();

        while (downStation != null) {
            stations.add(downStation);
            downStation = getNextStation(downStation);
        }

        return stations;
    }

    private Station findFirstUpStation() {
        Station upStation = this.sections.get(0).getUpStation();
        while (upStation != null) {
            Optional<Station> previousStationOptional = getPreviousStation(upStation);
            if (!previousStationOptional.isPresent()) {
                break;
            }
            upStation = previousStationOptional.get();
        }

        return upStation;
    }

    private Optional<Station> getPreviousStation(Station upStation) {
        Optional<Section> previousSection = this.sections.stream()
            .filter(section -> section.getDownStation() == upStation)
            .findFirst();
        return previousSection.map(Section::getUpStation);
    }

    private Station getNextStation(Station downStation) {
        Optional<Section> nextSection = this.sections.stream()
            .filter(section -> section.getUpStation() == downStation)
            .findFirst();
        return nextSection.map(Section::getDownStation).orElse(null);
    }

    private List<Station> downStations() {
        return sections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList());
    }

    public void deleteStation(Station station) {
        Optional<Section> optionalDownSection = findSectionByUpStation(station);
        Optional<Section> optionalUpSection = findSectionByDownStation(station);

        validateSectionExist(optionalUpSection, optionalDownSection);
        validatesSectionsSize();
        optionalUpSection.ifPresent(sections::remove);
        optionalDownSection.ifPresent(sections::remove);

        if (optionalUpSection.isPresent() && optionalDownSection.isPresent()) {
            combineSection(optionalUpSection.get(), optionalDownSection.get());
        }
    }

    private Optional<Section> findSectionByUpStation(Station upStation) {
        return this.sections
            .stream()
            .filter(section -> section.equalsUpStation(upStation))
            .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station downStation) {
        return this.sections
            .stream()
            .filter(section -> section.equalsDownStation(downStation))
            .findFirst();
    }

    private void validateSectionExist(Optional<Section> optionalUpSection,
        Optional<Section> optionalDownSection) {
        if (!optionalUpSection.isPresent() && !optionalDownSection.isPresent()) {
            throw new IllegalArgumentException(STATION_NOT_EXISTS);
        }
    }

    private void validatesSectionsSize() {
        if (sections.size() <= MIN_SIZE) {
            throw new IllegalArgumentException(LAST_SECTION);
        }
    }

    private void combineSection(Section upSection, Section downSection) {
        Distance newDistance = Section.newDistance(upSection, downSection);
        Section newSection = Section.of(upSection.getLine(), upSection.getUpStation(),
            downSection.getDownStation(),
            newDistance);
        newSection.setLine(upSection.getLine());
        addSection(newSection);
    }

    public List<Section> getSections() {
        return sections;
    }

}

