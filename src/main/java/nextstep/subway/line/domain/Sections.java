package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;

@Embeddable
public class Sections {
    private static final int MINIMUM_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return getSortedStations();
    }

    private List<Station> getSortedStations() {
        Collections.sort(sections);
        List<Station> stations = new ArrayList<>();
        stations.add(findFirstUpStation());
        sections.forEach(section -> stations.add(section.getDownStation()));
        return stations;
    }

    private Station findFirstUpStation() {
        return sections.get(0).getUpStation();
    }

    private boolean hasStation(Station station) {
        return getStations().contains(station);
    }

    public void addLineStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }
        boolean isUpStationExisted = hasStation(upStation);
        boolean isDownStationExisted = hasStation(downStation);
        validateStationExistenceInStations(isUpStationExisted, isDownStationExisted);
        if (isUpStationExisted) {
            addUpStation(line, upStation, downStation, distance);
            return;
        }
        addDownStation(line, upStation, downStation, distance);
    }

    private void validateStationExistenceInStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void addDownStation(Line line, Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void addUpStation(Line line, Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }


    public void removeLineStation(Line line, Station station) {
        validateSectionSize();

        Optional<Section> upLineStation = getSectionByPredicate(section -> section.getUpStation() == station);
        Optional<Section> downLineStation = getSectionByPredicate(section -> section.getDownStation() == station);
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateSectionSize() {
        if (sections.size() <= MINIMUM_SECTION_SIZE) {
            throw new RuntimeException();
        }
    }

    private Optional<Section> getSectionByPredicate(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findFirst();
    }
}
