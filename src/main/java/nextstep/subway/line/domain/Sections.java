package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DuplicateBothStationException;
import nextstep.subway.line.exception.NotMatchedStationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations();
        validateAddSection(upStation, downStation, stations);

        if (stations.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        findOverlapSection(upStation, downStation)
                .ifPresent(it -> it.updateStation(upStation, downStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }

    private Optional<Section> findOverlapSection(Station upStation, Station downStation) {
        Optional<Section> findUpStation = sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst();
        if (findUpStation.isPresent()) {
            return findUpStation;
        }

        return sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();
    }

    private void validateAddSection(Station upStation, Station downStation, List<Station> stations) {
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new DuplicateBothStationException(upStation, downStation);
        }

        if (!stations.isEmpty() &&
                stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new NotMatchedStationException(upStation, downStation);
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
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }
}
