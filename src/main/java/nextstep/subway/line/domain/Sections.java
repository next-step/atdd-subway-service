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

    private static final String EXCEPTION_FOR_CANT_ADD_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String EXCEPTION_FOR_ALREADY_ADD_SECTION = "이미 등록된 구간 입니다.";
    private static final String EXCEPTION_FOR_ONLY_ONE_SECTION = "구간이 1개 이하일 때에는 제거가 불가합니다.";

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(section::isEqaulsUpStation);
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
                .ifPresent(preSection -> preSection.updateDownStation(section));
    }

    private void addInsideCaseEqualUpStation(Section section) {
        sections.stream()
                .filter(preSection -> preSection.hasSameUpStation(section))
                .findFirst()
                .ifPresent(preSection -> preSection.updateUpStation(section));
    }

    private void checkNonMatchSection(Section section, List<Station> stations) {
        if (!stations.isEmpty() && stations.stream().noneMatch(section::isEqaulsUpStation) &&
                stations.stream().noneMatch(section::isEqualsDownStation)) {
            throw new IllegalSectionException(EXCEPTION_FOR_CANT_ADD_SECTION);
        }
    }

    private void checkExistAddedSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalSectionException(EXCEPTION_FOR_ALREADY_ADD_SECTION);
        }
    }

    public List<Station> getStations() {
        Optional<Section> firstSection = findFirstSection();
        if (!firstSection.isPresent()) {
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
        checkLastSection();
        Optional<Section> upLineStation = findUpLineStation(station);
        Optional<Section> downLineStation = findDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sections.add(Section.of(upLineStation.get(), downLineStation.get()));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void checkLastSection() {
        if (sections.size() <= 1) {
            throw new IllegalSectionException(EXCEPTION_FOR_ONLY_ONE_SECTION);
        }
    }

    private Optional<Section> findUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.isEqaulsUpStation(station))
                .findFirst();
    }

    private Optional<Section> findDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.isEqualsDownStation(station))
                .findFirst();
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
