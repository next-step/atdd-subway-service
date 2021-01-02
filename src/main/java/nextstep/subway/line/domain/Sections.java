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

        final boolean isUpStationExisted = stations.stream().anyMatch(it -> it == newSection.getUpStation());
        final boolean isDownStationExisted = stations.stream().anyMatch(it -> it == newSection.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException(ERR_TEXT_ALREADY_ADDED_SECTION);
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == newSection.getUpStation()) &&
            stations.stream().noneMatch(it -> it == newSection.getDownStation())) {
            throw new RuntimeException(ERR_TEXT_CAN_NOT_ADD_SECTION);
        }

        if (isUpStationExisted) {
            sections.stream()
                .filter(it -> it.getUpStation() == newSection.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));

            sections.add(newSection);
        } else if (isDownStationExisted) {
            sections.stream()
                .filter(it -> it.getDownStation() == newSection.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));

            sections.add(newSection);
        } else {
            throw new RuntimeException();
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
