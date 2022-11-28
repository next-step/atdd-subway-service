package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.LineExceptionType;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections;

    protected Sections() {
        this.sections = new ArrayList<>();
    }

    public void add(Section section) {
        validateDuplication(section);
        validateRegister(section);
        updateUpStation(section);
        updateDownStation(section);
        sections.add(section);
    }

    private void validateDuplication(Section section) {
        if (sections.stream().anyMatch(section::matchAllStations)) {
            throw new LineException(LineExceptionType.EXIST_SECTION);
        }
    }

    private void validateRegister(Section section) {
        if (!sections.isEmpty()) {
            validateExistence(section);
        }
    }

    private void validateExistence(Section section) {
        boolean upStationExist = sections.stream().anyMatch(section::hasUpStations);
        boolean downStationExist = sections.stream().anyMatch(section::hasDownStations);

        if (!upStationExist && !downStationExist) {
            throw new LineException(LineExceptionType.CAN_NOT_REGISTER_SECTION);
        }
    }

    private void existsBothStation(final Section section) {
        if (isUpStationExisted(section.getUpStation()) && isDownStationExisted(section.getDownStation())) {
            throw new LineException(LineExceptionType.EXIST_SECTION);
        }
    }

    private boolean isUpStationExisted(final Station upStation) {
        return getAllStations().stream()
                .anyMatch(it -> it.equals(upStation));
    }

    private boolean isDownStationExisted(final Station downStation) {
        return getAllStations().stream()
                .anyMatch(it -> it.equals(downStation));
    }

    private void updateUpStation(final Section section) {
        if (isUpStationExisted(section.getUpStation())) {
            sections.stream()
                    .filter(it -> section.equalsUpStation(it.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void updateDownStation(final Section section) {
        if (isDownStationExisted(section.getDownStation())) {
            sections.stream()
                    .filter(it -> section.equalsDownStation(it.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        }
    }

    public void delete(final Station station) {
        deletionValidation();
        final Optional<Section> upLineStation = getUpLineStation(station);
        final Optional<Section> downLineStation = getDownLineStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            final Section updateMiddleStation = upLineStation.get().updateMiddleStation(downLineStation.get());
            sections.add(updateMiddleStation);
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private void deletionValidation() {
        minSectionValidation();
    }

    private void minSectionValidation() {
        if (sections.size() <= MIN_SECTION) {
            throw new LineException(LineExceptionType.MIN_SECTION_DELETION);
        }
    }

    private Optional<Section> getUpLineStation(final Station station) {
        return sections.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst();
    }

    private Optional<Section> getDownLineStation(final Station station) {
        return sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public List<Station> getAllStations() {
        final Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        return Collections.unmodifiableList(new ArrayList<>(stations));
    }

    public List<Station> getSortedStations() {
        final Set<Station> sortedStations = new LinkedHashSet<>();
        Optional<Section> section = findFirstSection();

        while (section.isPresent()) {
            sortedStations.add(section.get().getUpStation());
            sortedStations.add(section.get().getDownStation());
            section = nextSection(section.get().getDownStation());
        }

        return Collections.unmodifiableList(new ArrayList<>(sortedStations));
    }

    public List<StationResponse> toStationResponses() {
        return getSortedStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private Optional<Section> findFirstSection() {
        return sections.stream()
                .filter(it -> !getDownStations().contains(it.getUpStation()))
                .findFirst();
    }

    private Optional<Section> nextSection(final Station station) {
        return sections.stream()
                .filter(it -> station.equals(it.getUpStation()))
                .findFirst();
    }

    private List<Station> getDownStations() {
        return Collections.unmodifiableList(sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections = (Sections) o;
        return Objects.equals(sections, sections.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
