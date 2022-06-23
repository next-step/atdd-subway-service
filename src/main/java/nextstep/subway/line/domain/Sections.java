package nextstep.subway.line.domain;

import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.station.domain.Station;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {
    private static final int MIN_SECTION_SIZE = 1;
    private static final String ERROR_MESSAGE_ALL_ALREADY_REGISTERED = "상행 역과 하행 역이 이미 모두 등록되어 있어 구간을 추가할 수 없습니다.";
    private static final String ERROR_MESSAGE_NOT_CONTAINS = "상행 역과 하행 역 둘 중 하나도 포함되어있지 않아 구간을 추가할 수 없습니다.";
    private static final String ERROR_MESSAGE_MIN_SECTION_SIZE = "구간이 1개 이하인 노선은 구간을 삭제할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            this.sections.add(newSection);
            return;
        }
        validateAddSection(newSection);
        sections.forEach(section -> section.update(newSection));
        sections.add(newSection);
    }

    private void validateAddSection(Section newSection) {
        if (hasUpStationAndDownStation(newSection)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ALL_ALREADY_REGISTERED);
        }
        if (hasNotUpStationAndDownStation(newSection)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_CONTAINS);
        }
    }

    private boolean hasUpStationAndDownStation(Section newSection) {
        List<Station> stations = findAllStations();
        return stations.contains(newSection.getUpStation()) && stations.contains(newSection.getDownStation());
    }

    private boolean hasNotUpStationAndDownStation(Section newSection) {
        List<Station> stations = findAllStations();
        return !stations.contains(newSection.getUpStation()) && !stations.contains(newSection.getDownStation());
    }

    public void delete(Station station) throws NotFoundStationException {
        validateDelete(station);

        Optional<Section> downSection = findSectionByUpStation(station);
        Optional<Section> upSection = findSectionByDownStation(station);

        if (downSection.isPresent() && upSection.isPresent()) {
            Station newUpStation = upSection.get().getUpStation();
            Station newDownStation = downSection.get().getDownStation();
            int newDistance = downSection.get().getDistance().getDistance() + upSection.get().getDistance().getDistance();
            sections.add(new Section(downSection.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        downSection.ifPresent(it -> sections.remove(it));
        upSection.ifPresent(it -> sections.remove(it));
    }

    private void validateDelete(Station station) throws NotFoundStationException {
        if (!findAllStations().contains(station)) {
            throw new NotFoundStationException(station.getId());
        }
        if (sections.size() <= MIN_SECTION_SIZE) {
            throw new IllegalArgumentException(ERROR_MESSAGE_MIN_SECTION_SIZE);
        }
    }

    public Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    public Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private List<Station> findAllStations() {
        return Collections.unmodifiableList(sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList()));
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station nextStation = findFirstUpStation();

        while (nextStation != null) {
            stations.add(nextStation);
            nextStation = getNextStation(nextStation);
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
        Optional<Section> previousSection = findSectionByDownStation(upStation);
        return previousSection.map(Section::getUpStation);
    }

    private Station getNextStation(Station station) {
        Optional<Section> nextSection = findSectionByUpStation(station);
        return nextSection.map(Section::getDownStation).orElse(null);
    }
}
