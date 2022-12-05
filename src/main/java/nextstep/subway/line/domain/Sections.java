package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.exception.SectionAlreadyExistException;
import nextstep.subway.exception.SectionCannotDeleteException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public void addSection(Line line, Station upStation, Station downStation, Distance distance) {
        validateAddSection(upStation, downStation);

        Section newSection = new Section(line, upStation, downStation, distance);
        if (hasStation(upStation)) {
            updateUpStation(upStation, downStation, distance);
        }
        if (hasStation(downStation)) {
            updateDownStation(upStation, downStation, distance);
        }
        sections.add(newSection);
    }

    private void validateAddSection(Station upStation, Station downStation) {
        boolean hasUpStation = hasStation(upStation);
        boolean hasDownStation = hasStation(downStation);

        if (hasUpStation && hasDownStation) {
            throw new SectionAlreadyExistException();
        }

        if (!sections.isEmpty() && !hasUpStation && !hasDownStation) {
            throw new InvalidSectionException();
        }
    }

    public List<Line> findLinesContainedStations(List<Station> stations) {
        return sections.stream()
                .map(Section::getLine)
                .filter(line -> stations.containsAll(line.getStations()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station nextStation = findUpStation();
        stations.add(nextStation);

        while (nextStation != null) {
            Station upStation = nextStation;
            Optional<Section> nextLineStation = findSectionByUpStation(upStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            nextStation = nextLineStation.get().getDownStation();
            stations.add(nextStation);
        }

        return stations;
    }

    public void removeLineStation(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new SectionCannotDeleteException();
        }

        Optional<Section> upLineStation = findSectionByUpStation(station);
        Optional<Section> downLineStation = findSectionByDownStation(station);

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section upSection = upLineStation.get();
            Section downSection = downLineStation.get();
            Distance distance = Distance.sum(upSection.getDistance(), downSection.getDistance());
            sections.add(new Section(line, downSection.getUpStation(), upSection.getDownStation(), distance));
        }
    }

    private void updateUpStation(Station upStation, Station downStation, Distance distance) {
        findSectionByUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, Distance distance) {
        findSectionByDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private boolean hasStation(Station station) {
        return getStations().stream().anyMatch(it -> it == station);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findSectionByDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();
    }
}
