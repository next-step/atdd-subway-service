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

    public void removeSection(final Station station, final Line line) {
        if (sections.size() <= MIN_LIMIT) {
            throw new RuntimeException();
        }

        final Optional<Section> upLineStation = sections.stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        final Optional<Section> downLineStation = sections.stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
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

            currentSection = findSection(section -> Objects.equals(section.getUpStation(), currDownStation))
                .orElse(null);
        }

        return stations;
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
