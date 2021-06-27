package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section::isSameUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::isSameDownStation);

        validateExistStations(isUpStationExisted, isDownStationExisted);
        validateNotMatchedStations(section, stations);

        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }

        if (isUpStationExisted) {
            connectSectionIfHasStation(section, true);
            return;
        }

        connectSectionIfHasStation(section, false);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (hasStation(downStation, true)) {
            Section nextLineStation = findSection(downStation, true);
            downStation = nextLineStation.getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    public void removeStation(Line line, Station station) {
        validateRemovableLength();

        Optional<Section> upLineStation = findOptionalUpLineStation(station);
        Optional<Section> downLineStation = findOptionDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            connectSectionIfRemoveSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (hasStation(downStation, false)) {
            Section nextLineStation = findSection(downStation, false);
            downStation = nextLineStation.getUpStation();
        }
        return downStation;
    }

    private Section findSection(Station station, boolean hasUpStation) {
        return sections.stream()
                .filter(it -> it.hasSameStation(station, hasUpStation))
                .findFirst()
                .orElse(null);
    }

    private boolean hasStation(Station station, boolean hasUpStation) {
        return sections.stream()
                .filter(it -> it.isExistStation(hasUpStation))
                .anyMatch(it -> it.hasSameStation(station, hasUpStation));
    }

    private void connectSectionIfHasStation(Section section, boolean hasUpStation) {
        sections.stream()
                .filter(it -> section.hasSameStation(getStation(it, hasUpStation), hasUpStation))
                .findFirst()
                .ifPresent(it -> it.updateStationBySection(section, hasUpStation));
        sections.add(section);
    }

    private Station getStation(Section section, boolean hasUpStation) {
        if (hasUpStation) {
            return section.getUpStation();
        }
        return section.getDownStation();
    }

    private void validateNotMatchedStations(Section section, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(section::isSameUpStation) &&
                stations.stream().noneMatch(section::isSameDownStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateExistStations(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private Optional<Section> findOptionDownLineStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> findOptionalUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private void connectSectionIfRemoveSection(Line line, Section upLineStation, Section downLineStation) {
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(new Section(line, downLineStation.getUpStation(), upLineStation.getDownStation(), newDistance));
    }

    private void validateRemovableLength() {
        if (sections.size() <= 1) {
            throw new RuntimeException("구간이 1개 이하인 경우 제거할 수 없습니다.");
        }
    }
}
