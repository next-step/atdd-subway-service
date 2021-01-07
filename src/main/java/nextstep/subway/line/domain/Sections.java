package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exceptions.SectionsException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Sections {

    private static final String ALREADY_EXIST_SECTION = "이미 등록된 구간 입니다.";
    private static final String CANNOT_ADD_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String THIS_IS_LAST_SECTION = "마지막 구간입니다.";
    private static final String NOT_FOUND = "데이터를 찾을 수 없습니다.";
    private static final int SECTION_LIST_MINIMUM_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections(List<Section> section) {
        this.sections = section;
    }

    public List<Station> getOrderedStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Section firstSection = findFirstSection();
        stations.add(firstSection.getUpStation());

        Section nextSection = firstSection;
        while (nextSection != null) {
            stations.add(nextSection.getDownStation());
            nextSection = findNextSection(nextSection.getDownStation()).orElse(null);
        }
        return stations;
    }

    public void add(Section section) {
        List<Station> stations = getOrderedStations();
        if (stations.isEmpty()) {
            sections.add(section);
            return;
        }
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        validationAlreadyExist(isUpStationExisted, isDownStationExisted);
        validateNoneMatch(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateSectionUpStation(section, upStation, downStation);
        }

        if (isDownStationExisted) {
            updateSectionDownStation(section, upStation, downStation);
        }

        sections.add(section);
    }

    public void remove(Station station) {
        validateSectionSize();

        Optional<Section> upLineStation = findMatchWithUpStation(station);
        Optional<Section> downLineStation = findMathWithDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            updateSectionToRemove(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    private void updateSectionToRemove(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        this.sections.add(new Section(upLineStation.getLine(), newUpStation, newDownStation, newDistance));
    }

    private Optional<Section> findMathWithDownStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.isEqualWithDownStation(station))
                .findFirst();
    }

    private Optional<Section> findMatchWithUpStation(Station station) {
        return this.sections.stream()
                .filter(it -> it.isEqualWithUpStation(station))
                .findFirst();
    }


    private Optional<Section> findNextSection(Station downStation) {
        return sections.stream()
                .filter(section -> section.isEqualWithUpStation(downStation))
                .findFirst();
    }

    private Section findFirstSection() {
        List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return this.sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new SectionsException(NOT_FOUND));
    }


    private void updateSectionDownStation(Section section, Station upStation, Station downStation) {
        findMathWithDownStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, section.getDistance()));
    }

    private void updateSectionUpStation(Section section, Station upStation, Station downStation) {
        findMatchWithUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, section.getDistance()));
    }

    private void validateSectionSize() {
        if (this.sections.size() <= SECTION_LIST_MINIMUM_SIZE) {
            throw new SectionsException(THIS_IS_LAST_SECTION);
        }
    }

    private void validationAlreadyExist(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionsException(ALREADY_EXIST_SECTION);
        }
    }

    private void validateNoneMatch(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new SectionsException(CANNOT_ADD_SECTION);
        }
    }
}
