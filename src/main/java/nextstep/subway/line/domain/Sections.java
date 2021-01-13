package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.exception.NotFoundSectionException;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.path.domain.PathSelector;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int SECTION_MINIMUM_COUNT = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(){}

    public Sections(List<Section> sections) {
        addAll(sections);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        List<Station> stations = getStations();
        if (stations.isEmpty()) {
            finallyAdd(section);
            return;
        }
        validateSection(stations, section);

        if (stations.contains(section.getUpStation())) {
            updateUpStationInSections(section);
            finallyAdd(section);
            return;
        }
        updateDownStationInSections(section);
        finallyAdd(section);
    }

    public void addAll(Section ... sections) {
        addAll(Arrays.asList(sections));
    }

    public void addAll(Collection<Section> sections) {
        for ( Section section : sections ) {
            add(section);
        }
    }

    public void remove(Section section) {
        sections.remove(section);
        PathSelector.remove(section);
    }


    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findLastUpSection().getUpStation();
        stations.add(station);

        int size = sections.size();
        for (int idx = 0 ; idx < size ; idx ++) {
            station = getNextStation(station);
            stations.add(station);
        }
        return stations;
    }

    public void removeStation(Station station) {
        if (sections.size() <= SECTION_MINIMUM_COUNT) {
            throw new BadRequestException("한 노선에는 반드시 하나 이상의 구간이 필요합니다.");
        }
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst();
        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();

        upLineStation.ifPresent(this::remove);
        downLineStation.ifPresent(this::remove);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section upSection = upLineStation.get();
            Section downSection = downLineStation.get();
            add(upSection.merge(downSection));
        }
    }

    public void removeAllStation(Station ... stations) {
        removeAllStation(Arrays.asList(stations));
    }

    public void removeAllStation(Collection<Station> stations) {
        for (Station station : stations) {
            removeStation(station);
        }
    }

    public boolean containsSection(Station departure, Station destination) {
        return sections.stream()
                .anyMatch(section -> section.matchSection(departure, destination));
    }

    private void finallyAdd(Section section) {
        sections.add(section);
        PathSelector.add(section);
    }

    private void validateSection(List<Station> stations, Section section) {
        boolean isUpStationExisted = stations.contains(section.getUpStation());
        boolean isDownStationExisted = stations.contains(section.getDownStation());

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new InvalidSectionException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new InvalidSectionException("이미 등록된 구간 입니다.");
        }
    }

    private void updateUpStationInSections(Section section) {
        updateStationInSections(it -> it.equalsUpStation(section.getUpStation()),
                it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
    }

    private void updateDownStationInSections(Section section) {
        updateStationInSections(it -> it.equalsDownStation(section.getDownStation()),
                it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
    }

    private void updateStationInSections(Predicate<Section> filter, Consumer<Section> updater) {
        sections.stream()
                .filter(filter)
                .findFirst()
                .ifPresent(updater);
    }

    private Section findLastUpSection() {
        final Set<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toSet());

        return sections.stream()
                .filter(it -> !downStations.contains(it.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new NotFoundSectionException("종점 구간을 찾을 수 없습니다."));
    }

    private Station getNextStation(Station currentUpStation) {
        return sections.stream()
                .filter(it -> it.equalsUpStation(currentUpStation))
                .findFirst()
                .map(Section::getDownStation)
                .orElseThrow(() -> new NotFoundStationException(currentUpStation.getName() + " 다음 역을 찾을 수 없습니다."));
    }
}
