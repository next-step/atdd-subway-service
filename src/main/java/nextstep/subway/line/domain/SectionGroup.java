package nextstep.subway.line.domain;

import nextstep.subway.line.exception.CanNotDeleteSectionException;
import nextstep.subway.line.exception.CanNotRegisterSectionException;
import nextstep.subway.line.exception.DuplicateSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Embeddable
public class SectionGroup {
    private static final int REMOVE_MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected SectionGroup() {
    }

    private SectionGroup(List<Section> sections) {
        this.sections.addAll(sections);
    }

    private SectionGroup(Section section) {
        this.sections.add(section);
    }

    public static SectionGroup of(List<Section> sections) {
        return new SectionGroup(sections);
    }

    public static SectionGroup of(Section section) {
        return new SectionGroup(section);
    }

    public void add(Section section) {
        this.sections.add((section));
    }

    public List<Section> getSections() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        return sections;
    }

    public List<Station> getStations() {
        return SectionGroupUptoDownSortUtils.sort(this.sections);
    }

    public void addSection(Section section) {

        final Station upStation = section.getUpStation();
        final Station downStation = section.getDownStation();
        final int distance = section.getDistance();

        boolean isUpStationExisted = isStationExist(upStation);
        boolean isDownStationExisted = isStationExist(downStation);

        validateAddSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
        }

        add(section);
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private boolean isStationExist(final Station station) {
        return this.sections.stream()
                .anyMatch(it -> it.matchAnyStation(station));
    }

    private void validateAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new DuplicateSectionException();
        }

        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new CanNotRegisterSectionException();
        }
    }

    public void removeLineStation(Line line, Station station) {
        validateRemoveMinSize();

        Optional<Section> upSection = getSectionByUpStation(station);
        Optional<Section> downSection = getSectionByDownStation(station);

        final boolean isExistUpAndDown = upSection.isPresent() && downSection.isPresent();
        if (isExistUpAndDown) {
            add(Section.merge(line, upSection.get(), downSection.get()));
        }

        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }

    private void validateRemoveMinSize() {
        if (sections.size() <= REMOVE_MIN_SIZE) {
            throw new CanNotDeleteSectionException();
        }
    }

    private Optional<Section> getSectionByDownStation(Station station) {
        final Predicate<Section> downStationFilter = it -> it.getDownStation() == station;
        return getSectionByFilter(downStationFilter);
    }

    private Optional<Section> getSectionByUpStation(Station station) {
        final Predicate<Section> upStationFilter = it -> it.getUpStation() == station;
        return getSectionByFilter(upStationFilter);
    }

    private Optional<Section> getSectionByFilter(Predicate<Section> downStationFilter) {
        return sections.stream()
                .filter(downStationFilter)
                .findAny();
    }
}
