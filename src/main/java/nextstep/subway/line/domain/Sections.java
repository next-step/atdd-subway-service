package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.exception.SectionAlreadyExistException;
import nextstep.subway.exception.SectionCannotDeleteException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        validateAddSection(upStation, downStation);

        Section newSection = new Section(line, upStation, downStation, distance);
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        if (hasStation(upStation)) {
            updateUpStation(upStation, downStation, distance);
            sections.add(newSection);
            return;
        }
        if (hasStation(downStation)) {
            updateDownStation(upStation, downStation, distance);
            sections.add(newSection);
            return;
        }
        throw new InvalidSectionException();
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

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getUpLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void removeLineStation(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new SectionCannotDeleteException();
        }

        Optional<Section> upLineStation = getUpLineStation(station);
        Optional<Section> downLineStation = getDownLineStation(station);

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section upSection = upLineStation.get();
            Section downSection = downLineStation.get();
            sections.add(new Section(line, downSection.getUpStation(), upSection.getDownStation(), upSection.getDistance() + downSection.getDistance()));
        }
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        getUpLineStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        getDownLineStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private boolean hasStation(Station station) {
        return getStations().stream().anyMatch(it -> it == station);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getDownLineStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> getUpLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst();
    }

    private Optional<Section> getDownLineStation(Station station) {
        return sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst();
    }
}
