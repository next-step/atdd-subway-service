package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of(Section... sections) {
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

    private void modifyIfSameUpStationExisted(Section section) {
        if (isExistsStation(section.getUpStation())) {
            sections.stream()
                    .filter(it -> it.hasSameUpStation(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void modifyIfSameDownStationExisted(Section section) {
        if (isExistsStation(section.getDownStation())) {
            sections.stream()
                    .filter(it -> it.hasSameDownStation(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
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
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNotExistsUpStationAndDownStation(Section section) {
        if (isNotExistsStation(section.getUpStation()) && isNotExistsStation(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
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
