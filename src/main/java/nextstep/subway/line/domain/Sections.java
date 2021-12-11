package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int REMOVE_SECTION_MIN_SIZE = 1;
    private static final int NOT_BETWEEN_SECTION = 1;
    private static final int BETWEEN_SECTION = 2;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    static Sections of(Section... sections) {
        return new Sections(new ArrayList<>(Arrays.asList(sections)));
    }

    public void add(Section section) {
        if (!sections.isEmpty()) {
            validateExistsUpStationAndDownStation(section);
            validateNotExistsUpStationAndDownStation(section);
            modifyIfSameUpStationExisted(section);
            modifyIfSameDownStationExisted(section);
        }
        sections.add(section);
    }

    public void remove(Station removeStation) {
        validateRemove();
        List<Section> findSections = findSections(removeStation);
        removeSectionNotBetweenSections(findSections);
        removeSectionBetweenSections(findSections);
    }

    public List<Section> getOrderedSections() {
        if (sections.isEmpty()) {
            return sections;
        }

        Section firstSection = findFirstSection();
        return makeOrderedSections(firstSection);
    }

    public List<Station> getOrderedStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        Station firstUpStation = findFirstUpStation();
        return makeOrderedStations(firstUpStation);
    }

    private void validateRemove() {
        if (sections.size() <= REMOVE_SECTION_MIN_SIZE) {
            throw new BadRequestException("구간을 제거할 수 없습니다.");
        }
    }

    private List<Section> findSections(Station removeStation) {
        return getOrderedSections().stream()
                .filter(section -> section.hasSameUpStation(removeStation)
                        || section.hasSameDownStation(removeStation))
                .collect(Collectors.toList());
    }

    private void removeSectionNotBetweenSections(List<Section> findSections) {
        if (findSections.size() == NOT_BETWEEN_SECTION) {
            sections.remove(findSections.get(0));
        }
    }

    private void removeSectionBetweenSections(List<Section> findSections) {
        if (findSections.size() == BETWEEN_SECTION) {
            Section upSection = findSections.get(0);
            Section downSection = findSections.get(1);
            upSection.merge(downSection);
            sections.remove(downSection);
        }
    }

    private void modifyIfSameUpStationExisted(Section section) {
        if (isExistsStation(section.getUpStation())) {
            sections.stream()
                    .filter(it -> it.hasSameUpStation(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.changeUpStationToAddSectionDownStation(section));
        }
    }

    private void modifyIfSameDownStationExisted(Section section) {
        if (isExistsStation(section.getDownStation())) {
            sections.stream()
                    .filter(it -> it.hasSameDownStation(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.changeDownStationToRemoveSectionUpStation(section));
        }
    }

    private List<Section> makeOrderedSections(Section downSection) {
        List<Section> orderedSections = new ArrayList<>();
        orderedSections.add(downSection);

        while (downSection != null) {
            Optional<Section> nextSection = findNextDownSection(downSection.getDownStation());

            if (!nextSection.isPresent()) {
                break;
            }

            downSection = nextSection.get();
            orderedSections.add(downSection);
        }

        return orderedSections;
    }

    private List<Station> makeOrderedStations(Station downStation) {
        List<Station> orderedStations = new ArrayList<>();
        orderedStations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextSection = findNextDownSection(downStation);

            if (!nextSection.isPresent()) {
                break;
            }

            downStation = nextSection.get().getDownStation();
            orderedStations.add(downStation);
        }
        return orderedStations;
    }

    private void validateExistsUpStationAndDownStation(Section section) {
        if (isExistsStation(section.getUpStation()) && isExistsStation(section.getDownStation())) {
            throw new BadRequestException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNotExistsUpStationAndDownStation(Section section) {
        if (isNotExistsStation(section.getUpStation()) && isNotExistsStation(section.getDownStation())) {
            throw new BadRequestException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isExistsStation(Station otherStation) {
        return getOrderedStations().stream()
                .anyMatch(station -> station.equals(otherStation));
    }

    private boolean isNotExistsStation(Station otherStation) {
        return getOrderedStations().stream()
                .noneMatch(station -> station.equals(otherStation));
    }

    private Section findFirstSection() {
        Section downSection = sections.get(0);
        while (downSection != null) {
            Optional<Section> nextSection = findNextUpSection(downSection.getUpStation());

            if (!nextSection.isPresent()) {
                break;
            }

            downSection = nextSection.get();
        }
        return downSection;
    }

    private Station findFirstUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextSection = findNextUpSection(downStation);

            if (!nextSection.isPresent()) {
                break;
            }

            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findNextDownSection(Station downStation) {
        return sections.stream()
                .filter(it -> it.hasSameUpStation(downStation))
                .findFirst();
    }

    private Optional<Section> findNextUpSection(Station downStation) {
        return sections.stream()
                .filter(it -> it.hasSameDownStation(downStation))
                .findFirst();
    }
}
