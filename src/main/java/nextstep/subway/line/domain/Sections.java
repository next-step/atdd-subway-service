package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.LineExceptionType;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> list;

    protected Sections() {
        this.list = new ArrayList<>();
    }

    public static Sections instance() {
        return new Sections();
    }

    public void add(final Section section) {
        additionalValidation(section);

        if (list.isEmpty()) {
            list.add(section);
            return;
        }
        updateUpStation(section);
        updateDownStation(section);

        list.add(section);
    }

    private void additionalValidation(final Section section) {
        existsBothStation(section);
        canNotRegisterSection(section);
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

    private void canNotRegisterSection(final Section section) {
        if (!list.isEmpty() && isUpStationNotExisted(section.getUpStation())
                && isDownStationNotExisted(section.getDownStation())) {
            throw new LineException(LineExceptionType.CAN_NOT_REGISTER_SECTION);
        }
    }

    private boolean isUpStationNotExisted(final Station upStation) {
        return getAllStations().stream()
                .noneMatch(upStation::equals);
    }

    private boolean isDownStationNotExisted(final Station downStation) {
        return getAllStations().stream()
                .noneMatch(downStation::equals);
    }

    private void updateUpStation(final Section section) {
        if (isUpStationExisted(section.getUpStation())) {
            list.stream()
                    .filter(it -> section.equalsUpStation(it.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void updateDownStation(final Section section) {
        if (isDownStationExisted(section.getDownStation())) {
            list.stream()
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
            list.add(updateMiddleStation);
        }

        upLineStation.ifPresent(list::remove);
        downLineStation.ifPresent(list::remove);
    }

    private void deletionValidation() {
        minSectionValidation();
    }

    private void minSectionValidation() {
        if (list.size() <= MIN_SECTION) {
            throw new LineException(LineExceptionType.MIN_SECTION_DELETION);
        }
    }

    private Optional<Section> getUpLineStation(final Station station) {
        return list.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst();
    }

    private Optional<Section> getDownLineStation(final Station station) {
        return list.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(list);
    }

    public List<Station> getAllStations() {
        final Set<Station> stations = new HashSet<>();
        for (Section section : list) {
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

    private Optional<Section> findFirstSection() {
        return list.stream()
                .filter(it -> !getDownStations().contains(it.getUpStation()))
                .findFirst();
    }

    private Optional<Section> nextSection(final Station station) {
        return list.stream()
                .filter(it -> station.equals(it.getUpStation()))
                .findFirst();
    }

    private List<Station> getDownStations() {
        return Collections.unmodifiableList(list.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "Sections{" +
                "list=" + list +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections = (Sections) o;
        return Objects.equals(list, sections.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
