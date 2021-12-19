package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int SECTION_MIN_SIZE = 1;
    private static final int LAST_STOP_SECTION = 1;
    private static final int BETWEEN_SECTIONS = 2;

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

    public static Sections of(List<Section> sections) {
        return new Sections(sections);
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

    public void remove(Station station) {
        validateRemove();
        List<Section> findSections = findSectionsBy(station);

        if (findSections.size() == LAST_STOP_SECTION) {
            sections.remove(findSections.get(0));
        }

        if (findSections.size() == BETWEEN_SECTIONS) {
            Section upSection = findSections.get(0);
            Section downSection = findSections.get(1);
            upSection.merge(downSection);
            sections.remove(downSection);
        }
    }

    public List<Section> getOrderedSections() {
        if (sections.isEmpty()) {
            return sections;
        }

        Section firstSection = sections.get(0);
        while (firstSection != null) {
            Optional<Section> nextSection = findNextUpSection(firstSection.getUpStation());
            if (!nextSection.isPresent()) {
                break;
            }
            firstSection = nextSection.get();
        }

        return makeOrderedSections(firstSection);
    }

    public List<Station> getOrderedStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        Station firstUpStation = sections.get(0).getUpStation();
        while (firstUpStation != null) {
            Optional<Section> nextSection = findNextUpSection(firstUpStation);

            if (!nextSection.isPresent()) {
                break;
            }

            firstUpStation = nextSection.get().getUpStation();
        }

        return makeOrderedStations(firstUpStation);
    }

    public Fare getMaxExtraFare() {
        return sections.stream()
                .map(section -> section.getLine()
                        .getExtraFare()
                        .value())
                .max(BigDecimal::compareTo)
                .map(Fare::from)
                .orElse(Fare.from(BigDecimal.ZERO));
    }

    private void validateRemove() {
        if (sections.size() <= SECTION_MIN_SIZE) {
            throw new BadRequestException("구간을 제거할 수 없습니다.");
        }
    }

    private List<Section> findSectionsBy(Station station) {
        return getOrderedSections().stream()
                .filter(section -> section.hasSameUpStation(station)
                        || section.hasSameDownStation(station))
                .collect(Collectors.toList());
    }

    private void modifyIfSameUpStationExisted(Section section) {
        if (isExistsStation(section.getUpStation())) {
            sections.stream()
                    .filter(it -> it.hasSameUpStation(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.changeUpStationToDownStationOf(section));
        }
    }

    private void modifyIfSameDownStationExisted(Section section) {
        if (isExistsStation(section.getDownStation())) {
            sections.stream()
                    .filter(it -> it.hasSameDownStation(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.changeDownStationToUpStationOf(section));
        }
    }

    private List<Section> makeOrderedSections(Section firstSection) {
        List<Section> orderedSections = new ArrayList<>();
        orderedSections.add(firstSection);

        Section nextSection = firstSection;
        while (nextSection != null) {
            Optional<Section> findSection = findNextDownSection(nextSection.getDownStation());

            if (!findSection.isPresent()) {
                break;
            }

            nextSection = findSection.get();
            orderedSections.add(nextSection);
        }

        return orderedSections;
    }

    private List<Station> makeOrderedStations(Station firstStation) {
        List<Station> orderedStations = new ArrayList<>();
        orderedStations.add(firstStation);

        Station nextStation = firstStation;
        while (nextStation != null) {
            Optional<Section> findSection = findNextDownSection(nextStation);

            if (!findSection.isPresent()) {
                break;
            }

            nextStation = findSection.get().getDownStation();
            orderedStations.add(nextStation);
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

    private Optional<Section> findNextDownSection(Station station) {
        return sections.stream()
                .filter(section -> section.hasSameUpStation(station))
                .findFirst();
    }

    private Optional<Section> findNextUpSection(Station station) {
        return sections.stream()
                .filter(section -> section.hasSameDownStation(station))
                .findFirst();
    }
}
