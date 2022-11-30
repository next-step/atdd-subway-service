package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.exception.type.ValidExceptionType.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addSection(Line line, Station upStation, Station downStation, Distance distance) {
        validCheckAddSection(upStation, downStation);
        Section newSection = new Section(line, upStation, downStation, distance);

        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        if (isExistStation(upStation)) {
            updateUpStation(upStation, downStation, distance);
        }
        if (isExistStation(downStation)) {
            updateDownStation(upStation, downStation, distance);
        }

        sections.add(newSection);
    }

    private void validCheckAddSection(Station upStation, Station downStation) {
        boolean isExistUpStation = isExistStation(upStation);
        boolean isExistDownStation = isExistStation(downStation);

        if (!sections.isEmpty() && !isExistUpStation && !isExistDownStation) {
            throw new NotValidDataException(STATION_BOTH_NOT_EXIST.getMessage());
        }

        if (isExistUpStation && isExistDownStation) {
            throw new NotValidDataException(ALREADY_EXIST_LINE_STATION.getMessage());
        }
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
            Optional<Section> nextLineStation = getByUpStation(upStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            nextStation = nextLineStation.get().getDownStation();
            stations.add(nextStation);
        }

        return stations;
    }

    public void removeLineStation(Line line, Station station) {
        validCheckSectionSize();

        Optional<Section> upLineStation = getByUpStation(station);
        Optional<Section> downLineStation = getByDownStation(station);


        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addSection(upLineStation.get(), downLineStation.get(), line);
        }

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    private void addSection(Section upSection, Section downSection, Line line) {
        Distance newDistance = upSection.plusDistance(downSection);
        Section section = new Section(line, downSection.getUpStation(), upSection.getDownStation(), newDistance);
        sections.add(section);
    }

    private void validCheckSectionSize() {
        if (sections.size() <= 1) {
            throw new NotValidDataException(SECTIONS_MIN_SIZE_ONE.getMessage());
        }
    }

    private void updateUpStation(Station upStation, Station downStation, Distance distance) {
        getByUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance.getDistance()));
    }

    private void updateDownStation(Station upStation, Station downStation, Distance distance) {
        getByDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance.getDistance()));
    }

    private boolean isExistStation(Station station) {
        return getStations().stream().anyMatch(it -> it == station);
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = getByDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> getByUpStation(Station station) {
        return sections.stream()
                .filter(it -> it.isSameUpStation(station))
                .findFirst();
    }

    private Optional<Section> getByDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.isSameDownStation(station))
                .findFirst();
    }

    public List<Section> getSections() {
        return sections;
    }
}