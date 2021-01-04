package nextstep.subway.line.domain;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Embeddable
public class Sections {
    private static final String ERR_TEXT_ALREADY_ADDED_SECTION = "이미 등록된 구간 입니다.";
    private static final String ERR_TEXT_CAN_NOT_ADD_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String ERR_TEXT_NOT_EXIST_DATA = "해당 데이터가 존재하지 않습니다.";
    private static final int MIN_LIMIT = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public static Sections create() {
        return new Sections();
    }

    public void add(final Section newSection) {
        if (this.sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        final List<Station> stations = getStations();

        final boolean isUpStationExisted = isExistStation(station -> station == newSection.getUpStation());
        final boolean isDownStationExisted = isExistStation(station -> station == newSection.getDownStation());

        validateStations(newSection, stations, isUpStationExisted, isDownStationExisted);

        changeOriginSection(newSection, isUpStationExisted, isDownStationExisted);

        sections.add(newSection);
    }

    private boolean isExistStation(final Predicate<Station> predicate) {
        return getStations().stream().anyMatch(predicate);
    }

    private void validateStations(final Section newSection, final List<Station> stations,
                                  final boolean isUpStationExisted, final boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ERR_TEXT_ALREADY_ADDED_SECTION);
        }

        if (!stations.contains(newSection.getUpStation()) && !stations.contains(newSection.getDownStation())) {
            throw new RuntimeException(ERR_TEXT_CAN_NOT_ADD_SECTION);
        }
    }

    private void changeOriginSection(final Section newSection, final boolean isUpStationExisted, final boolean isDownStationExisted) {
        if (isUpStationExisted) {
            findSection(section -> section.isMatchUpAndUpStation(newSection))
                .ifPresent(section -> section.updateUpStationByNewSection(newSection));
            return;
        }

        if (isDownStationExisted) {
            findSection(section -> section.isMatchDownAndDownStation(newSection))
                .ifPresent(section -> section.updateDownStationByNewSection(newSection));
        }
    }

    public void remove(final Station targetStation, final Line line) {
        isConditionThatCanBeDeleted(targetStation);

        final Optional<Section> upLineStation = findSection(section -> section.isMatchUpStation(targetStation));
        final Optional<Section> downLineStation = findSection(section -> section.isMatchDownStation(targetStation));

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addSectionWhenRemoveStationsIsIncludedInMoreThanOne(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private void addSectionWhenRemoveStationsIsIncludedInMoreThanOne(final Line line, final Section upLineStation, final Section downLineStation) {
        final Station newUpStation = downLineStation.getUpStation();
        final Station newDownStation = upLineStation.getDownStation();
        final int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.add(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void isConditionThatCanBeDeleted(final Station targetStation) {
        if (sections.size() <= MIN_LIMIT) {
            throw new RuntimeException();
        }

        final List<Station> stations = getStations();
        if (!stations.contains(targetStation)) {
            throw new IllegalArgumentException(ERR_TEXT_NOT_EXIST_DATA);
        }
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        final ArrayList<Station> stations = new ArrayList<>();
        Section currentSection = findFirstSection().orElseThrow(NotFoundException::new);
        stations.add(currentSection.getUpStation());

        while (currentSection != null) {
            final Station currDownStation = currentSection.getDownStation();
            stations.add(currDownStation);

            currentSection = findNextSectionByDownStation(currDownStation);
        }

        return stations;
    }

    private Section findNextSectionByDownStation(final Station currDownStation) {
        return findSection(section -> Objects.equals(section.getUpStation(), currDownStation))
            .orElse(null);
    }

    private Optional<Section> findFirstSection() {
        final List<Station> downStations = this.sections.stream()
            .map(Section::getDownStation)
            .collect(toList());

        return findSection(section -> !downStations.contains(section.getUpStation()));
    }

    private Optional<Section> findSection(final Predicate<Section> predicate) {
        return this.sections.stream()
            .filter(predicate)
            .findFirst();
    }
}
