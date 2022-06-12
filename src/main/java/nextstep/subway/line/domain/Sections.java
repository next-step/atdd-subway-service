package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getList() {
        return sections;
    }

    public void addSection(final Line line, final Station upStation, final Station downStation,
                           final Distance distance) {

        final List<Station> stations = getStations();

        checkAddableSection(stations, upStation, downStation);

        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        if (isStationExisted(upStation)) {
            updateUpToDownStation(upStation, downStation, distance);
        }

        if (isStationExisted(downStation)) {
            updateDownToUpStation(upStation, downStation, distance);
        }

        sections.add(new Section(line, upStation, downStation, distance));

    }

    private void checkAddableSection(final List<Station> stations, final Station upStation, final Station downStation) {
        if (isStationExisted(upStation) && isStationExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (isBothStationNotExisted(upStation, downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isStationExisted(final Station station) {
        return getStations()
                .stream()
                .anyMatch(it -> it.equals(station));
    }

    private boolean isBothStationNotExisted(final Station upStation, final Station downStation) {
        return !getStations().isEmpty() && !isStationExisted(upStation) &&
                !isStationExisted(downStation);
    }

    private void updateUpToDownStation(Station upStation, Station downStation, Distance distance) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownToUpStation(Station upStation, Station downStation, Distance distance) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        final List<Station> sortedStations = new ArrayList<>();
        final Station rootStation = searchRootStation();
        sortedStations.add(rootStation);

        Optional<Station> nextStation = findNextStation(rootStation);
        while (nextStation.isPresent()) {
            final Station next = nextStation.get();
            sortedStations.add(next);
            nextStation = findNextStation(next);
        }
        return sortedStations;
    }

    private Station searchRootStation() {
        final Set<Station> upStations = findUpStations();
        final Set<Station> downStations = findDownStations();
        return upStations.stream()
                .filter(upStation ->
                        !downStations.contains(upStation))
                .findFirst().get();
    }

    private Set<Station> findUpStations() {
        return sections.stream()
                .map(
                        section -> section.getUpStation())
                .collect(
                        Collectors.toSet());
    }

    private Set<Station> findDownStations() {
        return sections.stream()
                .map(section ->
                        section.getDownStation())
                .collect(Collectors.toSet());
    }

    private Optional<Station> findNextStation(final Station station) {
        final Optional<Section> nextSection = sections.stream()
                .filter(section ->
                        section.isEqualToUpStation(station))
                .findFirst();
        return nextSection.map(Section::getDownStation);
    }
}
