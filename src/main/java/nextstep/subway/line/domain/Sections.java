package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

import static nextstep.subway.common.domain.BizExceptionMessages.*;
import static nextstep.subway.common.domain.BizMagicNumber.SECTION_MIN_SIZE;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstStation();
        stations.add(downStation);

        while (isPresentDownStation(downStation)) {
            Section preSection = getSectionBySameUpStation(downStation);
            downStation = preSection.getDownStation();
            stations.add(downStation);
        }

        return Collections.unmodifiableList(stations);
    }

    public void add(Line line, Station upStation, Station downStation, int distance) {
        boolean isUpStationExisted = isExistStation(upStation);
        boolean isDownStationExisted = isExistStation(downStation);
        validAddableSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
        }

        sections.add(new Section(line, upStation, downStation, distance));
    }

    public void remove(Station station) {
        validRemovableSection(station);

        Optional<Section> preSection = findSectionBySameUpStation(station);
        Optional<Section> nextSection = findSectionBySameDownStation(station);

        if (preSection.isPresent() && nextSection.isPresent()) {
            connectNewSection(preSection.get(), nextSection.get());
        }

        removeBeforeSections(preSection, nextSection);
    }

    public List<Section> values() {
        return Collections.unmodifiableList(sections);
    }

    public boolean isEnroll(Station station) {
        return isPresentUpStation(station) || isPresentDownStation(station);
    }

    public Line getLineByStations(Station upStation, Station downStation) {
        return sections.stream()
                .filter(it -> it.isSame(upStation, downStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SECTION_IS_NOT_CONTAIN_STATION.message()))
                .getLine();
    }

    private void connectNewSection(Section preSection, Section nextSection) {
        Line line = preSection.getLine();
        Station newUpStation = nextSection.getUpStation();
        Station newDownStation = preSection.getDownStation();
        int newDistance = preSection.getDistance() + nextSection.getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void removeBeforeSections(Optional<Section> preSection, Optional<Section> nextSection) {
        preSection.ifPresent(section -> sections.remove(section));
        nextSection.ifPresent(section -> sections.remove(section));
    }

    private Station findFirstStation() {
        Station upStation = sections.get(0).getUpStation();
        while (isPresentUpStation(upStation)) {
            Section preSection = getSectionBySameDownStation(upStation);
            upStation = preSection.getUpStation();
        }
        return upStation;
    }

    private boolean isPresentDownStation(Station station) {
        return sections.stream().anyMatch(it -> it.isSameWithUpStation(station));
    }

    private boolean isPresentUpStation(Station station) {
        return sections.stream().anyMatch(it -> it.isSameWithDownStation(station));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        findSectionBySameDownStation(downStation)
                .ifPresent(section -> section.updateDownStation(upStation, distance));
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        findSectionBySameUpStation(upStation)
                .ifPresent(section -> section.updateUpStation(downStation, distance));
    }

    private Optional<Section> findSectionBySameUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.isSameWithUpStation(station))
                .findFirst();
    }

    private Optional<Section> findSectionBySameDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.isSameWithDownStation(station))
                .findFirst();
    }

    private Section getSectionBySameUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.isSameWithUpStation(station))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SECTION_IS_NOT_CONTAIN_STATION.message()));
    }

    private Section getSectionBySameDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.isSameWithDownStation(station))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SECTION_IS_NOT_CONTAIN_STATION.message()));
    }

    private boolean isExistStation(Station station) {
        return getStations().stream().anyMatch(it -> it.isSame(station));
    }

    private void validAddableSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        validAddSameSection(isUpStationExisted, isDownStationExisted);
        validReachableSection(isUpStationExisted, isDownStationExisted);
    }

    private void validAddSameSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException(SECTION_ALREADY_REGISTERED.message());
        }
    }

    private void validReachableSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!getStations().isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalStateException(SECTION_NOT_REACHABLE_ANY_STATION.message());
        }
    }

    private void validRemovableSection(Station station) {
        validSectionsSize();
        validExistStation(station);
    }

    private void validExistStation(Station station) {
        if (!isExistStation(station)) {
            throw new IllegalStateException(SECTION_NOT_REACHABLE_ANY_STATION.message());
        }
    }

    private void validSectionsSize() {
        if (sections.size() <= SECTION_MIN_SIZE.number()) {
            throw new IllegalStateException(LINE_MIN_SECTIONS_SIZE.message());
        }
    }
}
