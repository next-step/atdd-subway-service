package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
            connectSectionIfHasSameUpStation(section);
            return;
        }

        connectSectionIfHasSameDownStation(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (hasNextSection(downStation)) {
            Section nextLineStation = findNextSection(downStation);
            downStation = nextLineStation.getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    public void removeStation(Line line, Station station) {
        validateRemovableLength();

        Optional<Section> upLineStation = findOptionalSectionByCondition(it -> it.getUpStation() == station);
        Optional<Section> downLineStation = findOptionalSectionByCondition(it -> it.getDownStation() == station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            connectSectionIfRemoveSection(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    public List<Section> getSections() {
        return sections;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (hasPreSection(downStation)) {
            Section nextLineStation = findPreSection(downStation);
            downStation = nextLineStation.getUpStation();
        }
        return downStation;
    }

    private Section findPreSection(Station station) {
        return findFirstSection(it -> it.isSameDownStation(station));
    }

    private Section findNextSection(Station station) {
        return findFirstSection(it -> it.isSameUpStation(station));
    }

    private Section findFirstSection(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    private boolean hasNextSection(Station station) {
        return hasMatchedSection(it -> it.getUpStation() != null, it -> it.isSameUpStation(station));
    }

    private boolean hasPreSection(Station station) {
        return hasMatchedSection(it -> it.getDownStation() != null, it -> it.isSameDownStation(station));
    }

    private boolean hasMatchedSection(Predicate<Section> filter, Predicate<Section> anyMatch) {
        return sections.stream()
                .filter(filter)
                .anyMatch(anyMatch);
    }

    private void connectSectionIfHasSameDownStation(Section section) {
        sections.stream()
                .filter(it -> section.isSameDownStation(it.getDownStation()))
                .findFirst()
                .ifPresent(it -> it.updateDownStationBySection(section));
        sections.add(section);
    }

    private void connectSectionIfHasSameUpStation(Section section) {
        sections.stream()
                .filter(it -> section.isSameUpStation(it.getUpStation()))
                .findFirst()
                .ifPresent(it -> it.updateUpStationBySection(section));
        sections.add(section);
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

    private Optional<Section> findOptionalSectionByCondition(Predicate<Section> predicate) {
        return sections.stream()
                .filter(predicate)
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
