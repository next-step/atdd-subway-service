package nextstep.subway.line.domain;

import nextstep.subway.line.exception.IllegalSectionException;
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
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getStations();
        boolean isUpStationExisted = getStations().stream().anyMatch(section::isEqaulsUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::isEqualsDownStation);

        checkExistAddedSection(isUpStationExisted, isDownStationExisted);
        checkNonMatchSection(section, stations);


        addInsideCaseEqualUpStation(section);
        addInsideCaseEqualDownStation(section);
        sections.add(section);
    }

    private void addInsideCaseEqualDownStation(Section section) {
        sections.stream()
                .filter(preSection -> preSection.hasSameDownStation(section))
                .findFirst()
                .ifPresent(preSection -> {
                    preSection.updateDownStation(section);
                });
    }

    private void addInsideCaseEqualUpStation(Section section) {
        sections.stream()
                .filter(preSection -> preSection.hasSameUpStation(section))
                .findFirst()
                .ifPresent(preSection -> {
                    preSection.updateUpStation(section);
                });
    }

    private void checkNonMatchSection(Section section, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(section::isEqaulsUpStation) &&
                stations.stream().noneMatch(section::isEqualsDownStation)) {
            throw new IllegalSectionException("등록할 수 없는 구간 입니다.");
        }
    }

    private void checkExistAddedSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalSectionException("이미 등록된 구간 입니다.");
        }
    }

    public List<Station> getStations() {
        Optional<Section> firstSection = findFirstSection();
        if (!firstSection.isPresent()){
            return Collections.emptyList();
        }
        List<Station> otherStations = firstSection.get().findStations(this);
        return firstSection.get().findAllStations(otherStations);
    }

    private Optional<Section> findFirstSection() {
        return sections.stream()
                .filter(section -> section.isFirstSection(sections))
                .findFirst();
    }

    public void removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new IllegalSectionException("구간이 1개 이하일 때에는 제거가 불가합니다.");
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public Optional<Section> findSectionInUpStation(Section newSection) {
        return sections.stream()
                .filter(section -> section.isAfterSectionThan(newSection))
                .findFirst();
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
